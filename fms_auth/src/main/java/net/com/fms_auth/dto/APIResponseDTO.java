/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.dto;
import lombok.Data;
@Data
public class APIResponseDTO {
    private boolean success;
    private String message;
    public APIResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
