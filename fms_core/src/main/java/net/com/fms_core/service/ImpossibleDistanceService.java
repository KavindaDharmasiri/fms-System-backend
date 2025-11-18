package net.com.fms_core.service;

import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.dto.ImpossibleDistanceResult;

public interface ImpossibleDistanceService {
    ImpossibleDistanceResult checkImpossibleDistance(IsoMessageDTO currentTransaction);
    double calculateDistance(double lat1, double lon1, double lat2, double lon2);
    double calculateMaxPossibleSpeed(String transportMode);
    boolean isImpossibleTravel(double distance, long timeDiffMinutes, double maxSpeed);
}