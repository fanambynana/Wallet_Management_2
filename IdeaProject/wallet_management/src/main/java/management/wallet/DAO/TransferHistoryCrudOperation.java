package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Account;
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
    @Override
    public List<TransferHistory> findAll() {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<TransferHistory> transferHistories = new ArrayList<>();
        try {
            String query = "SELECT * FROM transfer_history";
            statement = connection.prepareStatement(query);
            statement.execute();
            resultSet  = statement.getResultSet();
            connection.close();
            while (resultSet.next()) {
                transferHistories.add(new TransferHistory(
                        resultSet.getInt("id"),
                        resultSet.getInt("debit_transaction_id"),
                        resultSet.getInt("credit_transaction_id"),
                        ((Timestamp) resultSet.getObject("datetime")).toLocalDateTime()
                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all transfer histories :\n"
                    + exception.getMessage()
            );
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                connection.close();
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return transferHistories;
    }

    @Override
    public TransferHistory findById(int id) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM transfer_history WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            resultSet  = statement.getResultSet();
            connection.close();
            if (resultSet.next()) {
                return new TransferHistory(
                        resultSet.getInt("id"),
                        resultSet.getInt("debit_transaction_id"),
                        resultSet.getInt("credit_transaction_id"),
                        ((Timestamp) resultSet.getObject("datetime")).toLocalDateTime()
                );
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all transfer histories :\n"
                    + exception.getMessage()
            );
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                connection.close();
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return null;
    }

    @Override
    public List<TransferHistory> saveAll(List<TransferHistory> toSave) {
        List<TransferHistory> existingList = new ArrayList<>();
        for (TransferHistory transfer : toSave) {
            TransferHistory saved  = save(transfer);
            if (saved  != null) {
                existingList.add(saved);
            }
        }
        return existingList;
    }

    @Override
    public TransferHistory save(TransferHistory toSave) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            if (findById(toSave.getId()) == null) {
                String query = """
                    INSERT INTO transfer_history
                    (debit_transaction_id, credit_transaction_id, datetime)
                    VALUES(?, ?, ?)
                """;
                statement = connection.prepareStatement(query);
                statement.setInt(1, toSave.getDebitTransactionId());
                statement.setInt(2, toSave.getCreditTransactionId());
                statement.setObject(3, toSave.getDatetime());
                statement.executeUpdate();
                resultSet = statement.getResultSet();
                connection.close();
                return findById(toSave.getId());
            } else {
                connection.close();
                return update(toSave);
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the balance history :\n"
                    + exception.getMessage()
            );
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                connection.close();
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return null;
    }

    @Override
    public TransferHistory update(TransferHistory toUpdate) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = """
                UPDATE transfer_history
                SET
                debit_transaction_id = ?,
                credit_transaction_id = ?,
                datetime = ?
                WHERE id = ?
            """;
            statement = connection.prepareStatement(query);
            statement.setInt(1, toUpdate.getDebitTransactionId());
            statement.setInt(2, toUpdate.getCreditTransactionId());
            statement.setObject(3, toUpdate.getDatetime());
            statement.setInt(4, toUpdate.getId());
            statement.executeUpdate();
            resultSet = statement.getResultSet();
            connection.close();
            return findById(toUpdate.getId());
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the balance history :\n"
                    + exception.getMessage()
            );
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                connection.close();
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return null;
    }

    public List<Account> findByIntervalReturnDebitAccount(LocalDateTime from, LocalDateTime to) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Account> accountList = new ArrayList<>();
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
            statement = connection.prepareStatement(query);
            statement.setObject(1, from);
            statement.setObject(1, to);
            statement.execute();
            resultSet  = statement.getResultSet();
            connection.close();
            while (resultSet.next()) {
                accountList.add(new Account(
                        resultSet.getInt("id"),
                        GetAccountName.getEnum(resultSet.getString("account_name")),
                        resultSet.getInt("balance_id"),
                        resultSet.getInt("currency_id"),
                        GetAccountType.getEnum(resultSet.getString("account_type"))
                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding transfer histories :\n"
                    + exception.getMessage()
            );
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                connection.close();
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return accountList;
    }
    public  List<Account> findByIntervalReturnCreditAccount(LocalDateTime from, LocalDateTime to) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Account> accountList = new ArrayList<>();
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
            statement = connection.prepareStatement(query);
            statement.setObject(1, from);
            statement.setObject(1, to);
            statement.execute();
            resultSet  = statement.getResultSet();
            connection.close();
            while (resultSet.next()) {
                accountList.add(new Account(
                        resultSet.getInt("id"),
                        GetAccountName.getEnum(resultSet.getString("account_name")),
                        resultSet.getInt("balance_id"),
                        resultSet.getInt("currency_id"),
                        GetAccountType.getEnum(resultSet.getString("account_type"))
                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding transfer histories :\n"
                    + exception.getMessage()
            );
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                connection.close();
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return accountList;
    }
    public List<BigDecimal> findByIntervalReturnTransferAmount(LocalDateTime from, LocalDateTime to) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<BigDecimal> amountList = new ArrayList<>();
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
            statement = connection.prepareStatement(query);
            statement.setObject(1, from);
            statement.setObject(1, to);
            statement.execute();
            resultSet  = statement.getResultSet();
            connection.close();
            while (resultSet.next()) {
                amountList.add(resultSet.getBigDecimal("amount"));
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding transfer histories :\n"
                    + exception.getMessage()
            );
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                connection.close();
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return amountList;
    }
    public List<LocalDateTime> findByIntervalReturnTransferDateTime(LocalDateTime from, LocalDateTime to) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<LocalDateTime> dateTimeList = new ArrayList<>();
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
            statement = connection.prepareStatement(query);
            statement.setObject(1, from);
            statement.setObject(1, to);
            statement.execute();
            resultSet  = statement.getResultSet();
            connection.close();
            while (resultSet.next()) {
                dateTimeList.add(((Timestamp) resultSet.getObject("datetime")).toLocalDateTime());
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding transfer histories :\n"
                    + exception.getMessage()
            );
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                connection.close();
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return dateTimeList;
    }
}
