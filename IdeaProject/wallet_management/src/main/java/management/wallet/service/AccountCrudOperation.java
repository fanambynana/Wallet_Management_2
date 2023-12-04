package management.wallet.service;

import management.wallet.model.Account;
import management.wallet.model.Transaction;
import management.wallet.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountCrudOperation implements CrudOperation<Account>{
    AccountRepository repository;

    @Override
    public List<Account> findAll() {
        return repository.findAll();
    }

    @Override
    public Account findById(int id) {
        return repository.findById(id);
    }

    public Account findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public List<Account> saveAll(List<Account> toSave) {
        return repository.saveAll(toSave);
    }

    @Override
    public Account save(Account toSave) {
        return repository.save(toSave);
    }
}
