package management.wallet.service;

import management.wallet.DAO.TransactionCategoriesDAO;
import management.wallet.model.AmountPerCategory;
import management.wallet.model.TransactionCategories;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    public BigDecimal findIncomeByInterval(int accountId, LocalDateTime startDatetime, LocalDateTime endDateTime) {
        return transactionCategoriesDAO.findIncomeByInterval(accountId, startDatetime, endDateTime);
    }
    public BigDecimal findExpenseByInterval(int accountId, LocalDateTime startDatetime, LocalDateTime endDateTime) {
        return transactionCategoriesDAO.findExpenseByInterval(accountId, startDatetime, endDateTime);
    }
    public List<AmountPerCategory> findByIdAccount(int accountId, LocalDateTime startDatetime, LocalDateTime endDateTime) {
        return transactionCategoriesDAO.findByIdAccount(accountId, startDatetime, endDateTime);
    }
}
