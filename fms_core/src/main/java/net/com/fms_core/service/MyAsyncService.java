/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service;
import net.com.fms_core.dto.RiskManagement.Transaction;
import net.com.fms_core.dto.message.IsoMessageDTO;
import java.util.concurrent.CompletableFuture;

public interface MyAsyncService {
    CompletableFuture<Void> processTask(IsoMessageDTO transaction);
    CompletableFuture<IsoMessageDTO> processTask2(IsoMessageDTO transaction);
}
