package net.com.fms_core.repository;

import net.com.fms_core.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionHistory, Long> {
    
    @Query(value = "SELECT * FROM transaction_history t WHERE " +
           "SUBSTRING(JSON_UNQUOTE(JSON_EXTRACT(t.tran_packet, '$.pan')), 1, 12) = :cardPrefix " +
           "AND t.created_at >= :fromTime " +
           "ORDER BY t.created_at DESC LIMIT 10", nativeQuery = true)
    List<TransactionHistory> findRecentByCard(
        @Param("cardPrefix") String cardPrefix,
        @Param("fromTime") LocalDateTime fromTime
    );
    
    default List<TransactionHistory> findRecentTransactionsByCardNumber(String cardPrefix, int hoursBack) {
        LocalDateTime fromTime = LocalDateTime.now().minusHours(hoursBack);
        return findRecentByCard(cardPrefix, fromTime);
    }

    List<TransactionHistory> findAllByOrderByTransactionHistoryIdDesc();
    TransactionHistory findByTranUuid(String tranUuid);
    List<TransactionHistory> findByCreatedAtBetween(Date startDate, Date endDate);
    
    // Additional methods for dashboard analytics
    long countByCreatedAtBetween(Date startDate, Date endDate);
    long countByStatusAndCreatedAtBetween(String status, Date startDate, Date endDate);
    
    @Query("SELECT COUNT(t) FROM TransactionHistory t WHERE t.status = 'FLAGGED' AND t.createdAt BETWEEN :startDate AND :endDate")
    long countFlaggedTransactionsBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT COUNT(t) FROM TransactionHistory t WHERE t.actionStatus = 'BLOCKED' AND t.createdAt BETWEEN :startDate AND :endDate")
    long countBlockedTransactionsBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT t FROM TransactionHistory t WHERE (t.status = 'FLAGGED' OR t.actionStatus = 'BLOCKED') AND t.createdAt BETWEEN :startDate AND :endDate")
    List<TransactionHistory> findFraudTransactionsBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
