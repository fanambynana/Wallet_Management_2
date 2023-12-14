package management.wallet.service;

import management.wallet.DAO.CurrencyValueDAO;
import management.wallet.model.CurrencyValue;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyValueCrudOperation implements CrudOperation<CurrencyValue> {
    CurrencyValueDAO currencyValueDAO;

    @Override
    public List<CurrencyValue> findAll() {
        return null;
    }

    @Override
    public CurrencyValue findById(int id) {
        return currencyValueDAO.findById(id);
    }

    @Override
    public List<CurrencyValue> saveAll(List<CurrencyValue> toSave) {
        return null;
    }

    @Override
    public CurrencyValue save(CurrencyValue toSave) {
        return null;
    }

    @Override
    public boolean update(CurrencyValue toUpdate) {
        return false;
    }
}
