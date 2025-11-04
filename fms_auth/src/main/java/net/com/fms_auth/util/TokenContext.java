/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.util;
public class TokenContext {
    private static final ThreadLocal<String> TOKEN = new ThreadLocal<>();
    public static String getToken() {
        return TOKEN.get();
    }
    public static void setToken(String token) {
        TOKEN.set("Bearer "+token);
    }
    public static void clear() {
        TOKEN.remove();
    }
}
