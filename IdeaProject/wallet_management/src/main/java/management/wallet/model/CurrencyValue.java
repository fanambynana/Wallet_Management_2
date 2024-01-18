package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CurrencyValue {
    private int id;
    private int exchangeSourceId;
    private int exchangeDestinationId;
    private BigDecimal exchangeValue;
    private LocalDateTime exchangeDatetime;
}
