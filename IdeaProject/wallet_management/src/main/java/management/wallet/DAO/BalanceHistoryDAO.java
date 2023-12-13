package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Balance;
import management.wallet.model.BalanceHistory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BalanceHistoryDAO {
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();

    public List<BalanceHistory> findAll() {
        List<BalanceHistory> balanceHistories  = new ArrayList<>();
        try {
            String query = "SELECT * FROM balance_history";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet  = statement.getResultSet();

            while (resultSet.next()) {
                balanceHistories.add(new BalanceHistory(
                        resultSet.getInt("id"),
                        resultSet.getInt("balance_id"),
                        resultSet.getInt("account_id"),
                        (LocalDateTime) resultSet.getObject("datetime")
                ));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all balance histories :\n"
                    + exception.getMessage()
            );
        }
        return balanceHistories;
    }

    public BalanceHistory findById(int id) {
        try {
            String query = "SELECT * FROM balance_history WHERE id = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet  = statement.getResultSet();

            if (resultSet.next()) {
                return new BalanceHistory(
                        resultSet.getInt("id"),
                        resultSet.getInt("balance_id"),
                        resultSet.getInt("account_id"),
                        (LocalDateTime) resultSet.getObject("datetime")
                );
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the balance history :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }

    public List<BalanceHistory> saveAll(List<BalanceHistory> toSave) {
        try {
            for (BalanceHistory history : toSave) {
                if (findById(history.getId()) == null) {
                    String query = """
                        INSERT INTO balance_history(balance_id, account_id, datetime)
                        VALUES(?, ?, ?)
                    """;
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, history.getBalanceId());
                    preparedStatement.setInt(2, history.getAccountId());
                    preparedStatement.setObject(3, history.getDateTime());
                    preparedStatement.close();
                } else {
                    update(history);
                }
                return toSave;
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving all balance histories :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }

    public BalanceHistory save(BalanceHistory toSave) {
        try {
            if (findById(toSave.getId()) == null) {
                String query = """
                        INSERT INTO balance_history(balance_id, account_id, datetime)
                        VALUES(?, ?, ?)
                    """;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, toSave.getBalanceId());
                preparedStatement.setInt(2, toSave.getAccountId());
                preparedStatement.setObject(3, toSave.getDateTime());
                preparedStatement.close();
            } else {
                update(toSave);
            }
            return toSave;
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the balance history :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }

    public boolean update(BalanceHistory toUpdate) {
        try {
            String query = """
            UPDATE balance_history
            SET balance_id = ?, account_id = ?, datetime = ?
            WHERE id = ?
        """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, toUpdate.getBalanceId());
            preparedStatement.setInt(2, toUpdate.getAccountId());
            preparedStatement.setObject(3, toUpdate.getDateTime());
            preparedStatement.setInt(4, toUpdate.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsUpdated > 0;
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating the balance history :\n"
                    + exception.getMessage());
            return false;
        }
    }
}
