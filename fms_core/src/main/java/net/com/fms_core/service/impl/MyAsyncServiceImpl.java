/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.service.impl;
import net.com.fms_core.dto.RiskManagement.Transaction;
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.entity.TransactionHistory;
import net.com.fms_core.repository.TransactionRepository;
import net.com.fms_core.service.DynamicDroolsService;
import net.com.fms_core.service.MyAsyncService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service("myAsyncService")
public class MyAsyncServiceImpl implements MyAsyncService {
    private final DynamicDroolsService droolsService;
    private final Executor taskExecutor;
    private final TransactionRepository transactionHistoryRepository;
    public MyAsyncServiceImpl(@Qualifier("applicationTaskExecutor") Executor taskExecutor, DynamicDroolsService droolsService, TransactionRepository transactionHistoryRepository) {
        this.taskExecutor = taskExecutor;
        this.droolsService = droolsService;
        this.transactionHistoryRepository = transactionHistoryRepository;
    }
    @Override
    public CompletableFuture<Void> processTask(IsoMessageDTO txn) {
        try {
            return CompletableFuture.runAsync(() -> {
                try {
                    long startTime = System.currentTimeMillis();
                    IsoMessageDTO evaluatedTxn = droolsService.evaluateTransaction(txn);
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    System.out.println("Execution time: TRAN " + txn.getTranId() + " / " + duration + " milliseconds");
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }, taskExecutor);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public CompletableFuture<IsoMessageDTO> processTask2(IsoMessageDTO txn) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                long startTime = System.currentTimeMillis();
                IsoMessageDTO evaluatedTxn = droolsService.evaluateTransaction2(txn);
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                System.out.println("Execution time: TRAN " + evaluatedTxn.getTranId() + " / " + duration + " milliseconds");
                return evaluatedTxn; // ✅ return the updated transaction
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, taskExecutor);
    }
}
