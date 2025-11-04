/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.templateSpecification;
import net.com.fms_core.dto.FilterDto;
import net.com.fms_core.entity.FmsElement;
import net.com.fms_core.entity.ReactionTemplate;
import org.springframework.data.jpa.domain.Specification;

public class FieldConfigurationSpecification {
    public static Specification<FmsElement> filterBy(FilterDto filter) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();
            if (filter.getTemplateId() != null && !filter.getTemplateId().isBlank()) {
                predicates = cb.and(predicates, cb.equal(root.get("elementCode"), filter.getTemplateId()));
            }
            if (filter.getTemplateName() != null && !filter.getTemplateName().isBlank()) {
                predicates = cb.and(predicates, cb.like(root.get("elementName"), "%" + filter.getTemplateName() + "%"));
            }
            if (filter.getStatus() != null && !filter.getStatus().isBlank()) {
                predicates = cb.and(predicates, cb.equal(root.get("status"), filter.getStatus()));
            }
            predicates = cb.and(predicates, cb.notEqual(root.get("status"), "DELETED"));
            return predicates;
        };
    }
}
