/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto.message;
import lombok.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class IsoMessageDTO {
    protected int messageTypeIndicator; // Field 0
    protected String bitMap; // Field 1
    protected String pan; // Field 2
    protected int processingCode; // Field 3
    protected double amount; // Field 4
    protected double settlementAmount; // Field 5
    protected double cardholderBillingAmount; // Field 6
    protected String transactionDateTime; // Field 7
    protected double cardholderBillingFeeAmount; // Field 8
    protected double settlementConversionRate; // Field 9
    protected double cardholderBillingConversionRate; // Field 10
    protected double stan; // Field 11
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
    protected double transactionFeeAmount; // Field 28
    protected double settlementFeeAmount; // Field 29
    protected double transactionProcessingFee; // Field 30
    protected double settlementProcessingFee; // Field 31
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
    protected double creditsProcessingFee; // Field 82
    protected double creditsTransactionFee; // Field 83
    protected double debitsProcessingFee; // Field 84
    protected double debitsTransactionFee; // Field 85
    protected double creditsAmount; // Field 86
    protected double creditsReversalAmount; // Field 87
    protected double debitsAmount; // Field 88
    protected double debitsReversalAmount; // Field 89
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
    protected String issuingInstitutuinIdentCode; // Field 121
    protected String remainingOpenToUse; // Field 122
    protected String addressVerificationResult; // Field 123
    protected String freeFormTextJapan; // Field 124
    protected String supportingInformation;
    protected VisaField126DTO field126; // Field 126
    protected String mac2; // Field 128
    private int customerRiskScore;
    private String responseSourceReasonCode; // Subfield 1
    private String avsResultCode; // Subfield 2
    private String tvcResultCode; // Subfield 3
    private String cardProductType; // Subfield 4
    private String cvvResultCode; // Subfield 5
    private String pacmDiversionLevel; // Subfield 6
    private String pacmDiversionReasonCode; // Subfield 7
    private String cardAuthenticationResultCode; // Subfield 8
    private String laAdditionalResponseData; // Subfield 9
    private String cvv2ResultCode; // Subfield 10
    private String originalResponseCode; // Subfield 11
    private String checkSettlementCode; // Subfield 12
    private String cavvResultCode; // Subfield 13
    private String customerName; // Subfield 1
    private String customerAddress; // Subfield 2
    private String billerAddress; // Subfield 3
    private String billerTelephoneNumber; // Subfield 4
    private String processByDate; // Subfield 5
    private String cardholderCertSerialNumber; // Subfield 6
    private String merchantCertSerialNumber; // Subfield 7
    private String transactionId; // Subfield 8
    private String transStain; // Subfield 9
    private String cvv2RequestData; // Subfield 10
    private String mvv; // Subfield 20
    private String customerName2; // Subfield 21
    private String customerName3; // Subfield 22
    private String productId; // Subfield 23
    private String programIdentifier; // Subfield 24
    private String spendQualifiedIndicator; // Subfield 25
    private String reserved; // Subfield 1 (Not Present)
    private String visaMerchantIdentifier; // Subfield 5
    private String xid; // Subfield 8
    private String cavv; // Subfield 9
    private String cvv2Data; // Subfield 10
    private String serviceIndicator1; // Subfield 12
    private String serviceIndicator2; // Subfield 13
    private String threeDSecureIndicator; // Subfield 20
    private int tranId;
    private String riskLevel;
    private String UUID;
    private String ruleName;
    private List<String> firedRules = new ArrayList<>();
    private boolean ruleFired = false;
    private double riskScore = 0.0;
    private Integer id;
    private boolean flaggedForReview = false;
    private String paymentNetwork;
    private Date timestamp;
    private Double fraudPercentage;
    
    public void setFiredRule(String ruleName) {
        this.firedRules.add(ruleName);
    }
    
    public void setFiredRule(String ruleName, String ruleDescription) {
        this.firedRules.add(ruleName + ": " + ruleDescription);
    }
    
    public void setRuleFired(boolean ruleFired) {
        this.ruleFired = ruleFired;
    }
    
    public boolean isRuleFired() {
        return this.ruleFired;
    }
    
    public double getRiskScore() {
        return this.riskScore;
    }
    
    public void setRiskScore(double riskScore) {
        this.riskScore = riskScore;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public boolean isFlaggedForReview() {
        return this.flaggedForReview;
    }
    
    public void setFlaggedForReview(boolean flaggedForReview) {
        this.flaggedForReview = flaggedForReview;
    }
    
    public String getPaymentNetwork() {
        return this.paymentNetwork;
    }
    
    public void setPaymentNetwork(String paymentNetwork) {
        this.paymentNetwork = paymentNetwork;
    }
    
    public Date getTimestamp() {
        return this.timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}