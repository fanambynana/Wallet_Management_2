package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.AccountSave;
import management.wallet.model.AccountName;
import management.wallet.model.AccountType;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountDAO {
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();

    public List<AccountSave> findAll() {
        List<AccountSave> accounts = new ArrayList<>();
        try {
            String query = "SELECT * FROM account";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                accounts.add(new AccountSave(
                        resultSet.getInt("id"),
                        (AccountName) resultSet.getObject("account_name"),
                        resultSet.getInt("balance_id"),
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

    public List<AccountSave> saveAll(List<AccountSave> toSave) {
        try {
            for (AccountSave account : toSave) {
                if (findById(account.getId()) == null) {
                    String query = """
                        INSERT INTO account(account_name, balance_id, currency_id, account_type)
                        VALUES(?, ?, ?, ?)
                    """;
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setObject(1, account.getAccountName());
                    preparedStatement.setInt(2, account.getBalanceId());
                    preparedStatement.setInt(3, account.getCurrencyId());
                    preparedStatement.setObject(4, account.getAccountType());
                    preparedStatement.close();
                } else {
                    update(account);
                }
            }
            return toSave;
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving all accounts :\n"
                + exception.getMessage()
            );
        }
        return null;
    }
    public AccountSave save(AccountSave toSave) {
        try {
            if (findById(toSave.getId()) == null) {
                String query = """
                        INSERT INTO account(account_name, balance_id, currency_id, account_type)
                        VALUES(?, ?, ?, ?)
                    """;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setObject(1, toSave.getAccountName());
                preparedStatement.setInt(2, toSave.getBalanceId());
                preparedStatement.setInt(3, toSave.getCurrencyId());
                preparedStatement.setObject(4, toSave.getAccountType());
                preparedStatement.close();
            } else {
                update(toSave);
            }
            return toSave;
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the account :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }
    public AccountSave findById(int id) {
        try {
            String query = " SELECT * FROM account WHERE id = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new AccountSave(
                        resultSet.getInt("id"),
                        (AccountName) resultSet.getObject("account_name"),
                        resultSet.getInt("balance_id"),
                        resultSet.getInt("currencyId"),
                        (AccountType) resultSet.getObject("account_type")
                );
            }
        } catch (SQLException sqlException) {
            System.out.println("Error occurred while finding the account :\n"
                    + sqlException.getMessage());
        }
        return null;
    }
    public boolean update (AccountSave accountUpdated) {
        try {
            String query = """
            UPDATE account
                SET account_name = ?, balance_id = ?,
                currency_id = ?, account_type = ?
                WHERE id = ?
            """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, accountUpdated.getAccountName());
            preparedStatement.setInt(2, accountUpdated.getBalanceId());
            preparedStatement.setInt(3, accountUpdated.getCurrencyId());
            preparedStatement.setObject(4, accountUpdated.getAccountType());
            preparedStatement.setInt(5, accountUpdated.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsUpdated > 0;
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating the account :\n" + exception.getMessage());
            return false;
        }
    }
    public boolean updateBalanceIdById (int accountId, int balanceId) {
        try {
            String query = """
            UPDATE account
                SET balance_id = ?
                WHERE id = ?
            """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, balanceId);
            preparedStatement.setInt(2, accountId);

            int rowsUpdated = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsUpdated > 0;
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating the account balance id :\n" + exception.getMessage());
            return false;
        }
    }
  }
