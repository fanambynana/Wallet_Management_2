package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class BalanceHistory {
    private int id;
    private int balanceId;
    private int accountId;
    private LocalDateTime dateTime;
}
