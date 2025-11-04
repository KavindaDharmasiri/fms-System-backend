/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.repository;
import net.com.fms_core.entity.ValidateTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ValidateTransactionRepository  extends JpaRepository<ValidateTransaction, Integer> {
    @Query("""
    SELECT vt FROM ValidateTransaction vt
    WHERE (:validateTransactionId IS NULL OR vt.validateTransactionId = :validateTransactionId)
      AND (:isValid IS NULL OR vt.isValid = :isValid)
      AND (:errorMessage IS NULL OR vt.errorMessage LIKE %:errorMessage%)
      AND (:isoMessage IS NULL OR vt.isoMessage LIKE %:isoMessage%)
      AND (:startTime IS NULL OR vt.transactionTime >= :startTime)
      AND (:endTime IS NULL OR vt.transactionTime <= :endTime)
""")
    Page<ValidateTransaction> filterTransactions(
            @Param("validateTransactionId") Integer validateTransactionId,
            @Param("isValid") Boolean isValid,
            @Param("errorMessage") String errorMessage,
            @Param("isoMessage") String isoMessage,
            @Param("startTime") Timestamp startTime,
            @Param("endTime") Timestamp endTime,
            Pageable pageable
    );
}
