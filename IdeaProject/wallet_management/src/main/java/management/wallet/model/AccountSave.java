package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import management.wallet.model.Enum.AccountName;
import management.wallet.model.Enum.AccountType;

@AllArgsConstructor
@Data
public class AccountSave {
    private int id;
    private AccountName accountName;
    private int balanceId;
    private int currencyId;
    private AccountType accountType;
}
