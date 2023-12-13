package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Account;
import management.wallet.model.AccountGet;
import management.wallet.model.Transaction;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ManageTransactional {
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();
    AccountDAO accountDAO;
    TransactionDAO transactionDAO;
    CurrencyDAO currencyDAO;

    public void beginTransactional() {
        try {
            String query = "BEGIN;";
            PreparedStatement statement = connection.prepareStatement(query);
        } catch (SQLException sqlException) {
            System.out.println("Error occurred :\n"
                + sqlException.getMessage()
            );
        }
    }
    public void commitTransactional() {
        try {
            String query = "COMMIT;";
            connection.prepareStatement(query);
        } catch (SQLException sqlException) {
            System.out.println("Error occurred :\n"
                    + sqlException.getMessage()
            );
        }
    }
    public void rollbackTransactional() {
        try {
            String query = "ROLLBACK;";
            connection.prepareStatement(query);
        } catch (SQLException sqlException) {
            System.out.println("Error occurred :\n"
                    + sqlException.getMessage()
            );
        }
    }

    public AccountGet makeTransaction(int accountId, Transaction transaction) {
        beginTransactional();
        boolean isUpdated = accountDAO.updateAmountById(accountId, transaction.getAmount());
        Transaction saved = transactionDAO.save(transaction);

        if (isUpdated && saved != null) {
            commitTransactional();
            Account account = accountDAO.findById(accountId);
            return new AccountGet(
                    account.getId(),
                    account.getAccountName(),
                    account.getBalanceAmount(),
                    account.getBalanceUpdateDateTime(),
                    transactionDAO.findByAccountId(accountId),
                    currencyDAO.findById(account.getCurrencyId()).getCode(),
                    account.getAccountType()
            );
        } else {
            rollbackTransactional();
        }
        return null;
    }
}
