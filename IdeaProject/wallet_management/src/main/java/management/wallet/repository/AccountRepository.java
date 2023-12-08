package management.wallet.repository;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Account;
import management.wallet.model.AccountName;
import management.wallet.model.AccountType;
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
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet  = statement.executeQuery();

            while (resultSet.next()) {
                accounts.add(new Account(
                        resultSet.getInt("id"),
                        (AccountName) resultSet.getObject("account_name"),
                        resultSet.getBigDecimal("balance_amount"),
                        resultSet.getTimestamp("balanceUpdateDateTime").toLocalDateTime(),
                        resultSet.getInt("currency_id"),
                        (AccountType) resultSet.getObject("account_type")
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
                if (verification.verifyAccountById(account.getId()) != null) {
                    existingAccounts.add(account);
                } else {
                    String query = """
                        INSERT INTO account(accountName, balanceAmount, balanceUpdateDateTime, currencyId, accountType)
                        VALUES(?, ?, ?, ?, ?)
                    """;
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setObject(1, account.getAccountName());
                    preparedStatement.setBigDecimal(2, account.getBalanceAmount());
                    preparedStatement.setTimestamp(3, Timestamp.valueOf(account.getBalanceUpdateDateTime()));
                    preparedStatement.setInt(4, account.getId());
                    preparedStatement.setObject(5, account.getAccountType());
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
