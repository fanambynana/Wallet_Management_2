package management.wallet.repository;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Transaction;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepository {
    VerificationSelect verification;
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
                        resultSet.getString("description"),
                        resultSet.getDouble("amaount"),
                        resultSet.getString("type"),
                        resultSet.getString("correspondent")
                ));
            }
            statement.close();
            resultSet.close();
            return transactions;
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all transactions :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }

    public List<Transaction> saveAll(List<Transaction> toSave) {
        List<Transaction> existingTransactions = new ArrayList<>();
        try {
            for (Transaction transaction : toSave) {
                if (verification.verifyTransactionById(transaction.getId()) != null) {
                    existingTransactions.add(transaction);
                } else {
                    String query = """
                        INSERT INTO transaction(description, amount, type, correspondent)
                        VALUES(?, ?, ?, ?)
                    """;
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, transaction.getDescription());
                    preparedStatement.setDouble(2, transaction.getAmount());
                    preparedStatement.setString(3, transaction.getType());
                    preparedStatement.setString(4, transaction.getCorrespondent());
                    preparedStatement.close();
                    return toSave;
                }
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
            if (verification.verifyTransactionById(toSave.getId()) == null) {
                String query = """
                        INSERT INTO transaction(description, amount, type, correspondent)
                        VALUES(?, ?, ?, ?)
                    """;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, toSave.getDescription());
                preparedStatement.setDouble(2, toSave.getAmount());
                preparedStatement.setString(3, toSave.getType());
                preparedStatement.setString(4, toSave.getCorrespondent());
                preparedStatement.close();
                return toSave;
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the transaction :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }
}
