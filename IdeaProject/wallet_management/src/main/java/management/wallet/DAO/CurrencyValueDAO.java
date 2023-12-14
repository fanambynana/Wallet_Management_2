package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Currency;
import management.wallet.model.CurrencyValue;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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
            System.out.println("Error occurred while finding the currency :\n"
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
            System.out.println("Error occurred while finding the currency :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }
}
