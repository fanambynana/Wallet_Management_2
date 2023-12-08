package management.wallet.repository;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Account;
import management.wallet.model.Currency;
import management.wallet.model.Transaction;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class VerificationSelect {
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();
    public Account verifyAccountById(int id) {
        try {
            String query = " SELECT * FROM account WHERE id = " + id;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return new Account(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("curency"),
                        resultSet.getDouble("balance")
                );
            }
            statement.close();
            resultSet.close();
        } catch (SQLException sqlException) {
            System.out.println("Verification error :\n" + sqlException.getMessage());
        }
        return null;
    }
    public Account verifyAccountByUsername(String username) {
        try {
            String query = " SELECT * FROM account WHERE username = " + username;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return new Account(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("curency"),
                        resultSet.getDouble("balance")
                );
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Verification error :\n" + exception.getMessage());
        }
        return null;
    }

    public Transaction verifyTransactionById(int id) {
        try {
            String query = " SELECT * FROM transaction WHERE id = " + id;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getString("description"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("type"),
                        resultSet.getString("correspondant")
                );
            }
        } catch (SQLException sqlException) {
            System.out.println("Verification error :\n" + sqlException.getMessage());
        }
        return null;
    }

    public Currency verifyCurrencyByCode(String code) {
        try {
            String query = " SELECT * FROM currency WHERE code = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, code);
            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                return new Currency(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("coe")
                );
            }
        } catch (SQLException sqlException) {
            System.out.println("Verification error :\n" + sqlException.getMessage());
        }
        return null;
    }
}
