package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AccountSave {
    private int id;
    private AccountName accountName;
    private int balanceId;
    private int currencyId;
    private AccountType accountType;
}
