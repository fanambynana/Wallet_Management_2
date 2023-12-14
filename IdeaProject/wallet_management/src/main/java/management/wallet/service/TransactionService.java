package management.wallet.service;

import management.wallet.DAO.ManageTransactional;
import management.wallet.model.Account;
import management.wallet.model.AccountSave;
import management.wallet.model.Transaction;
import management.wallet.model.TransactionSave;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    ManageTransactional manageTransactional;
    public AccountSave makeTransaction(int accountId, TransactionSave transaction) {
        return manageTransactional.makeTransaction(accountId, transaction);
    }
}
