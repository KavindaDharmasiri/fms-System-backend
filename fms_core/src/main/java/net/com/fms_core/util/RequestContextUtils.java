/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.util;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class RequestContextUtils {
    public static String getApiVersion() {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        return (String) attributes.getAttribute("apiVersion", 0);
    }
    public static String getRequestId() {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        return (String) attributes.getAttribute("requestId", 0);
    }
    public static String getRequestPath() {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        return (String) attributes.getAttribute("path", 0);
    }
}
