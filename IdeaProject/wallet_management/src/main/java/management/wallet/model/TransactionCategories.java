package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import management.wallet.model.Enum.SpecificCategories;


@AllArgsConstructor
@Data
public class TransactionCategories {
    private int id;
    private String category_name;
    private SpecificCategories specific_Categories;
}
