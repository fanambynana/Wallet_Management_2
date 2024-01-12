package management.wallet;

import management.wallet.DAO.*;
import management.wallet.UI.Menu;
import management.wallet.UI.ShowMenu;
import management.wallet.model.*;
import management.wallet.model.Enum.AccountName;
import management.wallet.model.Enum.AccountType;
import management.wallet.model.Enum.CategoryGroup;
import management.wallet.model.Enum.TransactionType;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Main {
    AccountCrudOperation accountCrudOperation = new AccountCrudOperation();
    BalanceCrudOperation balanceCrudOperation = new BalanceCrudOperation();
    BalanceHistoryCrudOperation balanceHistoryCrudOperation = new BalanceHistoryCrudOperation();
    CurrencyCrudOperation currencyCrudOperation = new CurrencyCrudOperation();
    CurrencyValueCrudOperation currencyValueCrudOperation = new CurrencyValueCrudOperation();
    TransactionCategoriesCrudOperation transactionCategoriesCrudOperation = new TransactionCategoriesCrudOperation();
    TransactionCrudOperation transactionCrudOperation = new TransactionCrudOperation();
    TransferHistoryCrudOperation transferHistoryCrudOperation = new TransferHistoryCrudOperation();

    public void runMain() {
        while (!Menu.toBreak) {
            ShowMenu.actionMenu();
            Menu menu = new Menu();
            menu.chooseAction(ShowMenu.prompt());
            if (!Menu.toBreak) {
                ShowMenu.modelMenu();
                menu.chooseModel(ShowMenu.prompt());
            }
        }
    }
    public void runInsert() {
        System.out.println(accountCrudOperation.save(new AccountSave(
                3,
                AccountName.CURRENT_ACCOUNT,
                3,
                1,
                AccountType.MOBILE_MONEY
        )));
        System.out.println(balanceCrudOperation.save(new Balance(
                4,
                BigDecimal.valueOf(2000),
                LocalDateTime.now()
        )));
        System.out.println(balanceHistoryCrudOperation.save(new BalanceHistory(
                15,
                1,
                2,
                LocalDateTime.now()
        )));
        System.out.println(currencyCrudOperation.save(new Currency(
                4,
                "Canadian Dollar",
                "CAD"
        )));
        System.out.println(currencyValueCrudOperation.save(new CurrencyValue(
                7,
                2,
                3,
                BigDecimal.valueOf(4600),
                Timestamp.valueOf("2023-12-06 00:00:00").toLocalDateTime()
        )));
        System.out.println(transactionCategoriesCrudOperation.save(new TransactionCategories(
                3,
                "Foods & Drinking",
                CategoryGroup.EXPENSE
        )));
        System.out.println(transactionCrudOperation.save(new TransactionSave(
                7,
                "New Year Gift",
                BigDecimal.valueOf(10000),
                LocalDateTime.now(),
                TransactionType.DEBIT,
                4,
                1
        )));
        System.out.println(transactionCrudOperation.save(new TransactionSave(
                8,
                "New Year Gift",
                BigDecimal.valueOf(10000),
                LocalDateTime.now(),
                TransactionType.CREDIT,
                4,
                3
        )));
        System.out.println(transferHistoryCrudOperation.save(new TransferHistory(
                2,
                2,
                9,
                Timestamp.valueOf("2023-12-02 02:00:00").toLocalDateTime()
        )));
    }
    public static void main(String[] args) {
        Main main = new Main();
        // main.runMain();
        main.runInsert();
    }

    /*
        Connection error :
        FATAL: remaining connection slots are reserved for non-replication superuser connections
     */
}
