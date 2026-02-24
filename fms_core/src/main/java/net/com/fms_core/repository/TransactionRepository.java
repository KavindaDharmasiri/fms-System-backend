package net.com.fms_core.repository;

import net.com.fms_core.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
}
