/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto.RiskManagement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.com.fms_core.dto.message.VisaField44DTO;
import net.com.fms_core.dto.message.VisaField62DTO;
import net.com.fms_core.dto.message.VisaField95DTO;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private double amount;
    private String location;
    private int frequency;
    private int tranId;
    private int customerRiskScore;
    private boolean fraudulent;
    private String timeOfDay;
    private String transactionType;
    private String vendorStatus;
    private String accountNumber;
    private String deviceStatus;
    private String accountType;
    private String paymentMethod;
    private String spendingPattern;
    private String customerStatus;
    private double timeDifference; // Time difference between transactions
    private boolean firstTimeCustomer;
    private String country;
    private int accountAge;
    private String currency;
    private String ipAddress;
    private String timezone;
    private String merchantStatus;
    private String recipientType;
    private String entityStatus;
    private int industryRiskScore;
    private String merchantCategory;
    private String merchantGroupStatus;
    private String merchantTypeStatus;
    private String merchantSubcategoryStatus;
    private String merchantSegmentStatus;
    private String merchantClusterStatus;
    private String accountLocation;
    private String paymentChannel;
    private int failedAttempts;
    private int failedLoginAttempts;
    private int accountRiskScore;
    private int countryRiskScore;
    private int merchantRiskScore;
    private int merchantCategoryRiskScore;
    private int merchantGroupRiskScore;
    private int merchantSegmentRiskScore;
    private int merchantClusterRiskScore;
    private int merchantSubcategoryRiskScore;
    private String accountStatus;
    private String vendorType;
    private int vendorRiskScore;
    private int merchantTypeRiskScore;
    private String riskLevel;
    private boolean flaggedForReview;
    private LocalDateTime timestamp;
    private String userId;
    private String cardNumber;
    private LocalDateTime timeOfTransaction;
    private boolean ruleFired;
    protected int messageTypeIndicator; // Field 0
    protected String bitMap; // Field 1
    protected String pan; // Field 2
    protected String processingCode; // Field 3
    protected String settlementAmount; // Field 5
    protected String cardholderBillingAmount; // Field 6
    protected String transactionDateTime; // Field 7
    protected String cardholderBillingFeeAmount; // Field 8
    protected String settlementConversionRate; // Field 9
    protected String cardholderBillingConversionRate; // Field 10
    protected String stan; // Field 11
    protected String localTransactionTime; // Field 12
    protected String localTransactionDate; // Field 13
    protected String expiryDate; // Field 14
    protected String settlementDate; // Field 15
    protected String conversionDate; // Field 16
    protected String captureDate; // Field 17
    protected String merchantCategoryCode; // Field 18
    protected String acquiringCountryCode; // Field 19
    protected String panExtendedCountryCode; // Field 20
    protected String forwardingCountryCode; // Field 21
    protected String posEntryMode; // Field 22
    protected String cardSequenceNumber; // Field 23
    protected String networkId; // Field 24
    protected String posConditionCode; // Field 25
    protected String posPinCaptureCode; // Field 26
    protected String authResponseLength; // Field 27
    protected String transactionFeeAmount; // Field 28
    protected String settlementFeeAmount; // Field 29
    protected String transactionProcessingFee; // Field 30
    protected String settlementProcessingFee; // Field 31
    protected String acquirerInstitutionId; // Field 32
    protected String forwardingInstitutionId; // Field 33
    protected String panExtended; // Field 34
    protected String track2Data; // Field 35
    protected String track3Data; // Field 36
    protected String retrievalReferenceNumber; // Field 37
    protected String authResponse; // Field 38
    protected String responseCode; // Field 39
    protected String serviceRestrictionCode; // Field 40
    protected String terminalId; // Field 41
    protected String cardAcceptorId; // Field 42
    protected String cardAcceptorNameLocation; // Field 43
    protected VisaField44DTO additionalResponseData; // Field 44
    protected String track1Data; // Field 45
    protected String additionalDataIso; // Field 46
    protected String additionalDataNational; // Field 47
    protected String additionalDataPrivate; // Field 48
    protected String transactionCurrencyCode; // Field 49
    protected String settlementCurrencyCode; // Field 50
    protected String cardholderBillingCurrencyCode; // Field 51
    protected String pinData; // Field 52
    protected String securityControlInfo; // Field 53
    protected String balanceInformation; // Field 54
    protected String reservedIso55; // Field 55
    protected String reservedIso56; // Field 56
    protected String reservedNational57; // Field 57
    protected String reservedNational58; // Field 58
    protected String nationalPosGeographicData; // Field 59
    protected String reservedPrivate60; // Field 60
    protected String reservedPrivate61; // Field 61
    protected VisaField62DTO field62; // Field 62
    protected String smsPrivateFields; // Field 63
    protected String macField; // Field 64
    protected String extendedBitMap; // Field 65
    protected String settlementCode; // Field 66
    protected String extendedPaymentCode; // Field 67
    protected String receivingInstitutionCountryCode; // Field 68
    protected String settlementInstitutionCountryCode; // Field 69
    protected String networkManagementCode; // Field 70
    protected String messageNumber; // Field 71
    protected String messageNumberLast; // Field 72
    protected String dateAction; // Field 73
    protected String creditsNumber; // Field 74
    protected String creditsReversalNumber; // Field 75
    protected String debitsNumber; // Field 76
    protected String debitsReversalNumber; // Field 77
    protected String transferNumber; // Field 78
    protected String transferReversalNumber; // Field 79
    protected String inquiriesNumber; // Field 80
    protected String authorizationNumber; // Field 81
    protected String creditsProcessingFee; // Field 82
    protected String creditsTransactionFee; // Field 83
    protected String debitsProcessingFee; // Field 84
    protected String debitsTransactionFee; // Field 85
    protected String creditsAmount; // Field 86
    protected String creditsReversalAmount; // Field 87
    protected String debitsAmount; // Field 88
    protected String debitsReversalAmount; // Field 89
    protected String originalDataElements; // Field 90
    protected String fileUpdateCode; // Field 91
    protected String fileSecurityCode; // Field 92
    protected String responseIndicator; // Field 93
    protected String serviceIndicator; // Field 94
    protected VisaField95DTO field95; // Field 95
    protected String messageSecurityCode; // Field 96
    protected String netSettlementAmount; // Field 97
    protected String payee; // Field 98
    protected String settlementInstitutionId; // Field 99
    protected String receivingInstitutionId; // Field 100
    protected String fileName; // Field 101
    protected String accountIdentification1; // Field 102
    protected String accountIdentification2; // Field 103
    protected String transactionDescription; // Field 104
    protected String reservedIso105;
    protected String reservedIso106;
    protected String reservedIso107;
    protected String reservedIso108;
    protected String reservedIso109;
    protected String reservedIso110;
    protected String reservedIso111;
    protected String reservedNational112;
    protected String reservedNational113;
    protected String reservedNational114;
    protected String reservedNational115;
    protected String reservedNational116;
    protected String reservedNational117;
    protected String reservedNational118;
    protected String reservedNational119;
    protected String reservedPrivate120;
    protected String reservedPrivate121;
    protected String reservedPrivate122;
    protected String reservedPrivate123;
    protected String reservedPrivate124;
    protected String reservedPrivate125;
    protected String mac2; // Field 128
    private String UUID;
    private int id;
}
