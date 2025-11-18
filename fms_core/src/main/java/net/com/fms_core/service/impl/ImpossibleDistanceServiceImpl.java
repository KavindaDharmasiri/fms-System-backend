package net.com.fms_core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.ImpossibleDistanceResult;
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.entity.TransactionHistory;
import net.com.fms_core.repository.TransactionRepository;
import net.com.fms_core.service.ImpossibleDistanceService;
import net.com.fms_core.service.PythonIntegrationService;
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
    
    // Earth's radius in kilometers
    private static final double EARTH_RADIUS_KM = 6371.0;
    
    @Override
    public ImpossibleDistanceResult checkImpossibleDistance(IsoMessageDTO currentTransaction) {
        try {
            // Try MATLAB analysis first if available
            if (pythonIntegrationService.isPythonAvailable()) {
                log.info("Using MATLAB for enhanced geospatial analysis");
                ImpossibleDistanceResult matlabResult = pythonIntegrationService.callPythonAnalysis(currentTransaction);
                if (matlabResult != null && !"MATLAB unavailable".contains(matlabResult.getAlertMessage())) {
                    return matlabResult;
                }
            }
            
            // Fallback to Java implementation
            log.info("Using Java implementation for distance analysis");
            String cardNumber = currentTransaction.getPan();
            if (cardNumber == null || cardNumber.length() < 12) {
                return createNoRiskResult("Invalid card number");
            }
            
            // Get last transaction for same card within 24 hours
            List<TransactionHistory> recentTransactions = transactionRepository
                .findRecentTransactionsByCardNumber(cardNumber.substring(0, 12), 24);
            System.out.println(recentTransactions.size());
            
            if (recentTransactions.isEmpty()) {
                return createNoRiskResult("No previous transactions found");
            }
            
            TransactionHistory lastTransaction = recentTransactions.get(0);
            IsoMessageDTO lastTxnData = parseTransactionPacket(lastTransaction.getTranPacket());
            
            if (lastTxnData == null) {
                return createNoRiskResult("Unable to parse previous transaction");
            }
            
            // Extract location data
            LocationData currentLoc = extractLocation(currentTransaction);
            LocationData previousLoc = extractLocation(lastTxnData);

            System.out.println(currentLoc.locationName);
            System.out.println(currentLoc.latitude);
            System.out.println(currentLoc.longitude);
            System.out.println("------------------------");
            System.out.println(previousLoc.locationName);
            System.out.println(previousLoc.latitude);
            System.out.println(previousLoc.longitude);
            if (currentLoc == null || previousLoc == null) {
                return createNoRiskResult("Location data unavailable");
            }
            
            // Calculate distance and time difference
            double distance = calculateDistance(
                previousLoc.latitude, previousLoc.longitude,
                currentLoc.latitude, currentLoc.longitude
            );
            
            // Convert Date to LocalDateTime for time calculation
            LocalDateTime lastTxnTime = lastTransaction.getCreatedAt().toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
            
            long timeDiffMinutes = ChronoUnit.MINUTES.between(
                lastTxnTime, LocalDateTime.now()
            );
            
            if (timeDiffMinutes <= 0) {
                timeDiffMinutes = 1; // Prevent division by zero
            }
            
            // Calculate required speed
            double requiredSpeedKmh = (distance / timeDiffMinutes) * 60;
            
            // Determine max possible speed based on context
            double maxPossibleSpeed = calculateMaxPossibleSpeed("COMMERCIAL_FLIGHT");
            
            // Check if travel is impossible
            boolean isImpossible = isImpossibleTravel(distance, timeDiffMinutes, maxPossibleSpeed);
            
            return new ImpossibleDistanceResult(
                isImpossible,
                Math.round(distance * 100.0) / 100.0,
                timeDiffMinutes,
                Math.round(requiredSpeedKmh * 100.0) / 100.0,
                maxPossibleSpeed,
                isImpossible ? "HIGH" : (requiredSpeedKmh > 100 ? "MID" : "LOW"),
                previousLoc.locationName,
                currentLoc.locationName,
                cardNumber.substring(0, 12) + "****",
                isImpossible ? "IMPOSSIBLE DISTANCE: Card used in impossible timeframe" : "Normal travel pattern"
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
            // Extract from Field 43 - Card Acceptor Name/Location
            String cardAcceptorNameLocation = transaction.getCardAcceptorNameLocation();
            
            if (cardAcceptorNameLocation != null && !cardAcceptorNameLocation.isEmpty()) {
                return parseCardAcceptorLocation(cardAcceptorNameLocation);
            }
            
            // Fallback: Generate location based on other fields
            return generateLocationFromOtherFields(transaction);
            
        } catch (Exception e) {
            log.warn("Failed to extract location: {}", e.getMessage());
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
        // Simplified geocoding - in production, use Google Maps API or similar
        LocationData location = new LocationData();
        
        // Sample coordinates for major cities (replace with actual geocoding service)
        String key = (city + "_" + country).toUpperCase();
        
        switch (key) {
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
            default:
                // Default to approximate coordinates based on country
                return getCountryCoordinates(country);
        }
        
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
    }
}
