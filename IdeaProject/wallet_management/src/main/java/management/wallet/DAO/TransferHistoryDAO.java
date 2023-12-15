package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.*;
import management.wallet.model.Enum.AccountName;
import management.wallet.model.Enum.AccountType;
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
    AccountDAO accountDAO;
    CurrencyValueDAO currencyValueDAO;

    public List<AccountSave> findByIntervalReturnDebitAccount(
            LocalDateTime from, LocalDateTime to
    ) {
        List<AccountSave> accounts = new ArrayList<>();
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
                accounts.add(new AccountSave(
                        accountId,
                        (AccountName) resultSet.getObject("account_name"),
                        resultSet.getInt("balance_id"),
                        resultSet.getInt("currency_id"),
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

    public List<AccountSave> findByIntervalReturnCreditAccount(LocalDateTime from, LocalDateTime to) {
        List<AccountSave> accounts = new ArrayList<>();
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
                accounts.add(new AccountSave(
                        accountId,
                        (AccountName) resultSet.getObject("account_name"),
                        resultSet.getInt("balance_id"),
                        resultSet.getInt("currency_id"),
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

    public List<LocalDateTime> findByIntervalReturnTransferDateTime(LocalDateTime from, LocalDateTime to) {
        List<LocalDateTime> datetimes = new ArrayList<>();
        try {
            String query = """
                SELECT
                th.datetime
                FROM transfer_history th
                INNER JOIN transaction t
                ON th.credit_transaction_id = t.id
                INNER JOIN "account" a
                ON transaction.account_id = a.id
                WHERE th.date_time BETWEEN ? AND ?
            """;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, from);
            statement.setObject(1, to);
            ResultSet resultSet  = statement.getResultSet();

            while (resultSet.next()) {
                datetimes.add((LocalDateTime) resultSet.getObject("datetime"));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding transfer histories :\n"
                    + exception.getMessage()
            );
        }
        return datetimes;
    }

    public TransferHistory save(TransferHistory toSave) {
        try {
            String query = """
                        INSERT INTO transfer_history
                        (debit_tansaction_id, credit_transaction_id, datetime)
                        VALUES(?, ?, ?)
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, toSave.getDebitTransactionId());
            preparedStatement.setInt(2, toSave.getCreditTransactionId());
            preparedStatement.setObject(3, toSave.getDateTime());
            preparedStatement.close();
            return toSave;
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the balance history :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }

    /*public TransferHistory saveWithExchange(TransferHistory toSave) {
        try {

            TransactionSave debitTransaction = transactionDAO.findById(toSave.getDebitTransactionId());
            AccountSave debitAccount = accountDAO.findById(debitTransaction.getAccountId());
            CurrencyValue currencyValue = currencyValueDAO.findByDate(LocalDate.from(debitTransaction.getTransactionDate()));


            String query = """
                        INSERT INTO transfer_history
                        (debit_tansaction_id, credit_transaction_id, datetime)
                        VALUES(?, ?, ?)
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, toSave.getDebitTransactionId());
            preparedStatement.setInt(2, toSave.getCreditTransactionId());
            preparedStatement.setObject(3, toSave.getDateTime());
            preparedStatement.close();
            return toSave;
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the balance history :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }*/
}
