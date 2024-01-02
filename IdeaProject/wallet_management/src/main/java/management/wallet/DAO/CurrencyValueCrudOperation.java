package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.CurrencyValue;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CurrencyValueCrudOperation implements CrudOperation<CurrencyValue> {
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();

    @Override
    public List<CurrencyValue> findAll() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<CurrencyValue> currencyValues = new ArrayList<>();
        try {
            String query = "SELECT * FROM currency_value";
            statement = connection.prepareStatement(query);
            statement.execute();
            resultSet  = statement.getResultSet();

            while (resultSet.next()) {
                currencyValues.add(new CurrencyValue(
                        resultSet.getInt("id"),
                        resultSet.getInt("exchange_source_id"),
                        resultSet.getInt("exchange_destination_id"),
                        resultSet.getBigDecimal("exchange_value"),
                        ((Date) resultSet.getObject("exchange_date")).toLocalDate()
                ));
            };
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all currency values :\n"
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
        return currencyValues;
    }

    @Override
    public CurrencyValue findById(int id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM currency_value WHERE id = ? ";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            resultSet  = statement.getResultSet();

            if (resultSet.next()) {
                return new CurrencyValue(
                        resultSet.getInt("id"),
                        resultSet.getInt("exchange_source_id"),
                        resultSet.getInt("exchange_destination_id"),
                        resultSet.getBigDecimal("exchange_value"),
                        ((Date) resultSet.getObject("exchange_date")).toLocalDate()
                );
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the currency value :\n"
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
    public List<CurrencyValue> saveAll(List<CurrencyValue> toSave) {
        return null;
    }

    @Override
    public CurrencyValue save(CurrencyValue toSave) {
        return null;
    }

    @Override
    public boolean update(CurrencyValue toUpdate) {
        return false;
    }

    public List<CurrencyValue> findAllByDate(LocalDate date) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<CurrencyValue> currencyValues = new ArrayList<>();
        try {
            String query = """
                SELECT * FROM currency_value
                WHERE exchage_date::date = ?
            """;
            statement = connection.prepareStatement(query);
            statement.setObject(1, date);
            statement.execute();
            resultSet  = statement.getResultSet();

            while (resultSet.next()) {
                currencyValues.add(new CurrencyValue(
                        resultSet.getInt("id"),
                        resultSet.getInt("exchange_source_id"),
                        resultSet.getInt("exchange_destination_id"),
                        resultSet.getBigDecimal("exchange_value"),
                        ((Date) resultSet.getObject("exchange_date")).toLocalDate()
                ));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all currency values :\n"
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
        return currencyValues;
    }
    public BigDecimal findAvgByDate(LocalDate date) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = """
                SELECT avg(exchange_value) as average
                FROM currency_value
                WHERE exchage_date::date = ?
            """;
            statement = connection.prepareStatement(query);
            statement.setObject(1, date);
            statement.execute();
            resultSet  = statement.getResultSet();

            if (resultSet.next()) {
                return (BigDecimal) resultSet.getObject("average");
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the currency value :\n"
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
    public BigDecimal findMinByDate(LocalDate date) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = """
                SELECT min(exchange_value) as average
                FROM currency_value
                WHERE exchage_date::date = ?
            """;
            statement = connection.prepareStatement(query);
            statement.setObject(1, date);
            statement.execute();
            resultSet  = statement.getResultSet();

            if (resultSet.next()) {
                return (BigDecimal) resultSet.getObject("average");
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the currency value :\n"
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
    public BigDecimal findMaxByDate(LocalDate date) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = """
                SELECT max(exchange_value) as average
                FROM currency_value
                WHERE exchage_date::date = ?
            """;
            statement = connection.prepareStatement(query);
            statement.setObject(1, date);
            statement.execute();
            resultSet  = statement.getResultSet();

            if (resultSet.next()) {
                return (BigDecimal) resultSet.getObject("average");
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the currency value :\n"
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

    public CurrencyValue findByDate(LocalDate date) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM currency_value WHERE exchage_date = ? ";
            statement = connection.prepareStatement(query);
            statement.setObject(1, date);
            statement.execute();
            resultSet  = statement.getResultSet();

            if (resultSet.next()) {
                return new CurrencyValue(
                        resultSet.getInt("id"),
                        resultSet.getInt("exchange_source_id"),
                        resultSet.getInt("exchange_destination_id"),
                        resultSet.getBigDecimal("exchange_value"),
                        ((Date) resultSet.getObject("exchange_date")).toLocalDate()
                );
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the currency value :\n"
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

    public BigDecimal findMedianByDate(LocalDate date) {
        List<CurrencyValue> currencyValues = findAllByDate(date);
        int mediumIndex;
        int size = currencyValues.size();
        if (size % 2 == 0) {
            mediumIndex = size / 2;
            return (currencyValues.get(mediumIndex).getExchangeValue()
                    .add(currencyValues.get(mediumIndex - 1).getExchangeValue()))
                    .divide(new BigDecimal(2));
        } else {
            mediumIndex = (size - 1) / 2;
            return currencyValues.get(mediumIndex).getExchangeValue();
        }
    }
}
