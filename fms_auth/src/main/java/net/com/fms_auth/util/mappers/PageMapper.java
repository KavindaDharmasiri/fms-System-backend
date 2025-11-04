/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.util.mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
public class PageMapper {
    public static <T, R> Page<R> mapPage(Page<T> sourcePage, List<R> mappedContent, Pageable pageable) {
        return new PageImpl<>(mappedContent, pageable, sourcePage.getTotalElements());
    }
}
