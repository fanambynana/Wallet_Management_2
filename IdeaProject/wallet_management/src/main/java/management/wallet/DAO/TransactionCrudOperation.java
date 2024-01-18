package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.*;
import management.wallet.model.Enum.GetTransactionType;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionCrudOperation implements CrudOperation<Transaction>{
    AccountCrudOperation accountCrudOperation = new AccountCrudOperation();
    CurrencyCrudOperation currencyCrudOperation = new CurrencyCrudOperation();
    BalanceCrudOperation balanceCrudOperation = new BalanceCrudOperation();
    BalanceHistoryCrudOperation balanceHistoryCrudOperation = new BalanceHistoryCrudOperation();
    TransferHistoryCrudOperation transferHistoryCrudOperation = new  TransferHistoryCrudOperation();
    CurrencyValueCrudOperation currencyValueCrudOperation = new CurrencyValueCrudOperation();

    @Override
    public List<Transaction> findAll() {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Transaction> transactions = new ArrayList<>();
        try {
            String query = "SELECT * FROM transaction";
            statement = connection.prepareStatement(query);
            statement.execute();
            resultSet = statement.getResultSet();
            connection.close();
            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getString("label"),
                        resultSet.getBigDecimal("amount"),
                        ((Timestamp) resultSet.getObject("transaction_date")).toLocalDateTime(),
                        GetTransactionType.getEnum(resultSet.getString("transaction_type")),
                        resultSet.getInt("transaction_category_id"),
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
                connection.close();
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return transactions;
    }

    @Override
    public Transaction findById(int id) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM transaction WHERE id = ? ";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            resultSet = statement.getResultSet();
            connection.close();
            if (resultSet.next()) {
                return new Transaction(
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
    public List<Transaction> saveAll(List<Transaction> toSave) {
        List<Transaction> existingList = new ArrayList<>();
        for (Transaction transaction : toSave) {
            Transaction saved  = save(transaction);
            if (saved != null) {
                existingList.add(saved);
            }
        }
        return existingList;
    }

    @Override
    public Transaction save(Transaction toSave) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
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
                connection.close();
                return findById(toSave.getId());
            } else {
                connection.close();
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
    public Transaction update(Transaction toUpdate) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
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
            connection.close();
            return findById(toUpdate.getId());
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
                connection.close();
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return null;
    }

    public List<Transaction> findByAccountId(int id) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Transaction> transactionList = new ArrayList<>();
        try {
            String query = "SELECT * FROM transaction WHERE account_id = ? ";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            resultSet = statement.getResultSet();
            connection.close();
            while (resultSet.next()) {
                transactionList.add(new Transaction(
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
                connection.close();
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return transactionList;
    }

    public PreparedStatement beginTransactional() {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        try {
            String query = "BEGIN;";
            return connection.prepareStatement(query);
        } catch (SQLException sqlException) {
            System.out.println("Error occurred :\n"
                    + sqlException.getMessage()
            );
        }
        return null;
    }
    public PreparedStatement commitTransactional() {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        try {
            String query = "COMMIT;";
            return connection.prepareStatement(query);
        } catch (SQLException sqlException) {
            System.out.println("Error occurred :\n"
                    + sqlException.getMessage()
            );
        }
        return null;
    }
    public PreparedStatement rollbackTransactional() {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        try {
            String query = "ROLLBACK;";
            return connection.prepareStatement(query);
        } catch (SQLException sqlException) {
            System.out.println("Error occurred :\n"
                    + sqlException.getMessage()
            );
        }
        return null;
    }

    public Account makeTransaction(int accountId, Transaction transaction) {
        PreparedStatement beginStatement = null;
        PreparedStatement commitStatement = null;
        PreparedStatement rollbackStatement = null;
        try {
            beginStatement = beginTransactional();
            beginStatement.execute();
            Account account = accountCrudOperation.findById(accountId);
            Transaction transactionSaved = save(transaction);
            Balance balanceSaved = balanceCrudOperation.save(new Balance(
                    0,
                    transactionSaved.getAmount(),
                    LocalDateTime.now()
            ));
            BalanceHistory balanceHistorySaved = balanceHistoryCrudOperation.save(new BalanceHistory(
                    0,
                    balanceSaved.getId(),
                    account.getId(),
                    LocalDateTime.now()
            ));
            Account accountUpdated = accountCrudOperation.updateBalanceIdById(accountId, balanceSaved.getId());
            if (
                    transactionSaved != null
                    &&
                    balanceSaved != null
                    &&
                    balanceHistorySaved  != null
                    &&
                    accountUpdated != null
            ) {
                commitStatement = commitTransactional();
                commitStatement.execute();
                return new Account(
                        account.getId(),
                        account.getAccountName(),
                        balanceSaved.getId(),
                        account.getCurrencyId(),
                        account.getAccountType()
                );
            } else {
                rollbackStatement = rollbackTransactional();
                rollbackStatement.execute();
            }
        } catch (Exception exception) {
            System.out.println("Error occurred while making the transaction :\n"
                    + exception.getMessage()
            );
        } finally {
            try {
                if (beginStatement != null) {
                    beginStatement.close();
                }
                if (commitStatement != null) {
                    commitStatement.close();
                }
                if (rollbackStatement != null) {
                    rollbackStatement.close();
                }
            } catch (Exception e) {
                System.out.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return null;
    }
    public Account makeTransactionWithExchange(int creditAccountId, Transaction transaction) {
        PreparedStatement beginStatement = null;
        PreparedStatement commitStatement = null;
        PreparedStatement rollbackStatement = null;
        try {
            beginStatement = beginTransactional();
            beginStatement.execute();
            Transaction transactionSaved = save(transaction);

            Account debitAccount = accountCrudOperation.findById(transactionSaved.getAccountId());
            Account creditAccount = accountCrudOperation.findById(creditAccountId);

            Balance currentDebitBalance = balanceCrudOperation.findById(
                    debitAccount.getBalanceId()
            );
            Balance currentCreditBalance = balanceCrudOperation.findById(
                    creditAccount.getBalanceId()
            );

            if (
                    currencyCrudOperation.findById(debitAccount.getCurrencyId()).getCode()
                            .equals("EUR")
                            &&
                            currencyCrudOperation.findById(creditAccount.getCurrencyId()).getCode()
                                    .equals("MGA")
            ) {
                CurrencyValue currentCurrencyValue = currencyValueCrudOperation.findByDate(LocalDate.now());

                BigDecimal debitAccountBalanceAmount = currentDebitBalance.getAmount();
                BigDecimal creditAccountBalanceAmount = currentCreditBalance.getAmount();

                debitAccountBalanceAmount = debitAccountBalanceAmount.subtract(
                        transactionSaved.getAmount()
                );
                creditAccountBalanceAmount = creditAccountBalanceAmount.add(
                        creditAccountBalanceAmount.multiply(currentCurrencyValue.getExchangeValue())
                );

                Balance debitBalanceSaved = balanceCrudOperation.save(new Balance(
                        0,
                        debitAccountBalanceAmount,
                        LocalDateTime.now()
                ));
                Balance creditBalanceSaved = balanceCrudOperation.save(new Balance(
                        0,
                        creditAccountBalanceAmount,
                        LocalDateTime.now()
                ));

                BalanceHistory debitBalanceHistorySaved = balanceHistoryCrudOperation.save(new BalanceHistory(
                        0,
                        debitBalanceSaved.getId(),
                        debitAccount.getId(),
                        LocalDateTime.now()
                ));
                BalanceHistory creditBalanceHistorySaved = balanceHistoryCrudOperation.save(new BalanceHistory(
                        0,
                        creditBalanceSaved.getId(),
                        creditAccount.getId(),
                        LocalDateTime.now()
                ));

                TransferHistory transferHistory = transferHistoryCrudOperation.save(new TransferHistory(
                        0,
                        debitAccount.getId(),
                        creditAccount.getId(),
                        LocalDateTime.now()
                ));

                Account debitAccountUpdated = accountCrudOperation.updateBalanceIdById(
                        debitAccount.getId(), debitAccount.getBalanceId()
                );
                Account creditAccountUpdated = accountCrudOperation.updateBalanceIdById(
                        creditAccount.getId(), creditAccount.getBalanceId()
                );
                if (
                        transactionSaved != null
                        &&
                        debitBalanceSaved != null
                        &&
                        creditBalanceSaved != null
                        &&
                        debitBalanceHistorySaved != null
                        &&
                        creditBalanceHistorySaved  != null
                        &&
                        debitAccountUpdated != null
                        &&
                        creditAccountUpdated != null
                ) {
                    commitStatement = commitTransactional();
                    commitStatement.execute();
                    return new Account(
                            debitAccount.getId(),
                            debitAccount.getAccountName(),
                            debitBalanceSaved.getId(),
                            debitAccount.getCurrencyId(),
                            debitAccount.getAccountType()
                    );
                } else {
                    rollbackStatement = rollbackTransactional();
                    rollbackStatement.execute();
                }
            }
        } catch (Exception exception) {
            System.out.println("Error occurred while making the transaction :\n"
                    + exception.getMessage()
            );
        } finally {
            try {
                if (beginStatement != null) {
                    beginStatement.close();
                }
                if (commitStatement != null) {
                    commitStatement.close();
                }
                if (rollbackStatement != null) {
                    rollbackStatement.close();
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