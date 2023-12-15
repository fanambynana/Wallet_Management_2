package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import management.wallet.model.Enum.CategoryGroup;


@AllArgsConstructor
@Data
public class TransactionCategories {
    private int id;
    private String categoryName;
    private CategoryGroup categoryGroup;
}
