/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.repository;
import net.com.fms_core.entity.ReactionTemplate;
import net.com.fms_core.entity.ReactionTemplateRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionTemplateRoleRepository extends JpaRepository<ReactionTemplateRole,Integer> {
    void deleteByReactionTemplateId(ReactionTemplate reactionTemplateId);
}
