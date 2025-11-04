/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.util;
import net.com.fms_core.dto.ApiPageReqDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ApiCommonMethod {
    public static Pageable pageableFrom(ApiPageReqDTO pageable, String defaultSortBy) {
        return PageRequest.of(
                pageable.getPage(),
                pageable.getSize() > 0 ? pageable.getSize() : 1,
                Sort.by(pageable.getOrderBy() != null ? Sort.Direction.DESC : Sort.Direction.ASC, pageable.getSortBy() != null ? pageable.getSortBy() : defaultSortBy));
    }
}
