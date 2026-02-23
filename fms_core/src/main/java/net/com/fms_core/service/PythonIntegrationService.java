package net.com.fms_core.service;

import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.dto.ImpossibleDistanceResult;

public interface PythonIntegrationService {
    ImpossibleDistanceResult callPythonAnalysis(IsoMessageDTO currentTransaction);
    boolean isPythonAvailable();
    double[] predictImpossibleTransaction(double distance, long timeDiffMinutes, double requiredSpeed);
}