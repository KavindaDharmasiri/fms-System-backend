/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_auth.dto.common;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import java.util.List;

@Data
@AllArgsConstructor
public class CommonPageResDTO {
    private List<Object> content;     // Current page data
    private int pageNumber;      // Current page number
    private int pageSize;        // Size of the page
    private long totalElements;  // Total number of elements
    private int totalPages;      // Total number of pages
    private boolean last;        // Is it the last page?
    private boolean first;       // Is it the first page?
    private boolean empty;       // Is the content empty?
    public static CommonPageResDTO fromPage(Page<?> page) {
        return new CommonPageResDTO(
                (List<Object>) page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.isFirst(),
                page.isEmpty()
        );
    }
}
