package management.wallet.service;

import management.wallet.model.Transaction;
import management.wallet.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionCrudOperation implements CrudOperation<Transaction>{
    TransactionRepository repository;

    @Override
    public List<Transaction> findAll() {
        return repository.findAll();
    }

    @Override
    public Transaction findById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<Transaction> saveAll(List<Transaction> toSave) {
        return repository.saveAll(toSave);
    }

    @Override
    public Transaction save(Transaction toSave) {
        return repository.save(toSave);
    }
}
