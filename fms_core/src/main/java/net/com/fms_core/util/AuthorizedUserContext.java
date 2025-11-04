/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.util;
public class AuthorizedUserContext {
    private static final ThreadLocal<String> USER = new ThreadLocal<>();
    public static String getUser() {
        if (USER.get() == null){
            return "admin";
        }else {
            return USER.get();
        }
    }
    public static void setUser(String user) {
        USER.set(user);
    }
    public static void clear() {
        USER.remove();
    }
}
