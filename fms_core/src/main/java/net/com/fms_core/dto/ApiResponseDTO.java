/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto;
import lombok.*;
import net.com.fms_core.util.RequestContextUtils;
import org.springframework.data.domain.Page;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;
    private List<ErrorDetailDTO> errors;
    private Map<String, Object> metadata;
    public static <T> ApiResponseDTO<T> success(T data) {
        RequestContext requestContext = getRequestContext();
        ApiResponseDTO<T> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setData(data);
        response.setErrors(Collections.emptyList());
        response.setMetadata(Map.of(
                "timestamp", Instant.now().toString(),
                "version", requestContext.apiVersion(),
                "requestId", requestContext.requestId(),
                "path", requestContext.path()
        ));
        return response;
    }
    public static <T,U> ApiResponseDTO<T> success(Page<U> pageData) {
        RequestContext requestContext = getRequestContext();
        @SuppressWarnings("unchecked")
        T listData = (T) pageData.getContent();
        ApiResponseDTO<T> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setData(listData);
        response.setErrors(Collections.emptyList());
        response.setMetadata(Map.of(
                "pagination", Map.of(
                        "totalElements", pageData.getTotalElements(),
                        "totalPages",pageData.getTotalPages(),
                        "pageNumber",pageData.getNumber(),
                        "pageSize",pageData.getSize(),
                        "isLast",pageData.isLast(),
                        "isFirst",pageData.isFirst(),
                        "isEmpty",pageData.isEmpty()
                ),
                "timestamp", Instant.now().toString(),
                "version", requestContext.apiVersion(),
                "requestId", requestContext.requestId(),
                "path", requestContext.path()
        ));
        return response;
    }
    public static <T> ApiResponseDTO<T> error(ErrorDetailDTO error) {
        RequestContext requestContext = getRequestContext();
        ApiResponseDTO<T> response = new ApiResponseDTO<>();
        response.setSuccess(false);
        response.setData(null);
        response.setErrors(Collections.singletonList(error));
        response.setMetadata(Map.of(
                "timestamp", Instant.now().toString(),
                "version", requestContext.apiVersion(),
                "requestId", requestContext.requestId(),
                "path", requestContext.path()
        ));
        return response;
    }
    public static <T> ApiResponseDTO<T> error(List<ErrorDetailDTO> errors) {
        RequestContext requestContext = getRequestContext();
        ApiResponseDTO<T> response = new ApiResponseDTO<>();
        response.setSuccess(false);
        response.setData(null);
        response.setErrors(errors);
        response.setMetadata(Map.of(
                "timestamp", Instant.now().toString(),
                "version", requestContext.apiVersion(),
                "requestId", requestContext.requestId(),
                "path", requestContext.path()
        ));
        return response;
    }
    private static RequestContext getRequestContext() {
        String apiVersion = RequestContextUtils.getApiVersion();
        String requestId = RequestContextUtils.getRequestId();
        String path = RequestContextUtils.getRequestPath();
        RequestContext requestContext = new RequestContext(apiVersion, requestId, path);
        return requestContext;
    }
    private record RequestContext(String apiVersion, String requestId, String path) {
    }
}
