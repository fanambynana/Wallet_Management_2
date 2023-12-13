package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
public class AccountGet {
    private int id;
    private AccountName accountName;
    private BigDecimal balanceAmount;
    private LocalDateTime balanceUpdateDateTime;
    private List<Transaction> transactions;
    private String currency;
    private AccountType accountType;
}
