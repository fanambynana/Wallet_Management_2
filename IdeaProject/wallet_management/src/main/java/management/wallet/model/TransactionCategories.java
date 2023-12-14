package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class TransactionCategories {
    private int id;
    private List<Transaction> transactionList;
}
