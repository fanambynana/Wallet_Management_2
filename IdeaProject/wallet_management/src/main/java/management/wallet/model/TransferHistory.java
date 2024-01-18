package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransferHistory {
    private int id;
    private int debitTransactionId;
    private int creditTransactionId;
    private LocalDateTime datetime;
}
