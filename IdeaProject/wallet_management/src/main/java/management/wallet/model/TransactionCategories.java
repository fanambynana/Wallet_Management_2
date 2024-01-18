package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import management.wallet.model.Enum.CategoryGroup;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionCategories {
    private int id;
    private String categoryName;
    private CategoryGroup categoryGroup;
}