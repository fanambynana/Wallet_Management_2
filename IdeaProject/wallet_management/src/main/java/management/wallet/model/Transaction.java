package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
public class Transaction {
    private int id;
    private String description;
    private double amount;
    private String type;
    private String correspondent;
}
