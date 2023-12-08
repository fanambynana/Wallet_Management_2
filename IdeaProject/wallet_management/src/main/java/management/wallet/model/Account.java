package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class Account {
    private int id;
    private AccountName accountName;
    private BigDecimal balanceAmount;
    private LocalDateTime balanceUpdateDateTime;
    private String currency;
    private AccountType accountType;
}
