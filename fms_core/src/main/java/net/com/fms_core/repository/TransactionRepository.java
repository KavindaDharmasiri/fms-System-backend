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
    
    @Query("SELECT t FROM TransactionHistory t WHERE " +
           "CAST(JSON_EXTRACT(t.tranPacket, '$.pan') AS string) LIKE CONCAT(:cardPrefix, '%') " +
           "ORDER BY t.createdAt DESC")
    List<TransactionHistory> findRecentTransactionsByCardNumber(
        @Param("cardPrefix") String cardPrefix, 
        @Param("fromTime") LocalDateTime fromTime,
        @Param("currentTime") LocalDateTime currentTime
    );
    
    default List<TransactionHistory> findRecentTransactionsByCardNumber(String cardPrefix, int hoursBack) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime fromTime = currentTime.minusHours(hoursBack);
        return findRecentTransactionsByCardNumber(cardPrefix, fromTime, currentTime);
    }

    List<TransactionHistory> findAllByOrderByTransactionHistoryIdDesc();
}
