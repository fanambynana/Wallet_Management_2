package management.wallet.repository;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Account;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountRepository {
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();
    VerificationSelect verification;

    public List<Account> findAll() {
        List<Account> accounts  = new ArrayList<>();
        try {
            String query = "SELECT * FROM account";
            Statement statement = connection.createStatement();
            ResultSet resultSet  = statement.executeQuery(query);

            while (resultSet.next()) {
                accounts.add(new Account(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("currency"),
                        resultSet.getDouble("balance")
                ));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all accounts :\n"
                + exception.getMessage()
            );
        }
        return accounts;
    }

    public List<Account> saveAll(List<Account> toSave) {
        List<Account> existingAccounts = new ArrayList<>();
        try {
            for (Account account : toSave) {
                if (verification.verifyAccountByUsername(account.getUsername()) != null) {
                    existingAccounts.add(account);
                } else {
                    String query = """
                        INSERT INTO account(username, currency, balance)
                        VALUES(?, ?, ?)
                    """;
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, account.getUsername());
                    preparedStatement.setString(2, account.getCurrency());
                    preparedStatement.setDouble(3, account.getBalance());
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

    public Account save(Account toSave) {
        try {
            if (verification.verifyAccountByUsername(toSave.getUsername()) == null) {
                String query = """
                        INSERT INTO account(username, currency, balance)
                        VALUES(?, ?, ?)
                    """;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, toSave.getUsername());
                preparedStatement.setString(2, toSave.getCurrency());
                preparedStatement.setDouble(3, toSave.getBalance());
                preparedStatement.close();
                return toSave;
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the account :\n"
                + exception.getMessage()
            );
        }
        return null;
    }

    public Account findById(int id) {
        try {
            String query = "SELECT * FROM account WHERE id = " + id;
            Statement statement = connection.createStatement();
            ResultSet resultSet  = statement.executeQuery(query);

            if (resultSet.next()) {
                return new Account(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("currency"),
                        resultSet.getDouble("balance")
                );
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the account :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }

    public Account findByUsername(String username) {
        try {
            String query = "SELECT * FROM account WHERE username = " + username;
            Statement statement = connection.createStatement();
            ResultSet resultSet  = statement.executeQuery(query);

            if (resultSet.next()) {
                return new Account(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("currency"),
                        resultSet.getDouble("balance")
                );
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the account :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }
}
