package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Balance;
import management.wallet.model.Currency;
import org.springframework.stereotype.Repository;

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
}
