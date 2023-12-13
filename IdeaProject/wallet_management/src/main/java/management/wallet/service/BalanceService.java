package management.wallet.service;

import management.wallet.DAO.BalanceDAO;
import management.wallet.model.Balance;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BalanceService {
    BalanceDAO balanceDAO;

    public Balance findBalanceByDateTime(LocalDateTime dateTime) {
        return balanceDAO.findByDateTime(dateTime);
    }
}
