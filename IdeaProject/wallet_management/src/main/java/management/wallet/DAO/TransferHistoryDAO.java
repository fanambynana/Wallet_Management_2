package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransferHistoryDAO {
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();
    BalanceDAO balanceDAO;
    TransactionDAO transactionDAO;
    CurrencyDAO currencyDAO;

    public List<Account> findByIntervalReturnDebitAccount(LocalDateTime from, LocalDateTime to) {
        List<Account> accounts = new ArrayList<>();
        try {
            String query = """
                SELECT
                a.id, a.account_name, a.balance_id, a.currency_id, a.account_type
                FROM transfer_history
                INNER JOIN transaction
                ON transfer_history.debit_transaction_id = transaction.id
                INNER JOIN "account" a
                ON transaction.account_id = a.id
                WHERE transfer_history.date_time BETWEEN ? AND ?
            """;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, from);
            statement.setObject(1, to);
            ResultSet resultSet  = statement.getResultSet();

            while (resultSet.next()) {
                int accountId = resultSet.getInt("id");
                accounts.add(new Account(
                        accountId,
                        (AccountName) resultSet.getObject("account_name"),
                        balanceDAO.findById(resultSet.getInt("balance_id")),
                        transactionDAO.findByAccountId(accountId),
                        currencyDAO.findById(resultSet.getInt("currency_id")),
                        (AccountType) resultSet.getObject("account_type")
                ));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding transfer histories :\n"
                    + exception.getMessage()
            );
        }
        return accounts;
    }

    public List<Account> findByIntervalReturnCreditAccount(LocalDateTime from, LocalDateTime to) {
        List<Account> accounts = new ArrayList<>();
        try {
            String query = """
                SELECT
                a.id, a.account_name, a.balance_id, a.currency_id, a.account_type
                FROM transfer_history
                INNER JOIN transaction
                ON transfer_history.credit_transaction_id = transaction.id
                INNER JOIN "account" a
                ON transaction.account_id = a.id
                WHERE transfer_history.date_time BETWEEN ? AND ?
            """;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, from);
            statement.setObject(1, to);
            ResultSet resultSet  = statement.getResultSet();

            while (resultSet.next()) {
                int accountId = resultSet.getInt("id");
                accounts.add(new Account(
                        accountId,
                        (AccountName) resultSet.getObject("account_name"),
                        balanceDAO.findById(resultSet.getInt("balance_id")),
                        transactionDAO.findByAccountId(accountId),
                        currencyDAO.findById(resultSet.getInt("currency_id")),
                        (AccountType) resultSet.getObject("account_type")
                ));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding transfer histories :\n"
                    + exception.getMessage()
            );
        }
        return accounts;
    }

    public List<BigDecimal> findByIntervalReturnTransferAmount(LocalDateTime from, LocalDateTime to) {
        List<BigDecimal> amounts = new ArrayList<>();
        try {
            String query = """
                SELECT
                t.amount
                FROM transfer_history
                INNER JOIN transaction t
                ON transfer_history.credit_transaction_id = t.id
                INNER JOIN "account" a
                ON transaction.account_id = a.id
                WHERE transfer_history.date_time BETWEEN ? AND ?
            """;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, from);
            statement.setObject(1, to);
            ResultSet resultSet  = statement.getResultSet();

            while (resultSet.next()) {
                amounts.add(resultSet.getBigDecimal("amount"));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding transfer histories :\n"
                    + exception.getMessage()
            );
        }
        return amounts;
    }
}
