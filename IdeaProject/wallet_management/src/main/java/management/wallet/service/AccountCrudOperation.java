package management.wallet.service;

import management.wallet.model.AccountSave;
import management.wallet.DAO.AccountDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountCrudOperation implements CrudOperation<AccountSave>{
    AccountDAO repository;

    @Override
    public List<AccountSave> findAll() {
        return repository.findAll();
    }

    @Override
    public AccountSave findById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<AccountSave> saveAll(List<AccountSave> toSave) {
        return repository.saveAll(toSave);
    }

    @Override
    public AccountSave save(AccountSave toSave) {
        return repository.save(toSave);
    }

    @Override
    public boolean update(AccountSave toUpdate) {
        return repository.update(toUpdate);
    }
}
