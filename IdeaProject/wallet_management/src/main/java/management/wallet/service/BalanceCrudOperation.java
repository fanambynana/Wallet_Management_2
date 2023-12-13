package management.wallet.service;

import management.wallet.DAO.BalanceDAO;
import management.wallet.model.Balance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BalanceCrudOperation implements CrudOperation<Balance> {
    BalanceDAO repository;

    @Override
    public List<Balance> findAll() {
        return repository.findAll();
    }

    @Override
    public Balance findById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<Balance> saveAll(List<Balance> toSave) {
        return repository.saveAll(toSave);
    }

    @Override
    public Balance save(Balance toSave) {
        return repository.save(toSave);
    }

    @Override
    public boolean update(Balance toUpdate) {
        return repository.update(toUpdate);
    }
}
