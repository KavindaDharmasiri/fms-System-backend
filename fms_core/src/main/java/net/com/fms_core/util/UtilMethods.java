/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.util;
import org.jpos.iso.ISOUtil;
import java.util.Date;
import java.util.UUID;

public class UtilMethods {
    private static int N = 0;
    public static void debug(String sessionId, int logType, String threadNo, String msg, int channelType) {
        try {
            String logName = "        ";
            String chType = "     ";
            if (logType == SysConfigValues.LOG_TYPE_INIT) {
                logName = "Init     ";
            } else if (logType == SysConfigValues.LOG_TYPE_INFOR) {
                logName = "Infor    ";
            } else if (logType == SysConfigValues.LOG_TYPE_ALERT) {
                logName = "Alert    ";
            } else if (logType == SysConfigValues.LOG_TYPE_TIMEOUT) {
                logName = "Timeout  ";
            } else if (logType == SysConfigValues.LOG_TYPE_SECURITY) {
                logName = "Security ";
            } else if (logType == SysConfigValues.LOG_TYPE_RAWDATA) {
                logName = "RawData  ";
            } else if (logType == SysConfigValues.LOG_TYPE_OPERATION) {
                logName = "Operation";
            }
            if (sessionId == null) {
                sessionId = "00000000000000000000000000000000";
            }
            if (threadNo == null) {
                threadNo = "0";
            }
            if (channelType == SysConfigValues.CHANNEL_TYPE_VISA) {
                chType = "VISA ";
            } else if (channelType == SysConfigValues.CHANNEL_TYPE_MASTER) {
                chType = "MASTER ";
            } else if (channelType == SysConfigValues.CHANNEL_TYPE_CUP) {
                chType = "CUP ";
            }
            msg = sessionId + ":" + logName + ":" + chType + ":[Thread." + ISOUtil.zeropad(threadNo, 4) + "]:" + msg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(msg);
    }
    public static String getTimestamp() throws Exception {
        return new java.sql.Timestamp(new Date().getTime()).toString();
    }
    public static String getTransactionId() throws Exception {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }
    public static StringBuffer maskCardNumber(StringBuffer cardNo) throws Exception {
        StringBuffer temp = new StringBuffer();
        if (cardNo != null && cardNo.length() >= 16) {
            temp.append(cardNo.substring(0, 6));
            temp.append("**");
            temp.append(cardNo.substring(12));
        } else {
            temp = cardNo;
        }
        return temp;
    }
}
