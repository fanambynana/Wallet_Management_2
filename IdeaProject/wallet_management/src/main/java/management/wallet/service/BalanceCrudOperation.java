package management.wallet.service;

import management.wallet.DAO.AccountDAO;
import management.wallet.DAO.BalanceDAO;
import management.wallet.model.Balance;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BalanceCrudOperation implements CrudOperation<Balance> {
    BalanceDAO balanceDAO;
    AccountDAO accountDAO;

    @Override
    public List<Balance> findAll() {
        return balanceDAO.findAll();
    }

    @Override
    public Balance findById(int id) {
        return balanceDAO.findById(id);
    }

    @Override
    public List<Balance> saveAll(List<Balance> toSave) {
        return balanceDAO.saveAll(toSave);
    }

    @Override
    public Balance save(Balance toSave) {
        return balanceDAO.save(toSave);
    }

    @Override
    public boolean update(Balance toUpdate) {
        return balanceDAO.update(toUpdate);
    }

    public Balance findByDateTime(LocalDateTime dateTime) {
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
