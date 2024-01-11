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
        List<AccountSave> accountList = new ArrayList<>();
        try {
            String query = "SELECT * FROM account";
            statement = connection.prepareStatement(query);
            statement.execute();
            resultSet = statement.getResultSet();
            while (resultSet.next()) {
                accountList.add(new AccountSave(
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
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return accountList;
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
                        resultSet.getInt("currency_id"),
                        GetAccountType.getEnum(resultSet.getString("account_type"))
                );
            }
        } catch (SQLException sqlException) {
            System.out.println("Error occurred while finding the account :\n"
                    + sqlException.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return null;
    }

    @Override
    public List<AccountSave> saveAll(List<AccountSave> toSave) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<AccountSave> existingList = new ArrayList<>();
        try {
            for (AccountSave account : toSave) {
                if (findById(account.getId()) == null) {
                    String query = String.format("""
                        INSERT INTO account(account_name, balance_id, currency_id, account_type)
                        VALUES
                        ('%s', ?, ?, '%s')
                    """,
                        account.getAccountName().toString().toLowerCase(),
                        account.getAccountType().toString().toLowerCase());
                    statement = connection.prepareStatement(query);
                    statement.setInt(1, account.getBalanceId());
                    statement.setInt(2, account.getCurrencyId());
                    statement.executeUpdate();
                    resultSet = statement.getResultSet();
                    existingList.add(findById(account.getId()));
                } else {
                    existingList.add(update(account));
                }
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving all accounts :\n"
                    + exception.getMessage()
            );
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return existingList;
    }

    @Override
    public AccountSave save(AccountSave toSave) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            if (findById(toSave.getId()) == null) {
                String query = String.format("""
                    INSERT INTO account(account_name, balance_id, currency_id, account_type)
                    VALUES
                    ('%s', ?, ?, '%s')
                """,
                    toSave.getAccountName().toString().toLowerCase(),
                    toSave.getAccountType().toString().toLowerCase());
                statement = connection.prepareStatement(query);
                statement.setInt(1, toSave.getBalanceId());
                statement.setInt(2, toSave.getCurrencyId());
                statement.executeUpdate();
                resultSet = statement.getResultSet();
                return findById(toSave.getId());
            } else {
                return update(toSave);
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the account :\n"
                    + exception.getMessage()
            );
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return null;
    }

    @Override
    public AccountSave update(AccountSave toUpdate) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = String.format("""
                UPDATE account
                SET account_name = '%s',
                balance_id = ?,
                currency_id = ?,
                account_type = '%s'
            """,
                toUpdate.getAccountName().toString().toLowerCase(),
                toUpdate.getAccountType().toString().toLowerCase());
            statement = connection.prepareStatement(query);
            statement.setInt(1, toUpdate.getBalanceId());
            statement.setInt(2, toUpdate.getCurrencyId());
            statement.setInt(3, toUpdate.getId());
            statement.executeUpdate();
            resultSet = statement.getResultSet();
            return findById(toUpdate.getId());
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating the account :\n"
                    + exception.getMessage()
            );
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return null;
    }
    public AccountSave updateBalanceIdById (int accountId, int balanceId) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = """
                UPDATE account
                SET balance_id = ?
                WHERE id = ?
            """;
            statement = connection.prepareStatement(query);
            statement.setObject(1, balanceId);
            statement.setInt(2, accountId);
            statement.executeUpdate();
            resultSet = statement.getResultSet();
            return findById(accountId);
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating the account balance id :\n"
                    + exception.getMessage()
            );
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return null;
    }
}
