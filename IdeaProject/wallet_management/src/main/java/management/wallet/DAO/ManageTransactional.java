package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public class ManageTransactional {
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();
    AccountCrudOperation accountCrudOperation;
    TransactionCrudOperation transactionCrudOperation;
    CurrencyCrudOperation currencyCrudOperation;
    BalanceCrudOperation balanceCrudOperation;
    BalanceHistoryCrudOperation balanceHistoryCrudOperation;
    TransferHistoryCrudOperation transferHistoryCrudOperation;
    CurrencyValueCrudOperation currencyValueCrudOperation;

    public void beginTransactional() {
        try {
            String query = "BEGIN;";
            PreparedStatement statement = connection.prepareStatement(query);
        } catch (SQLException sqlException) {
            System.out.println("Error occurred :\n"
                + sqlException.getMessage()
            );
        }
    }
    public void commitTransactional() {
        try {
            String query = "COMMIT;";
            connection.prepareStatement(query);
        } catch (SQLException sqlException) {
            System.out.println("Error occurred :\n"
                    + sqlException.getMessage()
            );
        }
    }
    public void rollbackTransactional() {
        try {
            String query = "ROLLBACK;";
            connection.prepareStatement(query);
        } catch (SQLException sqlException) {
            System.out.println("Error occurred :\n"
                    + sqlException.getMessage()
            );
        }
    }

    public AccountSave makeTransaction(int accountId, TransactionSave transaction) {
        beginTransactional();
        try {
            AccountSave account = accountCrudOperation.findById(accountId);
            TransactionSave transactionSaved = transactionCrudOperation.save(transaction);
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
            boolean accountUpdated = accountCrudOperation.updateBalanceIdById(accountId, balanceSaved.getId());
            if (accountUpdated) {
                commitTransactional();
                return new AccountSave(
                        account.getId(),
                        account.getAccountName(),
                        balanceSaved.getId(),
                        account.getCurrencyId(),
                        account.getAccountType()
                );
            } else {
                rollbackTransactional();
            }
        } catch (Exception exception) {
            rollbackTransactional();
            System.out.println("Error occurred while making the transaction :\n"
                + exception.getMessage()
            );
        }
        return null;
    }
    public AccountSave makeTransactionWithExchange(int creditAccountId, TransactionSave transaction) {
        beginTransactional();
        try {
            TransactionSave transactionSaved = transactionCrudOperation.save(transaction);

            AccountSave debitAccount = accountCrudOperation.findById(transactionSaved.getAccountId());
            AccountSave creditAccount = accountCrudOperation.findById(creditAccountId);

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

                boolean debitAccountUpdated = accountCrudOperation.updateBalanceIdById(
                        debitAccount.getId(), debitAccount.getBalanceId()
                );
                boolean creditAccountUpdated = accountCrudOperation.updateBalanceIdById(
                        creditAccount.getId(), creditAccount.getBalanceId()
                );
                if (creditAccountUpdated && debitAccountUpdated) {
                    commitTransactional();
                    return new AccountSave(
                            debitAccount.getId(),
                            debitAccount.getAccountName(),
                            debitBalanceSaved.getId(),
                            debitAccount.getCurrencyId(),
                            debitAccount.getAccountType()
                    );
                } else {
                    rollbackTransactional();
                }
            }
        } catch (Exception exception) {
            rollbackTransactional();
            System.out.println("Error occurred while making the transaction :\n"
                    + exception.getMessage()
            );
        }
        return null;
    }
}
