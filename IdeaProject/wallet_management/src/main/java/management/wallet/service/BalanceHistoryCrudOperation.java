package management.wallet.service;

import management.wallet.DAO.BalanceHistoryDAO;
import management.wallet.model.BalanceHistory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BalanceHistoryCrudOperation implements CrudOperation<BalanceHistory> {
    BalanceHistoryDAO repository;

    @Override
    public List<BalanceHistory> findAll() {
        return repository.findAll();
    }

    @Override
    public BalanceHistory findById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<BalanceHistory> saveAll(List<BalanceHistory> toSave) {
        return repository.saveAll(toSave);
    }

    @Override
    public BalanceHistory save(BalanceHistory toSave) {
        return repository.save(toSave);
    }

    @Override
    public boolean update(BalanceHistory toUpdate) {
        return repository.update(toUpdate);
    }
}
