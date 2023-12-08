package management.wallet.repository;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Account;
import management.wallet.model.AccountName;
import management.wallet.model.AccountType;
import management.wallet.model.Transaction;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class VerificationSelect {
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();
    public Account verifyAccountById(int id) {
        try {
            String query = " SELECT * FROM account WHERE id = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt("id"));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Account(
                        resultSet.getInt("id"),
                        (AccountName) resultSet.getObject("account_name"),
                        resultSet.getBigDecimal("balance_amount"),
                        resultSet.getTimestamp("balanceUpdateDateTime").toLocalDateTime(),
                        resultSet.getInt("currencyId"),
                        (AccountType) resultSet.getObject("account_type")
                );
            }
        } catch (SQLException sqlException) {
            System.out.println("Verification error :\n" + sqlException.getMessage());
        }
        return null;
    }

    public Transaction verifyTransactionById(int id) {
        try {
            String query = " SELECT * FROM transaction WHERE id = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, "id");
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
}

