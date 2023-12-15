package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AmountPerCategory {
    String categoryName;
    BigDecimal amount;
}
