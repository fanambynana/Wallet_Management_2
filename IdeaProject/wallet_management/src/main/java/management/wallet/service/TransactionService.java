package management.wallet.service;

import management.wallet.DAO.ManageTransactional;
import management.wallet.model.Account;
import management.wallet.model.Transaction;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    ManageTransactional manageTransactional;
    public Account makeTransaction(int accountId, Transaction transaction) {
        return manageTransactional.makeTransaction(accountId, transaction);
    }
}
