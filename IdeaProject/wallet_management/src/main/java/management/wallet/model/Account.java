package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import management.wallet.model.Enum.AccountName;
import management.wallet.model.Enum.AccountType;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {
    private int id;
    private AccountName accountName;
    private int balanceId;
    private int currencyId;
    private AccountType accountType;
}
