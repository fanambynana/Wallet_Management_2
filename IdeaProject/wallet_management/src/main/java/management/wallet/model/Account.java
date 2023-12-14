package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import management.wallet.model.Enum.AccountName;
import management.wallet.model.Enum.AccountType;

import java.util.List;

@AllArgsConstructor
@Data
public class Account {
    private int id;
    private AccountName accountName;
    private Balance balance;
    private List<Transaction> transactions;
    private Currency currency;
    private AccountType accountType;
}
