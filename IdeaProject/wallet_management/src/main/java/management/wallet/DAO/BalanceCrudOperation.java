package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Balance;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BalanceCrudOperation implements CrudOperation<Balance> {
    AccountCrudOperation accountCrudOperation = new AccountCrudOperation();

    @Override
    public List<Balance> findAll() {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Balance> balanceList  = new ArrayList<>();
        try {
            String query = "SELECT * FROM balance";
            statement = connection.prepareStatement(query);
            statement.execute();
            resultSet  = statement.getResultSet();
            while (resultSet.next()) {
                balanceList.add(new Balance(
                        resultSet.getInt("id"),
                        resultSet.getBigDecimal("amount"),
                        ((Timestamp) resultSet.getObject("update_datetime")).toLocalDateTime()
                ));
            }
            connection.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all balances :\n"
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
                connection.close();
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return balanceList;
    }

    @Override
    public Balance findById(int id) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM balance WHERE id = ? ";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            resultSet  = statement.getResultSet();
            connection.close();
            if (resultSet.next()) {
                return new Balance(
                        resultSet.getInt("id"),
                        resultSet.getBigDecimal("amount"),
                        ((Timestamp) resultSet.getObject("update_datetime")).toLocalDateTime()
                );
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the balance :\n"
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
                connection.close();
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return null;
    }

    @Override
    public List<Balance> saveAll(List<Balance> toSave) {
        List<Balance> existingList = new ArrayList<>();
        for (Balance balance : toSave) {
            Balance saved = save(balance);
            if (saved != null){
                existingList.add(saved);
            }
        }
        return existingList;
    }

    @Override
    public Balance save(Balance toSave) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            if (findById(toSave.getId()) == null) {
                String query = """
                        INSERT INTO balance(amount, update_datetime)
                        VALUES(?, ?)
                    """;
                statement = connection.prepareStatement(query);
                statement.setObject(1, toSave.getAmount());
                statement.setObject(2, toSave.getUpdateDatetime());
                statement.executeUpdate();
                resultSet = statement.getResultSet();
                connection.close();
                return findById(toSave.getId());
            } else {
                connection.close();
                return update(toSave);
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the balance :\n"
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
                connection.close();
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return null;
    }

    @Override
    public Balance update(Balance toUpdate) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = """
            UPDATE balance
            SET amount = ?, update_datetime = ?
            WHERE id = ?
        """;
            statement = connection.prepareStatement(query);
            statement.setObject(1, toUpdate.getAmount());
            statement.setObject(2, toUpdate.getUpdateDatetime());
            statement.setInt(3, toUpdate.getId());
            statement.executeUpdate();
            resultSet = statement.getResultSet();
            connection.close();
            return findById(toUpdate.getId());
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating the balance :\n"
                    + exception.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                connection.close();
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return null;
    }

    public Balance findByDateTime(LocalDateTime dateTime) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM balance WHERE update_date_time = ? ";
            statement = connection.prepareStatement(query);
            statement.setObject(1, dateTime);
            statement.execute();
            resultSet = statement.getResultSet();
            connection.close();
            if (resultSet.next()) {
                return new Balance(
                        resultSet.getInt("id"),
                        resultSet.getBigDecimal("amount"),
                        ((Timestamp) resultSet.getObject("update_datetime")).toLocalDateTime()
                );
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the balance :\n"
                    + exception.getMessage()
            );
            // throw new RuntimeException(exception);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                connection.close();
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
                /*
                    e.printStackTrace();
                    throw new RuntimeException(e);
                 */
            }
        }
        return null;
    }
    public Balance findCurrentBalance(int accountId) {
        return findById(
                accountCrudOperation.findById(accountId).getBalanceId()
        );
    }
    public List<Balance> findByInterval(LocalDateTime from, LocalDateTime to) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Balance> balanceList = new ArrayList<>();
        try {
            String query = """
                SELECT
                balance.id,
                balance.amount,
                balance.update_datetime
                FROM balance_history
                INNER JOIN balance
                ON balance_history.balance_id = balance.id
                WHERE balance.update_date_time BETWEEN ? AND ?
            """;
            statement = connection.prepareStatement(query);
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.execute();
            resultSet  = statement.getResultSet();
            while (resultSet.next()) {
                balanceList.add(new Balance(
                        resultSet.getInt("id"),
                        resultSet.getBigDecimal("amount"),
                        ((Timestamp) resultSet.getObject("update_datetime")).toLocalDateTime()
                ));
            }
            connection.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding balances :\n"
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
                connection.close();
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return balanceList;
    }
}
