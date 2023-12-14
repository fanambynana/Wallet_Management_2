package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Transaction;
import management.wallet.model.TransactionCategories;
import management.wallet.model.TransactionType;
import org.springframework.stereotype.Repository;

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
                        (TransactionType) resultSet.getObject("transaction_type"),
                        (TransactionCategories) resultSet.getObject("category")
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
            String query = "SELECT * FROM \"transaction\" WHERE id = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                return new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getString("label"),
                        resultSet.getBigDecimal("amount"),
                        (LocalDateTime) resultSet.getObject("transaction_date"),
                        (TransactionType) resultSet.getObject("transaction_type"),
                        (TransactionCategories) resultSet.getObject("category")
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
    public List<Transaction> findByAccountId(int id) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            String query = "SELECT * FROM \"transaction\" WHERE account_id = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getString("label"),
                        resultSet.getBigDecimal("amount"),
                        (LocalDateTime) resultSet.getObject("transaction_date"),
                        (TransactionType) resultSet.getObject("transaction_type"),
                        (TransactionCategories) resultSet.getObject("category")
                ));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the transaction :\n"
                    + exception.getMessage()
            );
        }
        return transactions;
    }

    public List<Transaction> saveAll(List<Transaction> toSave) {
        List<Transaction> existingTransactions = new ArrayList<>();
        try {
            for (Transaction transaction : toSave) {
                if (findById(transaction.getId()) == null) {
                    String query = """
                        INSERT INTO \"transaction\" (label, amount, transaction_date, transaction_type)
                        VALUES(?, ?, ?, ?, ?)
                    """;
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, transaction.getLabel());
                    preparedStatement.setBigDecimal(2, transaction.getAmount());
                    preparedStatement.setObject(3, transaction.getTransactionDate());
                    preparedStatement.setObject(4, transaction.getTransactionType());
                    preparedStatement.setObject(5, transaction.getCategory());

                    preparedStatement.close();
                } else {
                  update(transaction);
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
                        INSERT INTO \"transaction\" (label, amount, transaction_date, transaction_type, category)
                        VALUES(?, ?, ?, ?, ?)
                    """;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, toSave.getLabel());
                preparedStatement.setBigDecimal(2, toSave.getAmount());
                preparedStatement.setObject(3, toSave.getTransactionDate());
                preparedStatement.setObject(4, toSave.getTransactionType());
                preparedStatement.setObject(5, toSave.getCategory());
                preparedStatement.close();
                return toSave;
            } else {
                update(toSave);
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the transaction :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }
    public boolean update(Transaction transactionUpdated) {
        try {
            String query = """
                UPDATE \"tansaction\"
                SET label = ?, amount = ?, transaction_date = ?, transaction_type = ?, category = ?
                WHERE id = ?
            """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, transactionUpdated.getLabel());
            preparedStatement.setBigDecimal(2, transactionUpdated.getAmount());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(transactionUpdated.getTransactionDate()));
            preparedStatement.setObject(4, transactionUpdated.getTransactionType());
            preparedStatement.setObject(5, transactionUpdated.getCategory());
            preparedStatement.setInt(6, transactionUpdated.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsUpdated > 0;
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating the transaction :\n"
                    + exception.getMessage());
            return false;
        }
    }
}
