package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Currency;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CurrencyCrudOperation implements CrudOperation<Currency>{
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();

    @Override
    public List<Currency> findAll() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Currency> currencyList  = new ArrayList<>();
        try {
            String query = "SELECT * FROM currency";
            statement = connection.prepareStatement(query);
            statement.execute();
            resultSet  = statement.getResultSet();
            while (resultSet.next()) {
                currencyList.add(new Currency(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("code")
                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all currencies :\n"
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
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return currencyList;
    }

    @Override
    public Currency findById(int id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM currency WHERE id = ? ";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            resultSet  = statement.getResultSet();
            if (resultSet.next()) {
                return new Currency(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("code")
                );
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the currency :\n"
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
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return null;
    }

    @Override
    public List<Currency> saveAll(List<Currency> toSave) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Currency> existingList = new ArrayList<>();
        try {
            for (Currency currency : toSave) {
                if (findById(currency.getId()) == null) {
                    String query = """
                        INSERT INTO currency(name, code)
                        VALUES(?, ?)
                    """;
                    statement = connection.prepareStatement(query);
                    statement.setString(1, currency.getName());
                    statement.setString(2, currency.getCode());
                    statement.executeUpdate();
                    resultSet = statement.getResultSet();
                    existingList.add(findById(currency.getId()));
                } else {
                    existingList.add(update(currency));
                }
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving all currencies :\n"
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
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return existingList;
    }

    @Override
    public Currency save(Currency toSave) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            if (findById(toSave.getId()) == null) {
                String query = """
                        INSERT INTO currency(name, code)
                        VALUES(?, ?)
                    """;
                statement = connection.prepareStatement(query);
                statement.setString(1, toSave.getName());
                statement.setString(2, toSave.getCode());
                statement.executeUpdate();
                resultSet = statement.getResultSet();
                return findById(toSave.getId());
            } else {
                return update(toSave);
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the currency :\n"
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
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return null;
    }

    @Override
    public Currency update(Currency toUpdate) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = """
            UPDATE currency
            SET name = ?, code = ?
            WHERE id = ?
        """;
            statement = connection.prepareStatement(query);
            statement.setString(1, toUpdate.getName());
            statement.setString(2, toUpdate.getCode());
            statement.setInt(3, toUpdate.getId());
            statement.executeUpdate();
            resultSet = statement.getResultSet();
            return findById(toUpdate.getId());
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating the currency :\n"
                    + exception.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return null;
    }

    public Currency findByCode(String code) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM currency WHERE code ILIKE ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, code);
            statement.execute();
            resultSet  = statement.getResultSet();
            if (resultSet.next()) {
                return new Currency(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("code")
                );
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the currency :\n"
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
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return null;
    }
}
