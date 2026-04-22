package net.com.fms_core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.com.fms_core.dto.ImpossibleDistanceResult;
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.entity.geo.GeoCache;
import net.com.fms_core.repository.geo.GeoCacheRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */

@Service
@RequiredArgsConstructor
public class GeoService {

    private final GeoCacheRepository repo;

    public double[] getCoordinates(String location) {

        String cleaned = clean(location);
        GeoCache cached = repo.findById(cleaned).orElse(null);

        if (cached != null) {
            return new double[] { cached.getLatitude(), cached.getLongitude() };
        }

        double[] coords = queryApi(cleaned);

        if (coords != null) {
            GeoCache g = new GeoCache();
            g.setLocation(cleaned);
            g.setLatitude(coords[0]);
            g.setLongitude(coords[1]);
//            g.setUpdatedAt(System.currentTimeMillis());
            repo.save(g);

            return coords;
        }

        return null;
    }

    private String clean(String text) {
        if (text == null) return "";
        return text.trim().replaceAll(" +", " ").toLowerCase();
    }

    // Common country code aliases to normalize before geocoding
    private static final Map<String, String> COUNTRY_ALIASES = Map.of(
        "sl", "Sri Lanka",
        "lk", "Sri Lanka",
        "us", "United States",
        "uk", "United Kingdom",
        "gb", "United Kingdom",
        "in", "India",
        "au", "Australia"
    );

    private double[] queryApi(String location) {
        try {
            // Parse location: "MERCHANT NAME     City     CountryCode"
            String[] parts = location.trim().split("\\s+");
            String city = "";
            String country = "";

            if (parts.length >= 2) {
                String rawCountry = parts[parts.length - 1].toLowerCase();
                country = COUNTRY_ALIASES.getOrDefault(rawCountry, parts[parts.length - 1]);
                city = parts[parts.length - 2];
            } else {
                city = location;
            }

            String query = city + (country.isEmpty() ? "" : ", " + country);
            String encodedQuery = java.net.URLEncoder.encode(query, "UTF-8");
            String url = "https://nominatim.openstreetmap.org/search?format=json&q=" + encodedQuery;

            RestTemplate rest = new RestTemplate();
            rest.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().add("User-Agent", "FMS-FraudDetection/1.0");
                return execution.execute(request, body);
            });

            var response = rest.getForObject(url, Object[].class);

            if (response == null || response.length == 0) {
                return null;
            }

            Map map = (Map) response[0];
            double lat = Double.parseDouble(map.get("lat").toString());
            double lon = Double.parseDouble(map.get("lon").toString());

            return new double[] { lat, lon };

        } catch (Exception e) {
            return null;
        }
    }
}
