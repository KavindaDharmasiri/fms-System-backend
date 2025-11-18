package net.com.fms_core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImpossibleDistanceResult {
    private boolean isImpossible;
    private double distanceKm;
    private long timeDifferenceMinutes;
    private double requiredSpeedKmh;
    private double maxPossibleSpeedKmh;
    private String riskLevel;
    private String previousLocation;
    private String currentLocation;
    private String cardNumber;
    private String alertMessage;
}