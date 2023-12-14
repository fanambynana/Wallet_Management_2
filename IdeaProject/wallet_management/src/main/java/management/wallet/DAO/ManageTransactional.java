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
import java.util.ArrayList;
import java.util.List;

@Repository
public class ManageTransactional {
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();
    AccountDAO accountDAO;
    TransactionDAO transactionDAO;
    CurrencyDAO currencyDAO;
    BalanceDAO balanceDAO;
    BalanceHistoryDAO balanceHistoryDAO;
    TransferHistoryDAO transferHistoryDAO;
    CurrencyValueDAO currencyValueDAO;

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
            AccountSave account = accountDAO.findById(accountId);
            TransactionSave transactionSaved = transactionDAO.save(transaction);
            Balance balanceSaved = balanceDAO.save(new Balance(
                    0,
                    transactionSaved.getAmount(),
                    LocalDateTime.now()
            ));
            BalanceHistory balanceHistorySaved = balanceHistoryDAO.save(new BalanceHistory(
                    0,
                    balanceSaved.getId(),
                    account.getId(),
                    LocalDateTime.now()
            ));
            boolean accountUpdated = accountDAO.updateBalanceIdById(accountId, balanceSaved.getId());
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
            TransactionSave transactionSaved = transactionDAO.save(transaction);

            AccountSave debitAccount = accountDAO.findById(transactionSaved.getAccountId());
            AccountSave creditAccount = accountDAO.findById(creditAccountId);

            Balance currentDebitBalance = balanceDAO.findById(
                    debitAccount.getBalanceId()
            );
            Balance currentCreditBalance = balanceDAO.findById(
                    creditAccount.getBalanceId()
            );

            if (
                    currencyDAO.findById(debitAccount.getCurrencyId()).getCode()
                            .equals("EUR")
                    &&
                    currencyDAO.findById(creditAccount.getCurrencyId()).getCode()
                            .equals("MGA")
            ) {
                CurrencyValue currentCurrencyValue = currencyValueDAO.findByDate(LocalDate.now());

                BigDecimal debitAccountBalanceAmount = currentDebitBalance.getAmount();
                BigDecimal creditAccountBalanceAmount = currentCreditBalance.getAmount();

                debitAccountBalanceAmount = debitAccountBalanceAmount.subtract(
                        transactionSaved.getAmount()
                );
                creditAccountBalanceAmount = creditAccountBalanceAmount.add(
                        creditAccountBalanceAmount.multiply(currentCurrencyValue.getExchangeValue())
                );

                Balance debitBalanceSaved = balanceDAO.save(new Balance(
                        0,
                        debitAccountBalanceAmount,
                        LocalDateTime.now()
                ));
                Balance creditBalanceSaved = balanceDAO.save(new Balance(
                        0,
                        creditAccountBalanceAmount,
                        LocalDateTime.now()
                ));

                BalanceHistory debitBalanceHistorySaved = balanceHistoryDAO.save(new BalanceHistory(
                        0,
                        debitBalanceSaved.getId(),
                        debitAccount.getId(),
                        LocalDateTime.now()
                ));
                BalanceHistory creditBalanceHistorySaved = balanceHistoryDAO.save(new BalanceHistory(
                        0,
                        creditBalanceSaved.getId(),
                        creditAccount.getId(),
                        LocalDateTime.now()
                ));

                TransferHistory transferHistory = transferHistoryDAO.save(new TransferHistory(
                        0,
                        debitAccount.getId(),
                        creditAccount.getId(),
                        LocalDateTime.now()
                ));

                boolean debitAccountUpdated = accountDAO.updateBalanceIdById(
                        debitAccount.getId(), debitAccount.getBalanceId()
                );
                boolean creditAccountUpdated = accountDAO.updateBalanceIdById(
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
