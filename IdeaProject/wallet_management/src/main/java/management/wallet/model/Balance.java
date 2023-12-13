package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class Balance {
    private int id;
    private BigDecimal amount;
    private LocalDateTime updateDateTime;
}
