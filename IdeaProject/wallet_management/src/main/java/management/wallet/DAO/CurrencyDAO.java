package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Currency;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CurrencyDAO {
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();

    public List<Currency> findAll() {
        List<Currency> currencies  = new ArrayList<>();
        try {
            String query = "SELECT * FROM currency";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet  = statement.getResultSet();

            while (resultSet.next()) {
                currencies.add(new Currency(
                        resultSet.getInt("id"),
                        resultSet.getString("uname"),
                        resultSet.getString("code")
                ));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all currencies :\n"
                    + exception.getMessage()
            );
        }
        return currencies;
    }

    public Currency findById(int id) {
        try {
            String query = "SELECT * FROM currency WHERE id = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet  = statement.getResultSet();

            if (resultSet.next()) {
                return new Currency(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("code")
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

    public Currency findByCode(String code) {
        try {
            String query = "SELECT * FROM currency WHERE code = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet  = statement.getResultSet();

            if (resultSet.next()) {
                return new Currency(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("code")
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

    public List<Currency> saveAll(List<Currency> toSave) {
        try {
            for (Currency currency : toSave) {
                if (findById(currency.getId()) == null || findByCode(currency.getCode()) == null) {
                    String query = """
                        INSERT INTO currency(name, code)
                        VALUES(?, ?)
                    """;
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, currency.getName());
                    preparedStatement.setString(2, currency.getCode());
                    preparedStatement.close();
                } else {
                    update(currency);
                }
                return toSave;
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving all currencies :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }

    public Currency save(Currency toSave) {
        try {
            if (findById(toSave.getId()) == null || findByCode(toSave.getCode()) == null) {
                String query = """
                        INSERT INTO currency(name, code)
                        VALUES(?, ?)
                    """;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, toSave.getName());
                preparedStatement.setString(2, toSave.getCode());
                preparedStatement.close();
            } else {
                update(toSave);
            }
            return toSave;
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the currency :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }
    public boolean update (Currency currencyUpdated) {
        try {
            String query = """
            UPDATE currency
            SET name = ?, code = ?
            WHERE id = ?
        """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, currencyUpdated.getName());
            preparedStatement.setString(2, currencyUpdated.getCode());
            preparedStatement.setInt(3, currencyUpdated.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsUpdated > 0;
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating the currency :\n"
                    + exception.getMessage());
            return false;
        }
    }
}
