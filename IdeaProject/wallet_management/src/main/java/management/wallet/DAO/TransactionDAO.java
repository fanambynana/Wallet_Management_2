package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Account;
import management.wallet.model.Transaction;
import management.wallet.model.TransactionType;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionDAO {
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();

    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        try {
            String query = "SELECT * FROM transaction";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getString("label"),
                        resultSet.getBigDecimal("amount"),
                        (LocalDateTime) resultSet.getObject("transaction_date"),
                        (TransactionType) resultSet.getObject("transaction_type")
                ));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all transactions :\n"
                    + exception.getMessage()
            );
        }
        return transactions;
    }

    public Transaction findById(int id) {
        try {
            String query = "SELECT * FROM transaction WHERE id = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                return new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getString("label"),
                        resultSet.getBigDecimal("amount"),
                        (LocalDateTime) resultSet.getObject("transaction_date"),
                        (TransactionType) resultSet.getObject("transaction_type")
                );
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the transaction :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }

    public List<Transaction> saveAll(List<Transaction> toSave) {
        List<Transaction> existingTransactions = new ArrayList<>();
        try {
            for (Transaction transaction : toSave) {
                if (findById(transaction.getId()) == null) {
                    String query = """
                        INSERT INTO transaction(label, amount, transaction_date, transaction_type)
                        VALUES(?, ?, ?, ?)
                    """;
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, transaction.getLabel());
                    preparedStatement.setBigDecimal(2, transaction.getAmount());
                    preparedStatement.setObject(3, transaction.getTransactionDate());
                    preparedStatement.setObject(4, transaction.getTransactionsType());

                    preparedStatement.close();
                } else {
                  UpdateById(transaction.getId(), transaction );
                }
                return toSave;
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving all accounts :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }

    public Transaction save(Transaction toSave) {
        try {
            if (findById(toSave.getId()) == null) {
                String query = """
                        INSERT INTO transaction(label, amount, transaction_date, transaction_type)
                        VALUES(?, ?, ?, ?)
                    """;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, toSave.getLabel());
                preparedStatement.setBigDecimal(2, toSave.getAmount());
                preparedStatement.setObject(3, toSave.getTransactionDate());
                preparedStatement.setObject(4, toSave.getTransactionsType());
                preparedStatement.close();
                return toSave;
            } else {
                // update it
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the transaction :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }
    public boolean UpdateById (int id, Transaction transactionUpdated) {
        try {
            String query = """
            UPDATE account
            SET label = ?, amount = ?, transaction_date = ?
            WHERE id = ?
        """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, transactionUpdated.getLabel());
            preparedStatement.setBigDecimal(2, transactionUpdated.getAmount());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(transactionUpdated.getTransactionDate()));
            preparedStatement.setInt(4, id);

            int rowsUpdated = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsUpdated > 0;
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating account :\n" + exception.getMessage());
            return false;
        }
    }
    public boolean UpdateAmountById (int id, BigDecimal amount) {
        try {
            String query = """
            UPDATE account
            SET amount = ?
            WHERE id = ?
        """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBigDecimal(1, amount);
            preparedStatement.setInt(2, id);

            int rowsUpdated = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsUpdated > 0;
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating account :\n" + exception.getMessage());
            return false;
        }
    }
}
