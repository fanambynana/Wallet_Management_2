package gestion.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Transaction {
    private int id;
    private String description;
    private double amount;
    private String type;
    private String correspondent;
}
