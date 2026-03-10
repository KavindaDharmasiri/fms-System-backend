/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.repository;
import net.com.fms_core.entity.FmsElement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FmsElementRepository extends JpaRepository<FmsElement,Integer>, JpaSpecificationExecutor<FmsElement> {
     List<FmsElement> findAllByStatus(String active);
     Page<FmsElement> findAllByStatusNot(String active, Pageable pageable);
    boolean existsByElementName(String elementName);
    boolean existsByElementNameAndFmsElementIdNot(String elementName, Integer fmsElementId);
}
