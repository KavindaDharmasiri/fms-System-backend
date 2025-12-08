package net.com.fms_core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.ImpossibleDistanceResult;
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.entity.TransactionHistory;
import net.com.fms_core.repository.TransactionRepository;
import net.com.fms_core.service.ImpossibleDistanceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImpossibleDistanceServiceImpl implements ImpossibleDistanceService {
    
    private final TransactionRepository transactionRepository;
    private final ObjectMapper objectMapper;
    private final PythonIntegrationService pythonIntegrationService;
    private final GeoService geoService;

    // Earth's radius in kilometers
    private static final double EARTH_RADIUS_KM = 6371.0;

    @Override
    public ImpossibleDistanceResult checkImpossibleDistance(IsoMessageDTO currentTransaction) {
        try {
            log.info("Using Java + Python model for distance analysis");
            System.out.println("=== DISTANCE CHECK START ===");

            String cardNumber = currentTransaction.getPan();
            System.out.println("Card number: " + (cardNumber != null ? cardNumber.substring(0, Math.min(12, cardNumber.length())) + "****" : "NULL"));
            if (cardNumber == null || cardNumber.length() < 12) {
                System.out.println("EARLY EXIT: Invalid card number");
                return createNoRiskResult("Invalid card number");
            }

            // Get last transaction for same card within 24 hours
//            List<TransactionHistory> recentTransactions = transactionRepository
//                    .findRecentTransactionsByCardNumber(cardNumber.substring(0, 12), 24);

            List<TransactionHistory> recentTransactions = transactionRepository.findAllByOrderByTransactionHistoryIdDesc();
            System.out.println("Recent transactions found: " + recentTransactions.size());

            if (recentTransactions.isEmpty()) {
                System.out.println("EARLY EXIT: No previous transactions found");
                return createNoRiskResult("No previous transactions found");
            }

            TransactionHistory lastTransaction = recentTransactions.get(0);
            IsoMessageDTO lastTxnData = parseTransactionPacket(lastTransaction.getTranPacket());
            if (lastTxnData == null) {
                System.out.println("EARLY EXIT: Unable to parse previous transaction");
                return createNoRiskResult("Unable to parse previous transaction");
            }

            // Extract location data
            System.out.println("Extracting locations...");
            LocationData currentLoc = extractLocation(currentTransaction);
            LocationData previousLoc = extractLocation(lastTxnData);
            if (currentLoc == null || previousLoc == null) {
                System.out.println("EARLY EXIT: Location data unavailable - current: " + currentLoc + ", previous: " + previousLoc);
                return createNoRiskResult("Location data unavailable");
            }
            System.out.println("///////////////////////////////////////////////");
            System.out.println(currentLoc.toString());
            System.out.println(previousLoc.toString());

            // Calculate distance and time difference
            double distance = calculateDistance(previousLoc.latitude, previousLoc.longitude,
                    currentLoc.latitude, currentLoc.longitude);
            System.out.println(distance);
            LocalDateTime lastTxnTime = lastTransaction.getCreatedAt().toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();

            long timeDiffMinutes = ChronoUnit.MINUTES.between(lastTxnTime, LocalDateTime.now());
            if (timeDiffMinutes <= 0) timeDiffMinutes = 1;

            double requiredSpeedKmh = (distance / timeDiffMinutes) * 60;
            double maxPossibleSpeed = calculateMaxPossibleSpeed("COMMERCIAL_FLIGHT");

            // Java rule-based check
            boolean isImpossibleJava = isImpossibleTravel(distance, timeDiffMinutes, maxPossibleSpeed);

            // Call Python XGBoost model
            double[] modelResult = pythonIntegrationService.predictImpossibleTransaction(distance, timeDiffMinutes, requiredSpeedKmh);
            boolean isImpossibleByModel = modelResult[0] == 1;
            double modelProbability = modelResult[1];

            // Combine Java and Python results
            boolean finalImpossible = isImpossibleJava || isImpossibleByModel;
            String riskLevel = finalImpossible ? "HIGH" : (requiredSpeedKmh > 100 ? "MID" : "LOW");
            String alertMessage = finalImpossible
                    ? "IMPOSSIBLE DISTANCE: Card used in impossible timeframe (Model probability: " + modelProbability + ")"
                    : "Normal travel pattern";

            System.out.println(alertMessage);
            return new ImpossibleDistanceResult(
                    finalImpossible,
                    Math.round(distance * 100.0) / 100.0,
                    timeDiffMinutes,
                    Math.round(requiredSpeedKmh * 100.0) / 100.0,
                    maxPossibleSpeed,
                    riskLevel,
                    previousLoc.locationName,
                    currentLoc.locationName,
                    cardNumber.substring(0, 12) + "****",
                    alertMessage
            );

        } catch (Exception e) {
            log.error("Error checking impossible distance: {}", e.getMessage());
            return createNoRiskResult("Error during distance check");
        }
    }


    @Override
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula for great-circle distance
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS_KM * c;
    }
    
    @Override
    public double calculateMaxPossibleSpeed(String transportMode) {
        // Maximum realistic speeds (km/h) including processing delays
        switch (transportMode.toUpperCase()) {
            case "WALKING": return 6;
            case "BICYCLE": return 25;
            case "CAR": return 130;
            case "TRAIN": return 320;
            case "COMMERCIAL_FLIGHT": return 900;
            case "SUPERSONIC": return 2100;
            default: return 900; // Default to commercial flight speed
        }
    }
    
    @Override
    public boolean isImpossibleTravel(double distance, long timeDiffMinutes, double maxSpeed) {
        if (distance < 1.0) return false; // Same city transactions
        
        double requiredSpeed = (distance / timeDiffMinutes) * 60;
        
        // Add 20% buffer for processing delays and route variations
        double adjustedMaxSpeed = maxSpeed * 1.2;
        
        return requiredSpeed > adjustedMaxSpeed;
    }
    
    private LocationData extractLocation(IsoMessageDTO transaction) {
        try {
            String raw = transaction.getCardAcceptorNameLocation();

            if (raw != null && !raw.trim().isEmpty()) {

                // Call Python API through your integration service
                double[] coords = geoService.getCoordinates(raw);

                if (coords != null && coords.length == 2) {
                    LocationData loc = new LocationData();
                    loc.latitude = coords[0];
                    loc.longitude = coords[1];
                    loc.locationName = raw;
                    return loc;
                }
            }

            return generateDefaultLocation();

        } catch (Exception e) {
            log.warn("Location extraction failed: {}", e.getMessage());
            return generateDefaultLocation();
        }
    }
    
    private LocationData parseCardAcceptorLocation(String cardAcceptorNameLocation) {
        try {
            // Field 43 format: "MERCHANT NAME            CITY NAME    COUNTRY"
            // Example: "WALMART SUPERCENTER       NEW YORK     US"
            
            LocationData location = new LocationData();
            
            // Parse the fixed-format field
            if (cardAcceptorNameLocation.length() >= 40) {
                String merchantName = cardAcceptorNameLocation.substring(0, 25).trim();
                String cityName = cardAcceptorNameLocation.substring(25, 38).trim();
                String countryCode = cardAcceptorNameLocation.substring(38).trim();
                
                // Use city and country to get coordinates
                location = geocodeLocation(cityName, countryCode);
                
                if (location != null) {
                    location.locationName = cityName + ", " + countryCode;
                }
                
                log.debug("Parsed location: Merchant={}, City={}, Country={}", 
                         merchantName, cityName, countryCode);
                
                return location;
            }
            
            // If format is different, try to extract city and country
            String[] parts = cardAcceptorNameLocation.split("\\s+");
            if (parts.length >= 2) {
                String cityName = parts[parts.length - 2];
                String countryCode = parts[parts.length - 1];
                
                location = geocodeLocation(cityName, countryCode);
                if (location != null) {
                    location.locationName = cityName + ", " + countryCode;
                }
                
                return location;
            }
            
        } catch (Exception e) {
            log.warn("Failed to parse card acceptor location: {}", e.getMessage());
        }
        
        return generateDefaultLocation();
    }
    
    private LocationData generateLocationFromOtherFields(IsoMessageDTO transaction) {
        // Try other fields if Field 43 is not available
        
        // Method 1: Use Terminal ID
        if (transaction.getTerminalId() != null) {
            String terminalId = transaction.getTerminalId();
            LocationData location = getLocationFromTerminalId(terminalId);
            if (location != null) {
                return location;
            }
        }
        
        // Method 2: Use Acquiring Institution ID
        if (transaction.getAcquirerInstitutionId() != null) {
            String acquirerId = transaction.getAcquirerInstitutionId();
            LocationData location = getLocationFromAcquirer(acquirerId);
            if (location != null) {
                return location;
            }
        }
        
        return generateDefaultLocation();
    }
    
    private LocationData getLocationFromTerminalId(String terminalId) {
        LocationData location = new LocationData();
        
        // Extract country from terminal ID pattern
        if (terminalId.length() >= 2) {
            String prefix = terminalId.substring(0, 2).toUpperCase();
            
            switch (prefix) {
                case "US":
                    location.latitude = 40.7128; // New York
                    location.longitude = -74.0060;
                    location.locationName = "New York, US";
                    break;
                case "GB":
                    location.latitude = 51.5074; // London
                    location.longitude = -0.1278;
                    location.locationName = "London, GB";
                    break;
                case "JP":
                    location.latitude = 35.6762; // Tokyo
                    location.longitude = 139.6503;
                    location.locationName = "Tokyo, JP";
                    break;
                default:
                    return null;
            }
        }
        
        return location;
    }
    
    private LocationData getLocationFromAcquirer(String acquirerId) {
        LocationData location = new LocationData();
        
        // Map acquirer ID to location
        if (acquirerId.length() >= 3) {
            String prefix = acquirerId.substring(0, 3);
            
            switch (prefix) {
                case "001":
                    location.latitude = 40.7128; // US
                    location.longitude = -74.0060;
                    location.locationName = "New York, US";
                    break;
                case "002":
                    location.latitude = 51.5074; // UK
                    location.longitude = -0.1278;
                    location.locationName = "London, GB";
                    break;
                case "003":
                    location.latitude = 35.6762; // Japan
                    location.longitude = 139.6503;
                    location.locationName = "Tokyo, JP";
                    break;
                default:
                    return null;
            }
        }
        
        return location;
    }
    
    private LocationData generateDefaultLocation() {
        // Generate different locations for testing
        LocationData location = new LocationData();
        
        long currentTime = System.currentTimeMillis();
        int locationIndex = (int) (currentTime % 5);
        
        switch (locationIndex) {
            case 0:
                location.latitude = 40.7128; // New York
                location.longitude = -74.0060;
                location.locationName = "New York, US";
                break;
            case 1:
                location.latitude = 51.5074; // London
                location.longitude = -0.1278;
                location.locationName = "London, GB";
                break;
            case 2:
                location.latitude = 35.6762; // Tokyo
                location.longitude = 139.6503;
                location.locationName = "Tokyo, JP";
                break;
            case 3:
                location.latitude = 48.8566; // Paris
                location.longitude = 2.3522;
                location.locationName = "Paris, FR";
                break;
            default:
                location.latitude = -33.8688; // Sydney
                location.longitude = 151.2093;
                location.locationName = "Sydney, AU";
                break;
        }
        
        return location;
    }
    
    private LocationData geocodeLocation(String city, String country) {
        // Try OpenStreetMap Nominatim API first
        try {
            String url = String.format(
                "https://nominatim.openstreetmap.org/search?city=%s&country=%s&format=json&limit=1",
                java.net.URLEncoder.encode(city, "UTF-8"),
                java.net.URLEncoder.encode(country, "UTF-8")
            );
            
            org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
            restTemplate.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().add("User-Agent", "FMS-FraudDetection/1.0");
                return execution.execute(request, body);
            });
            
            String response = restTemplate.getForObject(url, String.class);
            
            if (response != null && !response.equals("[]")) {
                com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(response);
                if (jsonNode.isArray() && jsonNode.size() > 0) {
                    com.fasterxml.jackson.databind.JsonNode firstResult = jsonNode.get(0);
                    LocationData location = new LocationData();
                    location.latitude = firstResult.get("lat").asDouble();
                    location.longitude = firstResult.get("lon").asDouble();
                    location.locationName = city + ", " + country;
                    log.info("Geocoded {}, {} to: {}, {}", city, country, location.latitude, location.longitude);
                    return location;
                }
            }
        } catch (Exception e) {
            log.warn("Nominatim geocoding failed for {}, {}: {}", city, country, e.getMessage());
        }
        
        // Fallback to hardcoded coordinates
        LocationData location = new LocationData();
        
        // Sample coordinates for major cities (replace with actual geocoding service)
        String key = (city + "_" + country).toUpperCase();
        
        switch (key) {
            case "COLOMBO_LK":
                location.latitude = 6.9271;
                location.longitude = 79.8612;
                break;
            case "NEW YORK_US":
                location.latitude = 40.7128;
                location.longitude = -74.0060;
                break;
            case "LONDON_GB":
                location.latitude = 51.5074;
                location.longitude = -0.1278;
                break;
            case "TOKYO_JP":
                location.latitude = 35.6762;
                location.longitude = 139.6503;
                break;
            case "PARIS_FR":
                location.latitude = 48.8566;
                location.longitude = 2.3522;
                break;
            case "SYDNEY_AU":
                location.latitude = -33.8688;
                location.longitude = 151.2093;
                break;
            case "DUBAI_AE":
                location.latitude = 25.2048;
                location.longitude = 55.2708;
                break;
            case "SINGAPORE_SG":
                location.latitude = 1.3521;
                location.longitude = 103.8198;
                break;
            case "MUMBAI_IN":
                location.latitude = 19.0760;
                location.longitude = 72.8777;
                break;
            case "SAO PAULO_BR":
                location.latitude = -23.5505;
                location.longitude = -46.6333;
                break;
            case "MOSCOW_RU":
                location.latitude = 55.7558;
                location.longitude = 37.6176;
                break;
            case "BEIJING_CN":
                location.latitude = 39.9042;
                location.longitude = 116.4074;
                break;
            case "DELHI_IN":
                location.latitude = 28.7041;
                location.longitude = 77.1025;
                break;
            case "BANGKOK_TH":
                location.latitude = 13.7563;
                location.longitude = 100.5018;
                break;
            case "ISTANBUL_TR":
                location.latitude = 41.0082;
                location.longitude = 28.9784;
                break;
            case "TORONTO_CA":
                location.latitude = 43.6532;
                location.longitude = -79.3832;
                break;
            case "BERLIN_DE":
                location.latitude = 52.5200;
                location.longitude = 13.4050;
                break;
            case "MADRID_ES":
                location.latitude = 40.4168;
                location.longitude = -3.7038;
                break;
            case "ROME_IT":
                location.latitude = 41.9028;
                location.longitude = 12.4964;
                break;
            case "SEOUL_KR":
                location.latitude = 37.5665;
                location.longitude = 126.9780;
                break;
            case "HONG KONG_HK":
                location.latitude = 22.3193;
                location.longitude = 114.1694;
                break;
            case "LOS ANGELES_US":
                location.latitude = 34.0522;
                location.longitude = -118.2437;
                break;
            case "CHICAGO_US":
                location.latitude = 41.8781;
                location.longitude = -87.6298;
                break;
            case "BOSTON_US":
                location.latitude = 42.3601;
                location.longitude = -71.0589;
                break;
            case "MIAMI_US":
                location.latitude = 25.7617;
                location.longitude = -80.1918;
                break;
            case "KANDY_LK":
                location.latitude = 7.2906;
                location.longitude = 80.6337;
                break;
            case "GALLE_LK":
                location.latitude = 6.0535;
                location.longitude = 80.2210;
                break;
            case "NEGOMBO_LK":
                location.latitude = 7.2008;
                location.longitude = 79.8358;
                break;
            default:
                // Default to approximate coordinates based on country
                return getCountryCoordinates(country);
        }
        
        location.locationName = city + ", " + country;
        return location;
    }
    
    private LocationData getCountryCoordinates(String countryCode) {
        LocationData location = new LocationData();
        
        switch (countryCode.toUpperCase()) {
            case "US":
                location.latitude = 39.8283;
                location.longitude = -98.5795;
                break;
            case "GB":
                location.latitude = 55.3781;
                location.longitude = -3.4360;
                break;
            case "JP":
                location.latitude = 36.2048;
                location.longitude = 138.2529;
                break;
            case "FR":
                location.latitude = 46.2276;
                location.longitude = 2.2137;
                break;
            case "AU":
                location.latitude = -25.2744;
                location.longitude = 133.7751;
                break;
            default:
                return null; // Unknown location
        }
        
        return location;
    }
    
    private IsoMessageDTO parseTransactionPacket(String tranPacket) {
        try {
            return objectMapper.readValue(tranPacket, IsoMessageDTO.class);
        } catch (Exception e) {
            log.error("Failed to parse transaction packet: {}", e.getMessage());
            return null;
        }
    }
    
    private ImpossibleDistanceResult createNoRiskResult(String message) {
        return new ImpossibleDistanceResult(
            false, 0.0, 0L, 0.0, 0.0, "LOW", 
            "Unknown", "Unknown", "****", message
        );
    }
    
    private static class LocationData {
        double latitude;
        double longitude;
        String locationName;

        @Override
        public String toString() {
            return "LocationData{" +
                    "latitude=" + latitude +
                    ", longitude=" + longitude +
                    ", locationName='" + locationName + '\'' +
                    '}';
        }
    }
}
