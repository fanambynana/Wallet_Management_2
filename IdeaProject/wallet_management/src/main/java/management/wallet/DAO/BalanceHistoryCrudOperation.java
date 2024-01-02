package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.BalanceHistory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BalanceHistoryCrudOperation implements CrudOperation<BalanceHistory> {
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();

    @Override
    public List<BalanceHistory> findAll() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<BalanceHistory> balanceHistories  = new ArrayList<>();
        try {
            String query = "SELECT * FROM balance_history";
            statement = connection.prepareStatement(query);
            statement.execute();
            resultSet  = statement.getResultSet();

            while (resultSet.next()) {
                balanceHistories.add(new BalanceHistory(
                        resultSet.getInt("id"),
                        resultSet.getInt("balance_id"),
                        resultSet.getInt("account_id"),
                        ((Timestamp) resultSet.getObject("datetime")).toLocalDateTime()
                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all balance histories :\n"
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
        return balanceHistories;
    }

    @Override
    public BalanceHistory findById(int id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM balance_history WHERE id = ? ";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            resultSet  = statement.getResultSet();

            if (resultSet.next()) {
                return new BalanceHistory(
                        resultSet.getInt("id"),
                        resultSet.getInt("balance_id"),
                        resultSet.getInt("account_id"),
                        ((Timestamp) resultSet.getObject("datetime")).toLocalDateTime()
                );
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the balance history :\n"
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
    public List<BalanceHistory> saveAll(List<BalanceHistory> toSave) {
        PreparedStatement statement = null;
        try {
            for (BalanceHistory history : toSave) {
                if (findById(history.getId()) == null) {
                    String query = """
                        INSERT INTO balance_history(balance_id, account_id, datetime)
                        VALUES(?, ?, ?)
                    """;
                    statement = connection.prepareStatement(query);
                    statement.setInt(1, history.getBalanceId());
                    statement.setInt(2, history.getAccountId());
                    statement.setObject(3, history.getDateTime());
                    statement.executeUpdate();
                } else {
                    update(history);
                }
                return toSave;
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving all balance histories :\n"
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
    public BalanceHistory save(BalanceHistory toSave) {
        PreparedStatement statement = null;
        try {
            if (findById(toSave.getId()) == null) {
                String query = """
                        INSERT INTO balance_history(balance_id, account_id, datetime)
                        VALUES(?, ?, ?)
                    """;
                statement = connection.prepareStatement(query);
                statement.setInt(1, toSave.getBalanceId());
                statement.setInt(2, toSave.getAccountId());
                statement.setObject(3, toSave.getDateTime());
                statement.execute();
            } else {
                update(toSave);
            }
            return toSave;
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the balance history :\n"
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
    public boolean update(BalanceHistory toUpdate) {
        PreparedStatement statement = null;
        try {
            String query = """
                UPDATE balance_history
                SET balance_id = ?, account_id = ?, datetime = ?
                WHERE id = ?
            """;
            statement = connection.prepareStatement(query);
            statement.setInt(1, toUpdate.getBalanceId());
            statement.setInt(2, toUpdate.getAccountId());
            statement.setObject(3, toUpdate.getDateTime());
            statement.setInt(4, toUpdate.getId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating the balance history :\n"
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

    public BalanceHistory findByIntervalDateTime(LocalDateTime from, LocalDateTime to) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = """
                SELECT * FROM balance_history WHERE datetime BETWEEN ? AND ?
            """;
            statement = connection.prepareStatement(query);
            statement.setObject(1, from);
            statement.setObject(1, to);
            statement.execute();
            resultSet  = statement.getResultSet();

            if (resultSet.next()) {
                return new BalanceHistory(
                        resultSet.getInt("id"),
                        resultSet.getInt("balance_id"),
                        resultSet.getInt("account_id"),
                        ((Timestamp) resultSet.getObject("datetime")).toLocalDateTime()
                );
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the balance history :\n"
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
}
