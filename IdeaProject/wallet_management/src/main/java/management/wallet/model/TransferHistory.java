package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class TransferHistory {
    private int id;
    private int debit_transaction_id;
    private int credit_transaction_id;
    private LocalDateTime dateTime;
}
