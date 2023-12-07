package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class Transaction {
    private int id;
    private String label;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private TransactionsType transactionsType;

    public enum TransactionsType {
        DEBIT,
        CREDIT
    }
}
