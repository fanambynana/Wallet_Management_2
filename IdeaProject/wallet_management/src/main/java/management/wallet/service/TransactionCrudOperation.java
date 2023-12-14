package management.wallet.service;

import management.wallet.model.Transaction;
import management.wallet.DAO.TransactionDAO;
import management.wallet.model.TransactionSave;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionCrudOperation implements CrudOperation<TransactionSave>{
    TransactionDAO repository;

    @Override
    public List<TransactionSave> findAll() {
        return repository.findAll();
    }

    @Override
    public TransactionSave findById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<TransactionSave> saveAll(List<TransactionSave> toSave) {
        return repository.saveAll(toSave);
    }

    @Override
    public TransactionSave save(TransactionSave toSave) {
        return repository.save(toSave);
    }

    @Override
    public boolean update(TransactionSave toUpdate) {
        return repository.update(toUpdate);
    }
}
