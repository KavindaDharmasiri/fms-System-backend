/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateTransactionDTO {
    private Integer validateTransactionId;
    private Boolean isValid;
    private String errorMessage;
    private String isoMessage;
    private Timestamp transactionTime;
    private Timestamp fromTransactionTime;
    private Timestamp toTransactionTime;
}
