package management.wallet.service;

import management.wallet.DAO.TransferHistoryDAO;
import management.wallet.model.TransferHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransferHistoryCrudOperation implements CrudOperation<TransferHistory> {
    TransferHistoryDAO transferHistoryDAO;

    @Override
    public List<TransferHistory> findAll() {
        return null;
    }

    @Override
    public TransferHistory findById(int id) {
        return null;
    }

    @Override
    public List<TransferHistory> saveAll(List<TransferHistory> toSave) {
        return null;
    }

    @Override
    public TransferHistory save(TransferHistory toSave) {
        return transferHistoryDAO.save(toSave);
    }

    @Override
    public boolean update(TransferHistory toUpdate) {
        return false;
    }
}
