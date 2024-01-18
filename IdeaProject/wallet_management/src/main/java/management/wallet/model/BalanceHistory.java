package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BalanceHistory {
    private int id;
    private int balanceId;
    private int accountId;
    private LocalDateTime datetime;
}
