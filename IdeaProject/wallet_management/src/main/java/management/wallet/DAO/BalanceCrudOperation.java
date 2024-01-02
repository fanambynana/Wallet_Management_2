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
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();

    BalanceCrudOperation balanceCrudOperation;
    AccountCrudOperation accountCrudOperation;

    @Override
    public List<Balance> findAll() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<Balance> balances  = new ArrayList<>();
        try {
            String query = "SELECT * FROM balance";
            statement = connection.prepareStatement(query);
            statement.execute();
            resultSet  = statement.getResultSet();
            while (resultSet.next()) {
                balances.add(new Balance(
                        resultSet.getInt("id"),
                        resultSet.getBigDecimal("amount"),
                        ((Timestamp) resultSet.getObject("update_datetime")).toLocalDateTime()
                        // format : timestamp.toLocalDateTime()
                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all balances :\n"
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
        return balances;
    }

    @Override
    public Balance findById(int id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT * FROM balance WHERE id = ? ";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            resultSet  = statement.getResultSet();

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
    public List<Balance> saveAll(List<Balance> toSave) {
        PreparedStatement statement = null;

        try {
            for (Balance balance : toSave) {
                if (findById(balance.getId()) == null) {
                    String query = """
                        INSERT INTO balance(amount, update_datetime)
                        VALUES(?, ?)
                    """;
                    statement = connection.prepareStatement(query);
                    statement.setBigDecimal(1, balance.getAmount());
                    statement.setObject(2, balance.getUpdateDateTime());
                    statement.executeUpdate();
                } else {
                    update(balance);
                }
                return toSave;
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving all balances :\n"
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
    public Balance save(Balance toSave) {
        PreparedStatement statement = null;

        try {
            if (findById(toSave.getId()) == null) {
                String query = """
                        INSERT INTO balance(amount, update_datetime)
                        VALUES(?, ?)
                    """;
                statement = connection.prepareStatement(query);
                statement.setObject(1, toSave.getAmount());
                statement.setObject(2, toSave.getUpdateDateTime());
                statement.executeUpdate();
            } else {
                update(toSave);
            }
            return toSave;
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the balance :\n"
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
    public boolean update(Balance toUpdate) {
        PreparedStatement statement = null;
        try {
            String query = """
            UPDATE balance
            SET amount = ?, update_datetime = ?
            WHERE id = ?
        """;
            statement = connection.prepareStatement(query);
            statement.setObject(1, toUpdate.getAmount());
            statement.setObject(2, toUpdate.getUpdateDateTime());
            statement.setInt(3, toUpdate.getId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating the balance :\n"
                    + exception.getMessage());
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

    public Balance findByDateTime(LocalDateTime dateTime) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM balance WHERE update_date_time = ? ";
            statement = connection.prepareStatement(query);
            statement.setObject(1, dateTime);
            statement.execute();
            resultSet = statement.getResultSet();

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
            if(statement != null && resultSet != null){
                try {
                    statement.close();
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println("Error while closing :\n"
                        + e.getMessage()
                    );

                    /*
                    e.printStackTrace();
                    throw new RuntimeException(e);
                     */
                }
            }
        }
        return null;
    }
    public Balance findCurrentBalance(int accountId) {
        return balanceCrudOperation.findById(
                accountCrudOperation.findById(accountId).getBalanceId()
        );
    }
    public List<Balance> findByInterval(LocalDateTime from, LocalDateTime to) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Balance> balances = new ArrayList<>();
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
                balances.add(new Balance(
                        resultSet.getInt("id"),
                        resultSet.getBigDecimal("amount"),
                        ((Timestamp) resultSet.getObject("update_datetime")).toLocalDateTime()
                ));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding balances :\n"
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
        return balances;
    }
}
