package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.AmountPerCategory;
import management.wallet.model.Enum.CategoryGroup;
import management.wallet.model.Enum.GetCategoryGroup;
import management.wallet.model.TransactionCategories;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionCategoriesCrudOperation implements CrudOperation<TransactionCategories> {
    @Override
    public List<TransactionCategories> findAll() {
        DbConnect connect = new DbConnect();
        Connection connection = connect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<TransactionCategories> transactionCategoriesList = new ArrayList<>();
        try {
            String query = "SELECT * FROM transaction_categories";
            statement = connection.prepareStatement(query);
            statement.execute();
            resultSet = statement.getResultSet();
            connection.close();
            while (resultSet.next()) {
                transactionCategoriesList.add(new TransactionCategories(
                        resultSet.getInt("id"),
                        resultSet.getString("category_name"),
                        GetCategoryGroup.getEnum(resultSet.getString("category_group"))
                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all category of transactions :\n"
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
        return transactionCategoriesList;
    }

    @Override
    public TransactionCategories findById(int id) {
        DbConnect connect = new DbConnect();
        Connection connection = connect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM transaction_categories WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            resultSet = statement.getResultSet();
            connection.close();
            if(resultSet.next()) {
                return  new TransactionCategories(
                        resultSet.getInt("id"),
                        resultSet.getString("category_name"),
                        GetCategoryGroup.getEnum(resultSet.getString("category_group"))
                );
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while finding the transaction category :\n"
                    + e.getMessage()
            );
            // throw new RuntimeException(e);
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
    public List<TransactionCategories> saveAll(List<TransactionCategories> toSave) {
        List<TransactionCategories> existingList = new ArrayList<>();
        for (TransactionCategories transactionCategory : toSave) {
            TransactionCategories saved = save(transactionCategory);
            existingList.add(saved);
        }
        return existingList;
    }

    @Override
    public TransactionCategories save(TransactionCategories toSave) {
        DbConnect connect = new DbConnect();
        Connection connection = connect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            if (findById(toSave.getId()) == null ) {
                String query = String.format("""
                    INSERT INTO transaction_categories (category_name, category_group)
                    VALUES (?, '%s')
                """, toSave.getCategoryGroup().toString().toLowerCase());
                statement = connection.prepareStatement(query);
                statement.setString(1, toSave.getCategoryName());
                statement.executeUpdate();
                resultSet = statement.getResultSet();
                connection.close();
                return findById(toSave.getId());
            } else {
                connection.close();
                return update(toSave);
            }
        } catch (Exception e) {
            System.out.println("Error occurred while saving the transaction category :\n"
                    + e.getMessage()
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
    public TransactionCategories update(TransactionCategories toUpdate) {
        DbConnect connect = new DbConnect();
        Connection connection = connect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = String.format("""
                UPDATE transaction_categories
                SET category_name = ?, category_group = '%s'
                WHERE id = ?
            """, toUpdate.getCategoryGroup().toString().toLowerCase());
            statement = connection.prepareStatement(query);
            statement.setString(1, toUpdate.getCategoryName());
            statement.setInt(2, toUpdate.getId());
            statement.executeUpdate();
            resultSet = statement.getResultSet();
            connection.close();
            return findById(toUpdate.getId());
        } catch (SQLException e) {
            System.out.println("Error occurred while updating the transaction category :\n"
                    + e.getMessage());
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

    public BigDecimal findIncomeByInterval(int accountId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        DbConnect connect = new DbConnect();
        Connection connection = connect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = """
                SELECT sum(t.amount) as income
                FROM \"transaction\" t
                INNER JOIN transaction_category c
                ON t.category_id = c.id
                INNER JOIN \"account\" a
                ON t.account_id = a.id
                WHERE c.category_group = 'income'
                AND t.transaction_date >= ?
                AND t.transaction_date <= ?
                AND a.id = ?
            """;
            statement = connection.prepareStatement(query);
            statement.setObject(1, startDateTime);
            statement.setObject(2, endDateTime);
            statement.setInt(3, accountId);
            statement.execute();
            resultSet = statement.getResultSet();
            connection.close();
            if (resultSet.next()) {
                return resultSet.getBigDecimal("income");
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the income amount :\n"
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
    public BigDecimal findExpenseByInterval(int accountId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        DbConnect connect = new DbConnect();
        Connection connection = connect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = """
                SELECT sum(t.amount) as income
                FROM \"transaction\" t
                INNER JOIN transaction_category c
                ON t.category_id = c.id
                INNER JOIN \"account\" a
                ON t.account_id = a.id
                WHERE c.category_group = 'expense'
                AND t.transaction_date >= ?
                AND t.transaction_date <= ?
                AND a.id = ?
            """;
            statement = connection.prepareStatement(query);
            statement.setObject(1, startDateTime);
            statement.setObject(2, endDateTime);
            statement.setInt(3, accountId);
            statement.execute();
            resultSet = statement.getResultSet();
            connection.close();
            if (resultSet.next()) {
                return resultSet.getBigDecimal("income");
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the expense amount :\n"
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
    public List<AmountPerCategory> findByIdAccount(int accountId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        DbConnect connect = new DbConnect();
        Connection connection = connect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<AmountPerCategory> amountPerCategoryList = new ArrayList<>();
        try {
            String query = """
                SELECT c.category_name, sum(
                    CASE WHEN t.amount IS NULL THEN 0 ELSE t.amount
                ) as amount
                FROM \"transaction\" t
                RIGHT JOIN transaction_category c
                ON t.category_id = c.id
                INNER JOIN \"account\" a
                ON t.account_id = a.id
                WHERE c.category_name ILIKE 'Restaurant'
                AND t.transaction_date >= ?
                AND t.transaction_date <= ?
                AND a.id = ?
                GROUP BY c.category_name
            """;
            statement = connection.prepareStatement(query);
            statement.setObject(1, startDateTime);
            statement.setObject(2, endDateTime);
            statement.setInt(3, accountId);
            statement.execute();
            resultSet = statement.getResultSet();
            connection.close();
            while (resultSet.next()) {
                amountPerCategoryList.add(new AmountPerCategory(
                        resultSet.getString("category_name"),
                        resultSet.getBigDecimal("amount")
                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the expense amount :\n"
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
        return amountPerCategoryList;
    }
}
