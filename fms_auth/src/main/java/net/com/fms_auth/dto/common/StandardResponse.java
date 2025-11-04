/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.dto.common;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StandardResponse {
    private int code;
    private boolean success;
    private String message;
    private Object data;
    public StandardResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    public StandardResponse(int code, boolean success, String message) {
        this.code = code;
        this.success = success;
        this.message = message;
    }
    public StandardResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
