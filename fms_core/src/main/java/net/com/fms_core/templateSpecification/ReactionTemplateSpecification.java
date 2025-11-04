/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.templateSpecification;
import net.com.fms_core.dto.FilterDto;
import net.com.fms_core.entity.ReactionTemplate;
import org.springframework.data.jpa.domain.Specification;

public class ReactionTemplateSpecification {
    public static Specification<ReactionTemplate> filterBy(FilterDto filter) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();
            if (filter.getTemplateId() != null && !filter.getTemplateId().isBlank()) {
                predicates = cb.and(predicates, cb.equal(root.get("templateUuid"), filter.getTemplateId()));
            }
            if (filter.getTemplateName() != null && !filter.getTemplateName().isBlank()) {
                predicates = cb.and(predicates, cb.like(root.get("templateName"), "%" + filter.getTemplateName() + "%"));
            }
            if (filter.getSubject() != null && !filter.getSubject().isBlank()) {
                predicates = cb.and(predicates, cb.like(root.get("subject"), "%" + filter.getSubject() + "%"));
            }
            if (filter.getStatus() != null && !filter.getStatus().isBlank()) {
                predicates = cb.and(predicates, cb.equal(root.get("status"), filter.getStatus()));
            }
            predicates = cb.and(predicates, cb.notEqual(root.get("status"), "DELETED"));
            return predicates;
        };
    }
}
