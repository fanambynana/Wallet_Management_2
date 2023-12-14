package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class TransferHistory {
    private int id;
    private int debitTransactionId;
    private int creditTransactionId;
    private LocalDateTime dateTime;
}
