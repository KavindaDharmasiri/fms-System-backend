/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.repository;
import net.com.fms_core.entity.TestTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TestTransactionRepository extends JpaRepository<TestTransaction, Integer> {
}
