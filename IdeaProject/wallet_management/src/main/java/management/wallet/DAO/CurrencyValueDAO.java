package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Currency;
import management.wallet.model.CurrencyValue;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CurrencyValueDAO {
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();

    public CurrencyValue findById(int id) {
        try {
            String query = "SELECT * FROM currency_value WHERE id = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet  = statement.getResultSet();

            if (resultSet.next()) {
                return new CurrencyValue(
                        resultSet.getInt("id"),
                        resultSet.getInt("exchange_source_id"),
                        resultSet.getInt("exchange_destination_id"),
                        resultSet.getBigDecimal("exchange_value"),
                        (LocalDate) resultSet.getObject("exchange_date")
                );
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the currency value :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }
    public CurrencyValue findByDate(LocalDate date) {
        try {
            String query = "SELECT * FROM currency_value WHERE exchage_date = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, date);
            ResultSet resultSet  = statement.getResultSet();

            if (resultSet.next()) {
                return new CurrencyValue(
                        resultSet.getInt("id"),
                        resultSet.getInt("exchange_source_id"),
                        resultSet.getInt("exchange_destination_id"),
                        resultSet.getBigDecimal("exchange_value"),
                        (LocalDate) resultSet.getObject("exchange_date")
                );
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the currency value :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }
    public List<CurrencyValue> findAllByDate(LocalDate date) {
        List<CurrencyValue> currencyValues = new ArrayList<>();
        try {
            String query = """
                SELECT * FROM currency_value
                WHERE exchage_date::date = ?
            """;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, date);
            ResultSet resultSet  = statement.getResultSet();

            while (resultSet.next()) {
                currencyValues.add(new CurrencyValue(
                        resultSet.getInt("id"),
                        resultSet.getInt("exchange_source_id"),
                        resultSet.getInt("exchange_destination_id"),
                        resultSet.getBigDecimal("exchange_value"),
                        (LocalDate) resultSet.getObject("exchange_date")
                ));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all currency values :\n"
                    + exception.getMessage()
            );
        }
        return currencyValues;
    }

    public BigDecimal findAvgByDate(LocalDate date) {
        try {
            String query = """
                SELECT avg(exchange_value) as average
                FROM currency_value
                WHERE exchage_date::date = ?
            """;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, date);
            ResultSet resultSet  = statement.getResultSet();

            if (resultSet.next()) {
                return (BigDecimal) resultSet.getObject("average");
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the currency value :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }

    public BigDecimal findMinByDate(LocalDate date) {
        try {
            String query = """
                SELECT min(exchange_value) as average
                FROM currency_value
                WHERE exchage_date::date = ?
            """;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, date);
            ResultSet resultSet  = statement.getResultSet();

            if (resultSet.next()) {
                return (BigDecimal) resultSet.getObject("average");
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the currency value :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }
    public BigDecimal findMaxByDate(LocalDate date) {
        try {
            String query = """
                SELECT max(exchange_value) as average
                FROM currency_value
                WHERE exchage_date::date = ?
            """;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, date);
            ResultSet resultSet  = statement.getResultSet();

            if (resultSet.next()) {
                return (BigDecimal) resultSet.getObject("average");
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the currency value :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }
}
