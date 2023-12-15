package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Enum.SpecificCategories;
import management.wallet.model.TransactionCategories;
import org.springframework.stereotype.Repository;

import java.sql.*;
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
                        (SpecificCategories) resultSet.getObject("Specific_Categories")
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
                        (SpecificCategories) resultSet.getObject("specific_categories")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<TransactionCategories> saveAll(List<TransactionCategories> toSave){

        try {
            for (TransactionCategories transactionCategories : toSave){
                if(findById(transactionCategories.getId()) == null ) {
                    String query = """
                    INSERT INTO transaction_categories (category_name, specific_categories) 
                    VALUES (?, ?)
                    """;
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, transactionCategories.getCategory_name());
                    preparedStatement.setObject(2, transactionCategories.getSpecific_Categories());

                } else {
                      Update(transactionCategories);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
    public boolean Update(TransactionCategories transactionCategory) {
        String query = "UPDATE transaction_categories SET category_name = ?, specific_categories = ? WHERE id = ?";
        try (
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, transactionCategory.getCategory_name());
            preparedStatement.setObject(2, transactionCategory.getSpecific_Categories());
            preparedStatement.setInt(3, transactionCategory.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.out.println("Error occurred while updating the transactionCategories :\n"
                    + e.getMessage());
            return false;
        }
    }


}
