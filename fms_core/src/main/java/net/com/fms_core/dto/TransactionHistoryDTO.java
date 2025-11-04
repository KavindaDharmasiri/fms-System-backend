/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.com.fms_core.dto.RiskManagement.Transaction;
import net.com.fms_core.dto.message.IsoMessageDTO;
import java.util.Collection;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryDTO {
    private Integer transactionHistoryId;
    private String tranUuid;
    private IsoMessageDTO tranPacket;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private Collection<TransactionFlaggedRulesDTO> transactionFlaggedRulesCollection;
}
