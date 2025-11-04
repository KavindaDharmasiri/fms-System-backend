/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.repository;
import net.com.fms_auth.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege,Integer> {
    @Query("SELECT DISTINCT p.page FROM Privilege p WHERE p.page IS NOT NULL ORDER BY p.page ASC")
    List<String> findDistinctPages();
    @Query("SELECT DISTINCT p.section FROM Privilege p WHERE p.section IS NOT NULL ORDER BY p.section ASC")
    List<String> findDistinctSections();
    @Query("SELECT DISTINCT p.privilegeName FROM Privilege p WHERE p.privilegeName IS NOT NULL ORDER BY p.privilegeName ASC")
    List<String> findDistinctTasks();
}
