package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Account;
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

    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        try {
            String query = "SELECT * FROM account";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                accounts.add(new Account(
                        resultSet.getInt("id"),
                        (AccountName) resultSet.getObject("account_name"),
                        resultSet.getBigDecimal("balance_amount"),
                        resultSet.getTimestamp("balance_update_date_time").toLocalDateTime(),
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
        try {
            for (Account account : toSave) {
                if (findById(account.getId()) == null) {
                    String query = """
                        INSERT INTO account(account_name, balance_amount, balance_update_date_time, currency_id, account_type)
                        VALUES(?, ?, ?, ?, ?)
                    """;
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setObject(1, account.getAccountName());
                    preparedStatement.setBigDecimal(2, account.getBalanceAmount());
                    preparedStatement.setTimestamp(3, Timestamp.valueOf(account.getBalanceUpdateDateTime()));
                    preparedStatement.setInt(4, account.getId());
                    preparedStatement.setObject(5, account.getAccountType());
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
    public Account save(Account toSave) {
        try {
            if (findById(toSave.getId()) == null) {
                String query = """
                        INSERT INTO account(account_name, balance_amount, balance_update_date_time, currency_id, account_type)
                        VALUES(?, ?, ?, ?, ?)
                    """;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setObject(1, toSave.getAccountName());
                preparedStatement.setBigDecimal(2, toSave.getBalanceAmount());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(toSave.getBalanceUpdateDateTime()));
                preparedStatement.setInt(4, toSave.getId());
                preparedStatement.setObject(5, toSave.getAccountType());
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
    public Account findById(int id) {
        try {
            String query = " SELECT * FROM account WHERE id = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Account(
                        resultSet.getInt("id"),
                        (AccountName) resultSet.getObject("account_name"),
                        resultSet.getBigDecimal("balance_amount"),
                        resultSet.getTimestamp("balance_update_date_time").toLocalDateTime(),
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
    public boolean update (Account accountUpdated) {
        try {
            String query = """
            UPDATE account
                SET account_name = ?, balance_amount = ?,
                balance_update_date_time = ?,
                currency_id = ?, account_type = ?
                WHERE id = ?
            """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, accountUpdated.getAccountName());
            preparedStatement.setBigDecimal(2, accountUpdated.getBalanceAmount());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(accountUpdated.getBalanceUpdateDateTime()));
            preparedStatement.setInt(4, accountUpdated.getCurrencyId());
            preparedStatement.setObject(5, accountUpdated.getAccountType());
            preparedStatement.setInt(6, accountUpdated.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsUpdated > 0;
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating the account :\n" + exception.getMessage());
            return false;
        }
    }
    public boolean updateAmountById (int id, BigDecimal amount) {
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
            System.out.println("Error occurred while updating the account :\n"
                    + exception.getMessage());
            return false;
        }
    }
  }
