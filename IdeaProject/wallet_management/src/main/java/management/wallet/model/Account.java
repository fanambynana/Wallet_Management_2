package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Account {
    private int id;
    private String username;
    private String currency;
    private double balance;
}
