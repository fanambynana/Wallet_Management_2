package gestion.wallet.service;

import gestion.wallet.model.Account;
import gestion.wallet.repository.AccountRepository;
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
    public List<Account> saveAll(List<Account> toSave) {
        return repository.saveAll(toSave);
    }

    @Override
    public Account save(Account toSave) {
        return repository.save(toSave);
    }
}
