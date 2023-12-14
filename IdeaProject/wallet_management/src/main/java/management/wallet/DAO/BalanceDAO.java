package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Balance;
import management.wallet.model.Currency;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BalanceDAO {
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();

    public List<Balance> findAll() {
        List<Balance> balances  = new ArrayList<>();
        try {
            String query = "SELECT * FROM balance";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet  = statement.getResultSet();

            while (resultSet.next()) {
                balances.add(new Balance(
                        resultSet.getInt("id"),
                        resultSet.getBigDecimal("amount"),
                        (LocalDateTime) resultSet.getObject("update_datetime")
                ));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all balances :\n"
                    + exception.getMessage()
            );
        }
        return balances;
    }

    public Balance findById(int id) {
        try {
            String query = "SELECT * FROM balance WHERE id = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet  = statement.getResultSet();

            if (resultSet.next()) {
                return new Balance(
                        resultSet.getInt("id"),
                        resultSet.getBigDecimal("amount"),
                        (LocalDateTime) resultSet.getObject("update_datetime")
                );
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the balance :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }
    public Balance findByDateTime(LocalDateTime dateTime) {
        try {
            String query = "SELECT * FROM balance WHERE update_date_time = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, dateTime);
            ResultSet resultSet  = statement.getResultSet();

            if (resultSet.next()) {
                return new Balance(
                        resultSet.getInt("id"),
                        resultSet.getBigDecimal("amount"),
                        (LocalDateTime) resultSet.getObject("update_datetime")
                );
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the balance :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }
    public List<Balance> findByInterval(LocalDateTime from,LocalDateTime to) {
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
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, from);
            statement.setObject(2, to);
            ResultSet resultSet  = statement.getResultSet();

            while (resultSet.next()) {
                balances.add(new Balance(
                        resultSet.getInt("id"),
                        resultSet.getBigDecimal("amount"),
                        (LocalDateTime) resultSet.getObject("update_datetime")
                ));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding balances :\n"
                    + exception.getMessage()
            );
        }
        return balances;
    }
    public List<Balance> saveAll(List<Balance> toSave) {
        try {
            for (Balance balance : toSave) {
                if (findById(balance.getId()) == null) {
                    String query = """
                        INSERT INTO balance(amount, update_datetime)
                        VALUES(?, ?)
                    """;
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setBigDecimal(1, balance.getAmount());
                    preparedStatement.setObject(2, balance.getUpdateDateTime());
                    preparedStatement.close();
                } else {
                    update(balance);
                }
                return toSave;
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving all balances :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }
    public Balance save(Balance toSave) {
        try {
            if (findById(toSave.getId()) == null) {
                String query = """
                        INSERT INTO balance(amount, update_datetime)
                        VALUES(?, ?)
                    """;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setObject(1, toSave.getAmount());
                preparedStatement.setObject(2, toSave.getUpdateDateTime());
                preparedStatement.close();
            } else {
                update(toSave);
            }
            return toSave;
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the balance :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }
    public boolean update(Balance toUpdate) {
        try {
            String query = """
            UPDATE balance
            SET amount = ?, update_datetime = ?
            WHERE id = ?
        """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, toUpdate.getAmount());
            preparedStatement.setObject(2, toUpdate.getUpdateDateTime());
            preparedStatement.setInt(3, toUpdate.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsUpdated > 0;
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating the balance :\n"
                    + exception.getMessage());
            return false;
        }
    }

    /*
    public Balance findCurrentBalanceWithExchange(int accountId) {
        try {
            String query = """
                SELECT
                ()
                b.id,
                b.amount,
                b.update_datetime
                FROM balance_history bh
                INNER JOIN balance b
                ON bh.balance_id = b.id
                INNER JOIN 
                WHERE b.update_date_time BETWEEN ? AND ?
            """;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, from);
            statement.setObject(2, to);
            ResultSet resultSet  = statement.getResultSet();

            while (resultSet.next()) {
                balances.add(new Balance(
                        resultSet.getInt("id"),
                        resultSet.getBigDecimal("amount"),
                        (LocalDateTime) resultSet.getObject("update_datetime")
                ));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding balances :\n"
                    + exception.getMessage()
            );
        }
    }
    */

}
