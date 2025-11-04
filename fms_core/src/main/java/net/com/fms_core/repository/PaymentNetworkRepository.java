/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.repository;
import net.com.fms_core.entity.PaymentNetwork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentNetworkRepository extends JpaRepository<PaymentNetwork,Integer> {
    List<PaymentNetwork> findAllByStatus(String status);
    @Query(value = "SELECT pn FROM PaymentNetwork pn " +
            "WHERE (:paymentNetworkID IS NULL OR pn.paymentNetworkId = :paymentNetworkID) " +
            "AND (:paymentNetworkName IS NULL OR LOWER(pn.networkName) LIKE LOWER(CONCAT('%', :paymentNetworkName, '%'))) " +
            "AND (:bin IS NULL OR pn.bin = :bin) " +
            "AND (:binLength IS NULL OR pn.binLength = :binLength) " +
            "AND (:status IS NULL OR pn.status = :status) " +
            "ORDER BY pn.createdAt DESC ")
    Page<PaymentNetwork> listPaymentNetworks(
            @Param("paymentNetworkID") Integer paymentNetworkID,
            @Param("paymentNetworkName") String paymentNetworkName,
            @Param("bin") Double bin,
            @Param("binLength") Double binLength,
            @Param("status") String status,
            Pageable pageable);
}
