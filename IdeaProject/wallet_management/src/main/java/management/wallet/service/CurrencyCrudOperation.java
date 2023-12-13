package management.wallet.service;

import management.wallet.DAO.CurrencyDAO;
import management.wallet.model.Currency;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyCrudOperation implements CrudOperation<Currency>{
    CurrencyDAO repository;

    @Override
    public List<Currency> findAll() {
        return repository.findAll();
    }

    @Override
    public Currency findById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<Currency> saveAll(List<Currency> toSave) {
        return repository.saveAll(toSave);
    }

    @Override
    public Currency save(Currency toSave) {
        return repository.save(toSave);
    }

    @Override
    public boolean update(Currency toUpdate) {
        return repository.update(toUpdate);
    }
}
