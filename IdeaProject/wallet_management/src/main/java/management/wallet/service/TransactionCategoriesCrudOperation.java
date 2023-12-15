package management.wallet.service;

import management.wallet.DAO.TransactionCategoriesDAO;
import management.wallet.model.TransactionCategories;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionCategoriesCrudOperation implements CrudOperation<TransactionCategories> {
    TransactionCategoriesDAO transactionCategoriesDAO;

    @Override
    public List<TransactionCategories> findAll() {
        return transactionCategoriesDAO.findAll();
    }

    @Override
    public TransactionCategories findById(int id) {
        return transactionCategoriesDAO.findById(id);
    }

    @Override
    public List<TransactionCategories> saveAll(List<TransactionCategories> toSave) {
        return transactionCategoriesDAO.saveAll(toSave);
    }

    @Override
    public TransactionCategories save(TransactionCategories toSave) {
        return transactionCategoriesDAO.save(toSave);
    }

    @Override
    public boolean update(TransactionCategories toUpdate) {
        return transactionCategoriesDAO.update(toUpdate);
    }
}
