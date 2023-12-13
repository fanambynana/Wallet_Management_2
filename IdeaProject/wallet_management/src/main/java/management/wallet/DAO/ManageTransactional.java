package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.*;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Repository
public class ManageTransactional {
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();
    AccountDAO accountDAO;
    TransactionDAO transactionDAO;
    CurrencyDAO currencyDAO;
    BalanceDAO balanceDAO;
    BalanceHistoryDAO balanceHistoryDAO;

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

    public Account makeTransaction(int accountId, Transaction transaction) {
        beginTransactional();
        AccountSave account = accountDAO.findById(accountId);
        Transaction transactionSaved = transactionDAO.save(transaction);
        Balance balanceSaved = balanceDAO.save(new Balance(
                0,
                transactionSaved.getAmount(),
                LocalDateTime.now()
        ));
        BalanceHistory balanceHistorySaved = balanceHistoryDAO.save(new BalanceHistory(
                0,
                balanceSaved.getId(),
                account.getId(),
                LocalDateTime.now()
        ));
        boolean isUpdated =
                balanceSaved != null
                &&
                balanceHistorySaved != null
                &&
                transactionSaved != null;


        if (isUpdated) {
            commitTransactional();
            return new Account(
                    account.getId(),
                    account.getAccountName(),
                    balanceSaved,
                    transactionDAO.findByAccountId(accountId),
                    currencyDAO.findById(account.getCurrencyId()),
                    account.getAccountType()
            );
        } else {
            rollbackTransactional();
        }
        return null;
    }
}
