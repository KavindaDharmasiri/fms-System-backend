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
    @Column(name = "validate_transaction_id")
    private Integer validateTransactionId;
    @Column(name = "is_valid")
    private Boolean isValid;
    @Column(name = "error_message")
    private String errorMessage;
    @Column(name = "iso_message", columnDefinition = "TEXT")
    private String isoMessage;
    @Column(name = "transaction_time")
    private Timestamp transactionTime;
}
