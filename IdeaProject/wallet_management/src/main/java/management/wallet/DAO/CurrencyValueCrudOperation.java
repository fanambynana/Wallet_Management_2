package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.CurrencyValue;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CurrencyValueCrudOperation implements CrudOperation<CurrencyValue> {
    @Override
    public List<CurrencyValue> findAll() {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<CurrencyValue> currencyValueList = new ArrayList<>();
        try {
            String query = "SELECT * FROM currency_value";
            statement = connection.prepareStatement(query);
            statement.execute();
            resultSet = statement.getResultSet();
            connection.close();
            while (resultSet.next()) {
                currencyValueList.add(new CurrencyValue(
                        resultSet.getInt("id"),
                        resultSet.getInt("exchange_source_id"),
                        resultSet.getInt("exchange_destination_id"),
                        resultSet.getBigDecimal("exchange_value"),
                        ((Timestamp) resultSet.getObject("exchange_datetime")).toLocalDateTime()
                ));
            };
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all currency values :\n"
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
        return currencyValueList;
    }

    @Override
    public CurrencyValue findById(int id) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM currency_value WHERE id = ? ";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            resultSet  = statement.getResultSet();
            connection.close();
            if (resultSet.next()) {
                return new CurrencyValue(
                        resultSet.getInt("id"),
                        resultSet.getInt("exchange_source_id"),
                        resultSet.getInt("exchange_destination_id"),
                        resultSet.getBigDecimal("exchange_value"),
                        ((Timestamp) resultSet.getObject("exchange_datetime")).toLocalDateTime()
                );
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the currency value :\n"
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
    public List<CurrencyValue> saveAll(List<CurrencyValue> toSave) {
        List<CurrencyValue> existingList = new ArrayList<>();
        for (CurrencyValue currencyValue : toSave) {
            CurrencyValue saved = save(currencyValue);
            if (saved != null) {
                existingList.add(saved);
            }
        }
        return existingList;
    }

    @Override
    public CurrencyValue save(CurrencyValue toSave) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            if (findById(toSave.getId()) == null) {
                String query = """
                        INSERT INTO currency_value(exchange_source_id, exchange_destination_id, exchange_value, exchange_datetime)
                        VALUES(?, ?, ?, ?)
                    """;
                statement = connection.prepareStatement(query);
                statement.setInt(1, toSave.getExchangeSourceId());
                statement.setInt(2, toSave.getExchangeDestinationId());
                statement.setBigDecimal(3, toSave.getExchangeValue());
                statement.setObject(4, toSave.getExchangeDatetime());
                statement.executeUpdate();
                resultSet = statement.getResultSet();
                connection.close();
                return findById(toSave.getId());
            } else {
                connection.close();
                return update(toSave);
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the currency value :\n"
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
    public CurrencyValue update(CurrencyValue toUpdate) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = """
                        UPDATE currency_value
                        SET
                        exchange_source_id = ?,
                        exchange_destination_id = ?,
                        exchange_value = ?,
                        exchange_datetime = ?
                        WHERE id = ?
                    """;
            statement = connection.prepareStatement(query);
            statement.setInt(1, toUpdate.getExchangeSourceId());
            statement.setInt(2, toUpdate.getExchangeDestinationId());
            statement.setBigDecimal(3, toUpdate.getExchangeValue());
            statement.setObject(4, toUpdate.getExchangeDatetime());
            statement.setInt(5, toUpdate.getId());
            statement.executeUpdate();
            resultSet = statement.getResultSet();
            connection.close();
            return findById(toUpdate.getId());
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating the currency value :\n"
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

    public List<CurrencyValue> findAllByDate(LocalDate date) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<CurrencyValue> currencyValueList = new ArrayList<>();
        try {
            String query = """
                SELECT * FROM currency_value
                WHERE exchage_datetime::date = ?
            """;
            statement = connection.prepareStatement(query);
            statement.setObject(1, date);
            statement.execute();
            resultSet  = statement.getResultSet();
            connection.close();
            while (resultSet.next()) {
                currencyValueList.add(new CurrencyValue(
                        resultSet.getInt("id"),
                        resultSet.getInt("exchange_source_id"),
                        resultSet.getInt("exchange_destination_id"),
                        resultSet.getBigDecimal("exchange_value"),
                        ((Timestamp) resultSet.getObject("exchange_date")).toLocalDateTime()
                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all currency values :\n"
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
        return currencyValueList;
    }
    public BigDecimal findAvgByDate(LocalDate date) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = """
                SELECT avg(exchange_value) as average
                FROM currency_value
                WHERE exchage_datetime::date = ?
            """;
            statement = connection.prepareStatement(query);
            statement.setObject(1, date);
            statement.execute();
            resultSet  = statement.getResultSet();
            connection.close();
            if (resultSet.next()) {
                return (BigDecimal) resultSet.getObject("average");
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the currency value :\n"
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
    public BigDecimal findMinByDate(LocalDate date) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = """
                SELECT min(exchange_value) as average
                FROM currency_value
                WHERE exchage_datetime::date = ?
            """;
            statement = connection.prepareStatement(query);
            statement.setObject(1, date);
            statement.execute();
            resultSet  = statement.getResultSet();
            connection.close();
            if (resultSet.next()) {
                return (BigDecimal) resultSet.getObject("average");
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the currency value :\n"
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
    public BigDecimal findMaxByDate(LocalDate date) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = """
                SELECT max(exchange_value) as average
                FROM currency_value
                WHERE exchage_datetime::date = ?
            """;
            statement = connection.prepareStatement(query);
            statement.setObject(1, date);
            statement.execute();
            resultSet  = statement.getResultSet();
            connection.close();
            if (resultSet.next()) {
                return (BigDecimal) resultSet.getObject("average");
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the currency value :\n"
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

    public CurrencyValue findByDate(LocalDate date) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM currency_value WHERE exchage_datetime = ? ";
            statement = connection.prepareStatement(query);
            statement.setObject(1, date);
            statement.execute();
            resultSet  = statement.getResultSet();
            connection.close();
            if (resultSet.next()) {
                return new CurrencyValue(
                        resultSet.getInt("id"),
                        resultSet.getInt("exchange_source_id"),
                        resultSet.getInt("exchange_destination_id"),
                        resultSet.getBigDecimal("exchange_value"),
                        ((Timestamp) resultSet.getObject("exchange_datetime")).toLocalDateTime()
                );
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the currency value :\n"
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

    public BigDecimal findMedianByDate(LocalDate date) {
        List<CurrencyValue> currencyValueList = findAllByDate(date);
        int mediumIndex;
        int size = currencyValueList.size();
        if (size % 2 == 0) {
            mediumIndex = size / 2;
            return (currencyValueList.get(mediumIndex).getExchangeValue()
                    .add(currencyValueList.get(mediumIndex - 1).getExchangeValue()))
                    .divide(new BigDecimal(2));
        } else {
            mediumIndex = (size - 1) / 2;
            return currencyValueList.get(mediumIndex).getExchangeValue();
        }
    }
}
