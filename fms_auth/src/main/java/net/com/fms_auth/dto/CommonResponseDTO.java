/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommonResponseDTO {
    private boolean success;
    private String message;
    private Object data;
    public CommonResponseDTO(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}
