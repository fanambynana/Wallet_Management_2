package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.AccountSave;
import management.wallet.model.Enum.AccountName;
import management.wallet.model.Enum.AccountType;
import management.wallet.model.Enum.GetAccountName;
import management.wallet.model.Enum.GetAccountType;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountCrudOperation implements CrudOperation<AccountSave>{
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();

    @Override
    public List<AccountSave> findAll() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<AccountSave> accounts = new ArrayList<>();
        try {
            String query = "SELECT * FROM account";
            statement = connection.prepareStatement(query);
            statement.execute();
            resultSet = statement.getResultSet();

            while (resultSet.next()) {
                accounts.add(new AccountSave(
                        resultSet.getInt("id"),
                        GetAccountName.getEnum(resultSet.getString("account_name")),
                        resultSet.getInt("balance_id"),
                        resultSet.getInt("currency_id"),
                        GetAccountType.getEnum(resultSet.getString("account_type"))
                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all accounts :\n"
                    + exception.getMessage()
            );
        } finally {
            if (statement != null && resultSet != null) {
                try {
                    statement.close();
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println("Error while closing :\n"
                            + e.getMessage()
                    );
                }
            }
        }
        return accounts;
    }

    @Override
    public AccountSave findById(int id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = " SELECT * FROM account WHERE id = ? ";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            resultSet = statement.getResultSet();

            if (resultSet.next()) {
                return new AccountSave(
                        resultSet.getInt("id"),
                        GetAccountName.getEnum(resultSet.getString("account_name")),
                        resultSet.getInt("balance_id"),
                        resultSet.getInt("currency_i2d"),
                        GetAccountType.getEnum(resultSet.getString("account_type"))
                );
            }
        } catch (SQLException sqlException) {
            System.out.println("Error occurred while finding the account :\n"
                    + sqlException.getMessage());
        } finally {
            if (statement != null && resultSet != null) {
                try {
                    statement.close();
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println("Error while closing :\n"
                            + e.getMessage()
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<AccountSave> saveAll(List<AccountSave> toSave) {
        PreparedStatement statement = null;
        try {
            for (AccountSave account : toSave) {
                if (findById(account.getId()) == null) {
                    String query = """
                        INSERT INTO account(account_name, balance_id, currency_id, account_type)
                        VALUES(?, ?, ?, ?)
                    """;
                    statement = connection.prepareStatement(query);
                    statement.setObject(1, account.getAccountName());
                    statement.setInt(2, account.getBalanceId());
                    statement.setInt(3, account.getCurrencyId());
                    statement.setObject(4, account.getAccountType());
                    statement.executeUpdate();
                } else {
                    update(account);
                }
            }
            return toSave;
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving all accounts :\n"
                    + exception.getMessage()
            );
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println("Error while closing :\n"
                            + e.getMessage()
                    );
                }
            }
        }
        return null;
    }

    @Override
    public AccountSave save(AccountSave toSave) {
        PreparedStatement statement = null;
        try {
            if (findById(toSave.getId()) == null) {
                String query = """
                        INSERT INTO account(account_name, balance_id, currency_id, account_type)
                        VALUES(?, ?, ?, ?)
                    """;
                statement = connection.prepareStatement(query);
                statement.setObject(1, toSave.getAccountName());
                statement.setInt(2, toSave.getBalanceId());
                statement.setInt(3, toSave.getCurrencyId());
                statement.setObject(4, toSave.getAccountType());
                statement.executeUpdate();
            } else {
                update(toSave);
            }
            return toSave;
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the account :\n"
                    + exception.getMessage()
            );
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println("Error while closing :\n"
                            + e.getMessage()
                    );
                }
            }
        }
        return null;
    }

    @Override
    public boolean update(AccountSave toUpdate) {
        PreparedStatement statement = null;
        try {
            String query = """
                UPDATE account
                SET account_name = ?, balance_id = ?,
                currency_id = ?, account_type = ?
                WHERE id = ?
            """;
            statement = connection.prepareStatement(query);
            statement.setObject(1, toUpdate.getAccountName());
            statement.setInt(2, toUpdate.getBalanceId());
            statement.setInt(3, toUpdate.getCurrencyId());
            statement.setObject(4, toUpdate.getAccountType());
            statement.setInt(5, toUpdate.getId());

            int rowsUpdated = statement.executeUpdate();
            statement.close();

            return rowsUpdated > 0;
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating the account :\n"
                    + exception.getMessage()
            );
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println("Error while closing :\n"
                            + e.getMessage()
                    );
                }
            }
        }
        return false;
    }
    public boolean updateBalanceIdById (int accountId, int balanceId) {
        PreparedStatement statement = null;
        try {
            String query = """
                UPDATE account
                SET balance_id = ?
                WHERE id = ?
            """;
            statement = connection.prepareStatement(query);
            statement.setObject(1, balanceId);
            statement.setInt(2, accountId);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException exception) {
            System.out.println(
                    "Error occurred while updating the account balance id :\n"
                            + exception.getMessage()
            );
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println("Error while closing :\n"
                            + e.getMessage()
                    );
                }
            }
        }
        return false;
    }
}
