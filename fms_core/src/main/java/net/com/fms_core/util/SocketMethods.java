/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.util;
import net.com.fms_core.dto.VisaPacketDTO;
import net.com.fms_core.dto.message.*;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class SocketMethods {
    public String XML_PATH = "C:\\Users\\kavinda_d\\Documents\\projects\\fms backend\\fms_core\\src\\main\\resources\\isoXML\\visapack.xml";
    public IsoMessageDTO mapToDto(ISOMsg isoMsg) throws Exception {
        IsoMessageDTO dto = new IsoMessageDTO();
        dto.setMessageTypeIndicator(Integer.parseInt(isoMsg.getString(0)));
        dto.setBitMap(isoMsg.getString(1));
        dto.setPan(isoMsg.hasField(2) ? maskField(2, isoMsg.getString(2)) : null);
        dto.setProcessingCode(Integer.parseInt(isoMsg.getString(3)));
        dto.setAmount(Double.parseDouble(isoMsg.getString(4)));
        dto.setSettlementAmount(safeParseDouble(isoMsg.getString(5)));
        dto.setCardholderBillingAmount(safeParseDouble(isoMsg.getString(6)));
        dto.setTransactionDateTime(isoMsg.getString(7));
        dto.setCardholderBillingFeeAmount(safeParseDouble(isoMsg.getString(8)));
        dto.setSettlementConversionRate(safeParseDouble(isoMsg.getString(9)));
        dto.setCardholderBillingConversionRate(safeParseDouble(isoMsg.getString(10)));
        dto.setStan(safeParseDouble(isoMsg.getString(11)));
        dto.setLocalTransactionTime(isoMsg.getString(12));
        dto.setLocalTransactionDate(isoMsg.getString(13));
        dto.setExpiryDate(isoMsg.getString(14));
        dto.setSettlementDate(isoMsg.getString(15));
        dto.setConversionDate(isoMsg.getString(16));
        dto.setCaptureDate(isoMsg.getString(17));
        dto.setMerchantCategoryCode(isoMsg.getString(18));
        dto.setAcquiringCountryCode(isoMsg.getString(19));
        dto.setPanExtendedCountryCode(isoMsg.getString(20));
        dto.setForwardingCountryCode(isoMsg.getString(21));
        dto.setPosEntryMode(isoMsg.getString(22));
        dto.setCardSequenceNumber(isoMsg.getString(23));
        dto.setNetworkId(isoMsg.getString(24));
        dto.setPosConditionCode(isoMsg.getString(25));
        dto.setPosPinCaptureCode(isoMsg.getString(26));
        dto.setAuthResponseLength(isoMsg.getString(27));
        dto.setTransactionFeeAmount(safeParseDouble(isoMsg.getString(28)));
        dto.setSettlementFeeAmount(safeParseDouble(isoMsg.getString(29)));
        dto.setTransactionProcessingFee(safeParseDouble(isoMsg.getString(30)));
        dto.setSettlementProcessingFee(safeParseDouble(isoMsg.getString(31)));
        dto.setAcquirerInstitutionId(isoMsg.getString(32));
        dto.setForwardingInstitutionId(isoMsg.getString(33));
        dto.setPanExtended(isoMsg.getString(34));
        dto.setTrack2Data(isoMsg.hasField(35) ? maskField(35, isoMsg.getString(35)) : null);
        dto.setTrack3Data(isoMsg.getString(36));
        dto.setRetrievalReferenceNumber(isoMsg.getString(37));
        dto.setAuthResponse(isoMsg.getString(38));
        dto.setResponseCode(isoMsg.getString(39));
        dto.setServiceRestrictionCode(isoMsg.getString(40));
        dto.setTerminalId(isoMsg.getString(41));
        dto.setCardAcceptorId(isoMsg.getString(42));
        dto.setCardAcceptorNameLocation(isoMsg.getString(43));
        dto.setTrack1Data(isoMsg.hasField(45) ? maskField(45, isoMsg.getString(45)) : null);
        dto.setAdditionalResponseData(mapField44(isoMsg,dto));
        dto.setField62(mapField62(isoMsg,dto));
        dto.setField95(mapField95(isoMsg,dto));
        dto.setField126(mapField126(isoMsg,dto));
        return dto;
    }
    public static Double safeParseDouble(String value) {
        try {
            return (value != null && !value.isBlank()) ? Double.parseDouble(value) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    private VisaField44DTO mapField44(ISOMsg isoMsg, IsoMessageDTO dto1) throws Exception {
        VisaField44DTO dto = new VisaField44DTO();
        for (int i = 1; i <= 13; i++) {
            if (isoMsg.hasField("44." + i)) {
                String value = isoMsg.getString("44." + i);
                switch(i) {
                    case 1: dto.setResponseSourceReasonCode(value); dto1.setResponseSourceReasonCode(value); break;
                    case 2: dto.setAvsResultCode(value);dto1.setAvsResultCode(value); break;
                    case 3: dto.setTvcResultCode(value);dto1.setTvcResultCode(value); break;
                    case 4: dto.setCardProductType(value);dto1.setCardProductType(value); break;
                    case 5: dto.setCvvResultCode(maskField(44, 5, value));dto1.setCvvResultCode(maskField(44, 5, value)); break;
                    case 6: dto.setPacmDiversionLevel(value);dto1.setPacmDiversionLevel(value); break;
                    case 7: dto.setPacmDiversionReasonCode(value);dto1.setPacmDiversionReasonCode(value); break;
                    case 8: dto.setCardAuthenticationResultCode(value);dto1.setCardAuthenticationResultCode(value); break;
                    case 9: dto.setLaAdditionalResponseData(value);dto1.setLaAdditionalResponseData(value); break;
                    case 10: dto.setCvv2ResultCode(maskField(44, 10, value));dto1.setCvv2ResultCode(maskField(44, 10, value)); break;
                    case 11: dto.setOriginalResponseCode(value);dto1.setOriginalResponseCode(value); break;
                    case 12: dto.setCheckSettlementCode(value);dto1.setCheckSettlementCode(value); break;
                    case 13: dto.setCavvResultCode(value);dto1.setCavvResultCode(value); break;
                }
            }
        }
        return dto;
    }
    private VisaField62DTO mapField62(ISOMsg isoMsg, IsoMessageDTO dto1) throws Exception {
        VisaField62DTO dto = new VisaField62DTO();
        for (int i = 1; i <= 26; i++) {
            if (isoMsg.hasField("62." + i)) {
                String value = isoMsg.getString("62." + i);
                switch(i) {
                    case 1: dto.setCustomerName(value);dto1.setCustomerName(value); break;
                    case 2: dto.setCustomerAddress(value);dto1.setCustomerAddress(value); break;
                    case 3: dto.setBillerAddress(value);dto1.setBillerAddress(value); break;
                    case 4: dto.setBillerTelephoneNumber(value);dto1.setBillerTelephoneNumber(value); break;
                    case 5: dto.setProcessByDate(value);dto1.setProcessByDate(value); break;
                    case 6: dto.setCardholderCertSerialNumber(value);dto1.setCardholderCertSerialNumber(value); break;
                    case 7: dto.setMerchantCertSerialNumber(ISOUtil.hexString(isoMsg.getBytes("62.7")));dto1.setMerchantCertSerialNumber(ISOUtil.hexString(isoMsg.getBytes("62.7"))); break;
                    case 8: dto.setTransactionId(value);dto1.setTransactionId(value); break;
                    case 9: dto.setTransStain(value);dto1.setTransStain(value); break;
                    case 10: dto.setCvv2RequestData(maskField(62, 10, value));dto1.setCvv2RequestData(maskField(62, 10, value)); break;
                    case 20: dto.setMvv(value);dto1.setMvv(value); break;
                    case 21: dto.setCustomerName2(value);dto1.setCustomerName2(value); break;
                    case 22: dto.setCustomerName3(value);dto1.setCustomerName3(value); break;
                    case 23: dto.setProductId(value);dto1.setProductId(value); break;
                    case 24: dto.setProgramIdentifier(value);dto1.setProgramIdentifier(value); break;
                    case 25: dto.setSpendQualifiedIndicator(value);dto1.setSpendQualifiedIndicator(value); break;
                }
            }
        }
        return dto;
    }
    private VisaField95DTO mapField95(ISOMsg isoMsg, IsoMessageDTO dto1) {
        VisaField95DTO dto = new VisaField95DTO();
        for (int i = 1; i <= 2; i++) {
            if (isoMsg.hasField("95." + i)) {
                String value = isoMsg.getString("95." + i);
                switch(i) {
                    case 1: dto.setReserved(value); dto1.setReserved(value); break;
                    case 2: dto.setResponseSourceReasonCode(value);dto1.setResponseSourceReasonCode(value); break;
                }
            }
        }
        return dto;
    }
    private VisaField126DTO mapField126(ISOMsg isoMsg, IsoMessageDTO dto1) throws Exception {
        VisaField126DTO dto = new VisaField126DTO();
        for (int i = 1; i <= 30; i++) {
            if (isoMsg.hasField("126." + i)) {
                String value = isoMsg.getString("126." + i);
                switch(i) {
                    case 5: dto.setVisaMerchantIdentifier(value); dto1.setVisaMerchantIdentifier(value); break;
                    case 8: dto.setXid(value); dto1.setXid(value); break;
                    case 9: dto.setCavv(value);dto1.setCavv(value); break;
                    case 10: dto.setCvv2Data(maskField(126, 10, value));dto1.setCvv2Data(maskField(126, 10, value)); break;
                    case 12: dto.setServiceIndicator1(value);dto1.setServiceIndicator1(value); break;
                    case 13: dto.setServiceIndicator2(value);dto1.setServiceIndicator2(value); break;
                    case 20: dto.setThreeDSecureIndicator(value);dto1.setThreeDSecureIndicator(value); break;
                }
            }
        }
        return dto;
    }
    private String maskField(int fieldNumber, String value) throws Exception {
        return maskField(fieldNumber, -1, value);
    }
    private String maskField(int fieldNumber, int subFieldNumber, String value) throws Exception {
        if (value == null) return null; // Add null check [[1]][[6]]
        if (!SysConfigValues.MASK_SENSITIVE_DATA) return value;
        if ((fieldNumber == 2) ||
                (fieldNumber == 35) ||
                (fieldNumber == 45) ||
                (fieldNumber == 44 && subFieldNumber == 5) ||
                (fieldNumber == 44 && subFieldNumber == 10) ||
                (fieldNumber == 62 && subFieldNumber == 10) ||
                (fieldNumber == 126 && subFieldNumber == 10)) {
            return UtilMethods.maskCardNumber(new StringBuffer(value)).toString();
        }
        return value;
    }
    public  void printISOMessage(ISOMsg m, String msg) throws Exception {
        Map<Integer, VisaPacketDTO> fieldMap = new HashMap<>();
        StringBuffer buf = new StringBuffer();
        buf.append("\n|_________|\n");
        buf.append(msg + "\n");
        for (int i = 0; i <= 128; i++) {
            if (m.hasField(i)) {
                if (i == 2) {
                    if (SysConfigValues.MASK_SENSITIVE_DATA) {
                        buf.append("Field [" + i + "]"
                                + UtilMethods.maskCardNumber(new StringBuffer(m.getString(i))) + "\n");
                    } else {
                        buf.append("Field [" + i + "]" + m.getString(i) + "\n");
                    }
                } else if (i == 35 || i == 45 || i == 55) {
                    if (SysConfigValues.MASK_SENSITIVE_DATA) {
                        buf.append("Field [" + i + "]" + "*********" + "\n");
                    } else {
                        buf.append("Field [" + i + "]" + m.getString(i) + "\n");
                    }
                } else if (i == 56) {
                    if (SysConfigValues.MASK_SENSITIVE_DATA) {
                        if (m.getString(i).indexOf("#|") >= 0) {
                            String newString = m.getString(i).substring(0, m.getString(i).indexOf("#|") - 3) + "*"
                                    + m.getString(i).substring(m.getString(i).indexOf("#|"));
                            buf.append("Field [" + i + "]" + newString + "\n");
                        } else
                            buf.append("Field [" + i + "]" + m.getString(i) + "\n");
                    } else {
                        buf.append("Field [" + i + "]" + m.getString(i) + "\n");
                    }
                } else if (i == 44) {
                    for (int j = 1; j <= 13; j++) {
                        if (m.hasField("44." + j)) {
                            buf.append("Field [44." + j + "]" + m.getString("44." + j) + "\n");
                        }
                    }
                } else if (i == 62) {
                    for (int j = 1; j <= 26; j++) {
                        if (j == 7) {
                            if (m.hasField("62.7")) {
                                buf.append("Field [62.7]" + ISOUtil.hexString(m.getBytes("62.7")) + "\n");
                            }
                        } else if (m.hasField("62." + j)) {
                            buf.append("Field [62." + j + "]" + m.getString("62." + j) + "\n");
                        }
                    }
                } else if (i == 95) {
                    for (int j = 1; j <= 2; j++) {
                        if (m.hasField("95." + j)) {
                            buf.append("Field [95." + j + "]" + m.getString("95." + j) + "\n");
                        }
                    }
                } else if (i == 104) {
                    buf.append("Field [" + i + "]" + ISOUtil.hexString(m.getBytes(i)) + "\n");
                } else if (i == 126) {
                    for (int j = 1; j <= 30; j++) {
                        if (m.hasField("126." + j)) {
                            if (j == 10 && SysConfigValues.MASK_SENSITIVE_DATA) { // Mask cvv2
                                buf.append("Field [126." + j + "]" + "*" + "\n");
                            } else {
                                buf.append("Field [126." + j + "]" + m.getString("126." + j) + "\n");
                            }
                        }
                    }
                } else {
                    buf.append("Field [" + i + "]" + m.getString(i) + "\n");
                }
                String fieldValue = m.getString(i);
                String fieldName = "";
                if (m.getPackager() != null && m.getPackager() instanceof GenericPackager) {
                    GenericPackager genericPackager = (GenericPackager) m.getPackager();
                    if (genericPackager.getFieldPackager(i) != null) {
                        fieldName = genericPackager.getFieldPackager(i).getDescription();
                    }
                }
                if (fieldName == null || fieldName.isEmpty()) {
                    fieldName = "Unknown";
                }
                buf.append(String.format("Field [%03d] (%s): %s\n", i, fieldName, fieldValue));
                fieldMap.put(i, VisaPacketDTO.builder().fileId(i).fieldName(fieldName).fieldValue(fieldValue).build());
            }
        }
        buf.append("|_________|");
        buf.append("\n");
        System.out.println(buf.toString());
    }
}
