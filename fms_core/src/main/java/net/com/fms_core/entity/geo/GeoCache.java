package net.com.fms_core.entity.geo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */

@Entity
@Data
public class GeoCache {

    @Id
    private String location;

    private Double latitude;
    private Double longitude;
}
