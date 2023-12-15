package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Enum.CategoryGroup;
import management.wallet.model.TransactionCategories;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionCategoriesDAO {
    DbConnect connect = new DbConnect();
    Connection connection = connect.createConnection();

    public List<TransactionCategories> findAll() {
        List<TransactionCategories> transactionCategories = new ArrayList<>();
        try {
            String query = "SELECT * FROM transaction_categories";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                transactionCategories.add(new TransactionCategories(
                        resultSet.getInt("id"),
                        resultSet.getString("category_name"),
                        (CategoryGroup) resultSet.getObject("category_group")
                ));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all category of transactions :\n"
                    + exception.getMessage()
            );
        }
        return transactionCategories;
    }
    public TransactionCategories findById(int id){
        try {
            String query = "SELECT * FROM transaction_categories WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.getResultSet();

            if(resultSet.next()) {
                return  new TransactionCategories(
                        resultSet.getInt("id"),
                        resultSet.getString("category_name"),
                        (CategoryGroup) resultSet.getObject("category_group")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error occurred while finding the transaction category :\n"
                    + e.getMessage()
            );
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<TransactionCategories> saveAll(List<TransactionCategories> toSave){
        try {
            for (TransactionCategories transactionCategories : toSave){
                if (findById(transactionCategories.getId()) == null ) {
                    String query = """
                        INSERT INTO transaction_categories (category_name, category_group) 
                        VALUES (?, ?)
                    """;
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, transactionCategories.getCategoryName());
                    preparedStatement.setObject(2, transactionCategories.getCategoryGroup());
                } else {
                      update(transactionCategories);
                }
                return toSave;
            }

        } catch (Exception e) {
            System.out.println("Error occurred while saving all transaction categories :\n"
                    + e.getMessage()
            );
            throw new RuntimeException(e);
        }
        return null;
    }
    public TransactionCategories save(TransactionCategories toSave) {
        try {
            if (findById(toSave.getId()) == null ) {
                String query = """
                        INSERT INTO transaction_categories (category_name, category_group) 
                        VALUES (?, ?)
                    """;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, toSave.getCategoryName());
                preparedStatement.setObject(2, toSave.getCategoryGroup());
            } else {
                update(toSave);
            }
            return toSave;
        } catch (Exception e) {
            System.out.println("Error occurred while saving the transaction category :\n"
                    + e.getMessage()
            );
            throw new RuntimeException(e);
        }
    }
    public boolean update(TransactionCategories transactionCategory) {
        String query = """
            UPDATE transaction_categories
            SET category_name = ?, category_group = ?
            WHERE id = ?
        """;
        try (
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, transactionCategory.getCategoryName());
            preparedStatement.setObject(2, transactionCategory.getCategoryGroup());
            preparedStatement.setInt(3, transactionCategory.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.out.println("Error occurred while updating the transaction category :\n"
                    + e.getMessage());
            return false;
        }
    }
    public BigDecimal findIncomeByInterval(int accountId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
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
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, startDateTime);
            statement.setObject(2, endDateTime);
            statement.setInt(3, accountId);
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return resultSet.getBigDecimal("income");
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the income amount :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }
    public BigDecimal findExpenseByInterval(int accountId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
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
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, startDateTime);
            statement.setObject(2, endDateTime);
            statement.setInt(3, accountId);
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return resultSet.getBigDecimal("income");
            }
            statement.close();
            resultSet.close();
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the expense amount :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }
}
