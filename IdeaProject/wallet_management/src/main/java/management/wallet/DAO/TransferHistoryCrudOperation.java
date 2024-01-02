package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.AccountSave;
import management.wallet.model.Enum.*;
import management.wallet.model.TransferHistory;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransferHistoryCrudOperation implements CrudOperation<TransferHistory> {
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();

    @Override
    public List<TransferHistory> findAll() {
        List<TransferHistory> transferHistories = new ArrayList<>();
        try {
            String query = "SELECT * FROM transfer_history";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet resultSet  = statement.getResultSet();

            while (resultSet.next()) {
                transferHistories.add(new TransferHistory(
                        resultSet.getInt("id"),
                        resultSet.getInt("debit_transaction_id"),
                        resultSet.getInt("credit_transaction_id"),
                        ((Timestamp) resultSet.getObject("datetime")).toLocalDateTime()
                ));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all transfer histories :\n"
                    + exception.getMessage()
            );
        }
        return transferHistories;
    }

    @Override
    public TransferHistory findById(int id) {
        try {
            String query = "SELECT * FROM transfer_history WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            ResultSet resultSet  = statement.getResultSet();

            if (resultSet.next()) {
                return new TransferHistory(
                        resultSet.getInt("id"),
                        resultSet.getInt("debit_transaction_id"),
                        resultSet.getInt("credit_transaction_id"),
                        ((Timestamp) resultSet.getObject("datetime")).toLocalDateTime()
                );
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all transfer histories :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }

    @Override
    public List<TransferHistory> saveAll(List<TransferHistory> toSave) {
        return null;
    }

    @Override
    public TransferHistory save(TransferHistory toSave) {
        try {
            String query = """
                        INSERT INTO transfer_history
                        (debit_tansaction_id, credit_transaction_id, datetime)
                        VALUES(?, ?, ?)
                    """;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, toSave.getDebitTransactionId());
            statement.setInt(2, toSave.getCreditTransactionId());
            statement.setObject(3, toSave.getDateTime());
            statement.executeUpdate();
            statement.close();
            return toSave;
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the balance history :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }

    @Override
    public boolean update(TransferHistory toUpdate) {
        return false;
    }

    public List<AccountSave> findByIntervalReturnDebitAccount(LocalDateTime from, LocalDateTime to) {
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
            statement.execute();
            ResultSet resultSet  = statement.getResultSet();

            while (resultSet.next()) {
                accounts.add(new AccountSave(
                        resultSet.getInt("id"),
                        GetAccountName.getEnum(resultSet.getString("account_name")),
                        resultSet.getInt("balance_id"),
                        resultSet.getInt("currency_id"),
                        GetAccountType.getEnum(resultSet.getString("account_type"))
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
    public  List<AccountSave> findByIntervalReturnCreditAccount(LocalDateTime from, LocalDateTime to) {
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
            statement.execute();
            ResultSet resultSet  = statement.getResultSet();

            while (resultSet.next()) {
                accounts.add(new AccountSave(
                        resultSet.getInt("id"),
                        GetAccountName.getEnum(resultSet.getString("account_name")),
                        resultSet.getInt("balance_id"),
                        resultSet.getInt("currency_id"),
                        GetAccountType.getEnum(resultSet.getString("account_type"))
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
            statement.execute();
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
            statement.execute();
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
}
