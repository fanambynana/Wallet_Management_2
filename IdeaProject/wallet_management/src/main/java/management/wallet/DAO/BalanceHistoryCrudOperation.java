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
    @Override
    public List<BalanceHistory> findAll() {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
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
            connection.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all balance histories :\n"
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
        return balanceHistories;
    }

    @Override
    public BalanceHistory findById(int id) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM balance_history WHERE id = ? ";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            resultSet  = statement.getResultSet();
            connection.close();
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
    public List<BalanceHistory> saveAll(List<BalanceHistory> toSave) {
        List<BalanceHistory> existingList = new ArrayList<>();
        for (BalanceHistory balanceHistory : toSave) {
            BalanceHistory saved = save(balanceHistory);
            if (saved != null) {
                existingList.add(saved);
            }
        }
        return existingList;
    }

    @Override
    public BalanceHistory save(BalanceHistory toSave) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            if (findById(toSave.getId()) == null) {
                String query = """
                        INSERT INTO balance_history(balance_id, account_id, datetime)
                        VALUES(?, ?, ?)
                    """;
                statement = connection.prepareStatement(query);
                statement.setInt(1, toSave.getBalanceId());
                statement.setInt(2, toSave.getAccountId());
                statement.setObject(3, toSave.getDatetime());
                statement.execute();
                resultSet = statement.getResultSet();
                connection.close();
                return findById(toSave.getId());
            } else {
                connection.close();
                return update(toSave);
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the balance history :\n"
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
    public BalanceHistory update(BalanceHistory toUpdate) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = """
                UPDATE balance_history
                SET balance_id = ?, account_id = ?, datetime = ?
                WHERE id = ?
            """;
            statement = connection.prepareStatement(query);
            statement.setInt(1, toUpdate.getBalanceId());
            statement.setInt(2, toUpdate.getAccountId());
            statement.setObject(3, toUpdate.getDatetime());
            statement.setInt(4, toUpdate.getId());
            statement.executeUpdate();
            resultSet = statement.getResultSet();
            connection.close();
            return findById(toUpdate.getId());
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating the balance history :\n"
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

    public BalanceHistory findByIntervalDateTime(LocalDateTime from, LocalDateTime to) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
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
            connection.close();
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
}
