package management.wallet.service;

import management.wallet.DAO.AccountDAO;
import management.wallet.DAO.BalanceDAO;
import management.wallet.model.Balance;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BalanceService {
    BalanceDAO balanceDAO;
    AccountDAO accountDAO;

    public Balance findBalanceByDateTime(LocalDateTime dateTime) {
        return balanceDAO.findByDateTime(dateTime);
    }
    public Balance findCurrentBalance(int accountId) {
        return balanceDAO.findById(
                accountDAO.findById(accountId).getBalanceId()
        );
    }
}
