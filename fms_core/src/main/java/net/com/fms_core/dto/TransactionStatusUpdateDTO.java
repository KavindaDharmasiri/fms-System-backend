package net.com.fms_core.dto;

import lombok.Data;

@Data
public class TransactionStatusUpdateDTO {
    private String transactionUuid;
    private String newStatus;
    private String reason;
    private String reviewedBy;
}
