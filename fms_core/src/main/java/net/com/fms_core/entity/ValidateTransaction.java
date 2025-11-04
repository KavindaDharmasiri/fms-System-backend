/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Entity
@Table(name = "validate_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer validateTransactionId;
    private Boolean isValid;
    private String errorMessage;
    private String isoMessage;
    private Timestamp transactionTime;
}
