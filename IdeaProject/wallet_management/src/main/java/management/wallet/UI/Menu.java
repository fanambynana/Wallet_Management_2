package management.wallet.UI;

import management.wallet.DAO.*;

import java.util.Scanner;

public class Menu implements Model {
    // get one/all account, balance, balance history, currency, currency value,
    // transaction, transaction category, transfer history
    public static boolean toBreak;
    private static String action;
    private static int id;
    private final Scanner scanner = new Scanner(System.in);
    AccountCrudOperation accountCrudOperation = new AccountCrudOperation();
    BalanceCrudOperation balanceCrudOperation = new BalanceCrudOperation();
    BalanceHistoryCrudOperation balanceHistoryCrudOperation = new BalanceHistoryCrudOperation();
    CurrencyCrudOperation currencyCrudOperation = new CurrencyCrudOperation();
    CurrencyValueCrudOperation currencyValueCrudOperation = new CurrencyValueCrudOperation();
    TransactionCrudOperation transactionCrudOperation = new TransactionCrudOperation();
    TransactionCategoriesCrudOperation transactionCategoriesCrudOperation = new TransactionCategoriesCrudOperation();
    TransferHistoryCrudOperation transferHistoryCrudOperation = new TransferHistoryCrudOperation();

    public void chooseAction(int choice) {
        switch (choice) {
            case 1 -> action = "findAll";
            case 2 -> {
                action = "findById";
                System.out.print("ID : ");
                id = scanner.nextInt();
            }
            case 0 -> exit();
        }
    }
    public void chooseModel(int choice) {
        switch (choice) {
            case 1 -> accountModel();
            case 2 -> balanceModel();
            case 3 -> balanceHistoryModel();
            case 4 -> currencyModel();
            case 5 -> currencyValueModel();
            case 6 -> transactionModel();
            case 7 -> transactionCategoryModel();
            case 8 -> transferHistoryModel();
            case 0 -> exit();
        }
    }

    private void exit() {
        System.out.println("You're quitting,");
        System.out.println("...");
        toBreak = true;

    }

    @Override
    public void accountModel() {
        if (action.equals("findAll")) {
            System.out.println(accountCrudOperation.findAll());
        } else if (action.equals("findById")) {
            System.out.println(accountCrudOperation.findById(id));
        }
    }

    @Override
    public void balanceModel() {
        if (action.equals("findAll")) {
            System.out.println(
                    balanceCrudOperation.findAll() + "\n"
            );
        } else if (action.equals("findById")) {
            System.out.println(balanceCrudOperation.findById(id));
        }
    }

    @Override
    public void balanceHistoryModel() {
        if (action.equals("findAll")) {
            System.out.println(balanceHistoryCrudOperation.findAll());
        } else if (action.equals("findById")) {
            System.out.println(balanceHistoryCrudOperation.findById(id));
        }
    }

    @Override
    public void currencyModel() {
        if (action.equals("findAll")) {
            System.out.println(currencyCrudOperation.findAll());
        } else if (action.equals("findById")) {
            System.out.println(currencyCrudOperation.findById(id));
        }
    }

    @Override
    public void currencyValueModel() {
        if (action.equals("findAll")) {
            System.out.println(currencyValueCrudOperation.findAll());
        } else if (action.equals("findById")) {
            System.out.println(currencyValueCrudOperation.findById(id));
        }
    }

    @Override
    public void transactionModel() {
        if (action.equals("findAll")) {
            System.out.println(transactionCrudOperation.findAll());
        } else if (action.equals("findById")) {
            System.out.println(transactionCrudOperation.findById(id));
        }
    }

    @Override
    public void transactionCategoryModel() {
        if (action.equals("findAll")) {
            System.out.println(transactionCategoriesCrudOperation.findAll());
        } else if (action.equals("findById")) {
            System.out.println(transactionCategoriesCrudOperation.findById(id));
        }
    }

    @Override
    public void transferHistoryModel() {
        if (action.equals("findAll")) {
            System.out.println(transferHistoryCrudOperation.findAll());
        } else if (action.equals("findById")) {
            System.out.println(transferHistoryCrudOperation.findById(id));
        }
    }
}
