package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Enum.GetTransactionType;
import management.wallet.model.Enum.TransactionType;
import management.wallet.model.TransactionSave;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionCrudOperation implements CrudOperation<TransactionSave>{
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();

    @Override
    public List<TransactionSave> findAll() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<TransactionSave> transactions = new ArrayList<>();
        try {
            String query = "SELECT * FROM transaction";
            statement = connection.prepareStatement(query);
            statement.execute();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                transactions.add(new TransactionSave(
                        resultSet.getInt("id"),
                        resultSet.getString("label"),
                        resultSet.getBigDecimal("amount"),
                        ((Timestamp) resultSet.getObject("transaction_date")).toLocalDateTime(),
                        GetTransactionType.getEnum(resultSet.getString("transaction_type")),
                        resultSet.getInt("category_id"),
                        resultSet.getInt("account_id")
                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding all transactions :\n"
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
        return transactions;
    }

    @Override
    public TransactionSave findById(int id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM transaction WHERE id = ? ";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return new TransactionSave(
                        resultSet.getInt("id"),
                        resultSet.getString("label"),
                        resultSet.getBigDecimal("amount"),
                        ((Timestamp) resultSet.getObject("transaction_date")).toLocalDateTime(),
                        GetTransactionType.getEnum(resultSet.getString("transaction_type")),
                        resultSet.getInt("category_id"),
                        resultSet.getInt("account_id")
                );
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the transaction :\n"
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
    public List<TransactionSave> saveAll(List<TransactionSave> toSave) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<TransactionSave> existingList = new ArrayList<>();
        try {
            for (TransactionSave transaction : toSave) {
                if (findById(transaction.getId()) == null) {
                    String query = """
                        INSERT INTO transaction (label, amount, transaction_date, transaction_type, category_id, account_id)
                        VALUES(?, ?, ?, ?, ?, ?)
                    """;
                    statement = connection.prepareStatement(query);
                    statement.setString(1, transaction.getLabel());
                    statement.setBigDecimal(2, transaction.getAmount());
                    statement.setObject(3, transaction.getTransactionDate());
                    statement.setObject(4, transaction.getTransactionType());
                    statement.setObject(5, transaction.getTransactionCategoryId());
                    statement.setInt(6, transaction.getAccountId());
                    statement.executeUpdate();
                    resultSet = statement.getResultSet();
                    existingList.add(findById(transaction.getId()));
                } else {
                    existingList.add(update(transaction));
                }
                return existingList;
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving all transactions :\n"
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
    public TransactionSave save(TransactionSave toSave) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            if (findById(toSave.getId()) == null) {
                String query = String.format("""
                    INSERT INTO transaction (label, amount, transaction_date, transaction_type, category_id, account_id)
                    VALUES(?, ?, ?, '%s', ?, ?)
                """, toSave.getTransactionType().toString().toLowerCase());
                statement = connection.prepareStatement(query);
                statement.setString(1, toSave.getLabel());
                statement.setBigDecimal(2, toSave.getAmount());
                statement.setObject(3, toSave.getTransactionDate());
                statement.setObject(4, toSave.getTransactionCategoryId());
                statement.setInt(5, toSave.getAccountId());
                statement.executeUpdate();
                resultSet = statement.getResultSet();
                return findById(toSave.getId());
            } else {
                return update(toSave);
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while saving the transaction :\n"
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
    public TransactionSave update(TransactionSave toUpdate) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = String.format("""
                UPDATE transaction
                SET label = ?, amount = ?,
                transaction_date = ?, transaction_type = '%s',
                category_id = ?, account_id = ?
                WHERE id = ?
            """, toUpdate.getTransactionType().toString().toLowerCase());
            statement = connection.prepareStatement(query);
            statement.setString(1, toUpdate.getLabel());
            statement.setBigDecimal(2, toUpdate.getAmount());
            statement.setTimestamp(3, Timestamp.valueOf(toUpdate.getTransactionDate()));
            statement.setObject(4, toUpdate.getTransactionCategoryId());
            statement.setInt(5, toUpdate.getAccountId());
            statement.setInt(6, toUpdate.getId());
            statement.executeUpdate();
            resultSet = statement.getResultSet();
        } catch (SQLException exception) {
            System.out.println("Error occurred while updating the transaction :\n"
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
        return findById(toUpdate.getId());
    }

    public List<TransactionSave> findByAccountId(int id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<TransactionSave> transactionList = new ArrayList<>();
        try {
            String query = "SELECT * FROM transaction WHERE account_id = ? ";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            resultSet = statement.getResultSet();
            while (resultSet.next()) {
                transactionList.add(new TransactionSave(
                        resultSet.getInt("id"),
                        resultSet.getString("label"),
                        resultSet.getBigDecimal("amount"),
                        ((Timestamp) resultSet.getObject("transaction_date")).toLocalDateTime(),
                        GetTransactionType.getEnum(resultSet.getString("transaction_type")),
                        resultSet.getInt("category_id"),
                        resultSet.getInt("account_id")
                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error occurred while finding the transaction :\n"
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
        return transactionList;
    }
}
