package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import management.wallet.model.Enum.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {
    private int id;
    private String label;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private TransactionType transactionType;
    private int transactionCategoryId;
    private int accountId;
}
