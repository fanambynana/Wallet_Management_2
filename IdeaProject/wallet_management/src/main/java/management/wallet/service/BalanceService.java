package management.wallet.service;

import management.wallet.DAO.AccountDAO;
import management.wallet.DAO.BalanceDAO;
import management.wallet.DAO.CurrencyDAO;
import management.wallet.model.AccountSave;
import management.wallet.model.Balance;
import management.wallet.model.Currency;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    public List<Balance> findByInterval(LocalDateTime from, LocalDateTime to) {
        return balanceDAO.findByInterval(from, to);
    }
}
