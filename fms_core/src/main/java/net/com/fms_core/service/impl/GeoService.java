package net.com.fms_core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.com.fms_core.dto.ImpossibleDistanceResult;
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.entity.geo.GeoCache;
import net.com.fms_core.repository.geo.GeoCacheRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
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
        text = text.trim();
        text = text.replaceAll(" +", " ");
        return text;
    }

    private double[] queryApi(String location) {
        try {
            // Parse location: "MERCHANT NAME City CountryCode"
            String[] parts = location.split("\\s+");
            String city = "";
            String country = "";
            
            if (parts.length >= 2) {
                country = parts[parts.length - 1]; // Last part is country code
                city = parts[parts.length - 2]; // Second last is city
            } else {
                city = location;
            }
            
            String query = city + (country.isEmpty() ? "" : ", " + country);
            String url = "https://nominatim.openstreetmap.org/search?format=json&q=" + query;

            RestTemplate rest = new RestTemplate();
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
