package management.wallet.DAO.reflect;

import management.wallet.DAO.reflect.test.withPostgre.CrudOpClass;
import management.wallet.dbConnection.DbConnect;
import management.wallet.model.*;
import management.wallet.model.Enum.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AutoCrudOperation<T> implements CrudOperation<T> {
    private final T t;
    public AutoCrudOperation(T t) {
        this.t = t;
    }
    public T getT() {
        return t;
    }

    public static String getSnakeCase(String name) {
        StringBuilder sb = new StringBuilder();
        for (Character c : name.toCharArray()) {
            if (!sb.isEmpty()) {
                if (c.toString().toUpperCase().equals(c.toString())) {
                    sb.append("_").append(c);
                    continue;
                }
            }
            sb.append(c);
        }
        return sb.toString().toLowerCase();
    }

    public List<T> returnObjectModel(ResultSet resultSet) throws SQLException {
        Class<?> clazz = getT().getClass();
        Field[] fields = clazz.getDeclaredFields();
        List<T> tList = new ArrayList<>();
        while (resultSet.next()) {
            if (getT().getClass() == Currency.class) {
                Currency result = new Currency();
                int stringCount = 0;
                for (Field field : fields) {
                    Type fieldType = field.getType();
                    if (fieldType == int.class) {
                        result.setId(resultSet.getInt(getSnakeCase(field.getName())));
                    } else if (fieldType == String.class) {
                        stringCount++;
                        if (stringCount == 1) {
                            result.setName(resultSet.getString(getSnakeCase(field.getName())));
                        } else {
                            result.setCode(resultSet.getString(getSnakeCase(field.getName())));
                        }
                    }
                }
                tList.add((T) result);
            } else if (getT().getClass() == Account.class){
                Account result = new Account();
                int intCount = 0;
                for (Field field : fields) {
                    Type fieldType = field.getType();
                    if (fieldType == int.class) {
                        intCount++;
                        if (intCount == 1) {
                            result.setId(resultSet.getInt(getSnakeCase(field.getName())));
                        } else if (intCount == 2) {
                            result.setBalanceId(resultSet.getInt(getSnakeCase(field.getName())));
                        } else if (intCount == 3) {
                            result.setCurrencyId(resultSet.getInt(getSnakeCase(field.getName())));
                        }
                    } else if (fieldType == AccountName.class) {
                        result.setAccountName(GetAccountName.getEnum(resultSet.getString(getSnakeCase(field.getName()))));
                    } else if (fieldType == AccountType.class) {
                        result.setAccountType(GetAccountType.getEnum(resultSet.getString(getSnakeCase(field.getName()))));
                    }
                }
                tList.add((T) result);
            } else if (getT().getClass() == Balance.class) {
                Balance result = new Balance();
                for (Field field : fields) {
                    Type fieldType = field.getType();
                    if (fieldType == int.class) {
                        result.setId(resultSet.getInt(getSnakeCase(field.getName())));
                    } else if (fieldType == BigDecimal.class) {
                        result.setAmount(resultSet.getBigDecimal(getSnakeCase(field.getName())));
                    } else if (fieldType == LocalDateTime.class) {
                        result.setUpdateDatetime(resultSet.getTimestamp(getSnakeCase(field.getName())).toLocalDateTime());
                    }
                }
                tList.add((T) result);
            } else if (getT().getClass() == BalanceHistory.class) {
                BalanceHistory result = new BalanceHistory();
                int intCount = 0;
                for (Field field : fields) {
                    Type fieldType = field.getType();
                    if (fieldType == int.class) {
                        intCount++;
                        if (intCount == 1) {
                            result.setId(resultSet.getInt(getSnakeCase(field.getName())));
                        } else if (intCount == 2) {
                            result.setBalanceId(resultSet.getInt(getSnakeCase(field.getName())));
                        } else if (intCount == 3) {
                            result.setAccountId(resultSet.getInt(getSnakeCase(field.getName())));
                        }
                    } else if (fieldType == LocalDateTime.class) {
                        result.setDatetime(resultSet.getTimestamp(getSnakeCase(field.getName())).toLocalDateTime());
                    }
                }
                tList.add((T) result);
            } else if (getT().getClass() == CurrencyValue.class) {
                CurrencyValue result = new CurrencyValue();
                int intCount = 0;
                for (Field field : fields) {
                    Type fieldType = field.getType();
                    if (fieldType == int.class) {
                        intCount++;
                        if (intCount == 1) {
                            result.setId(resultSet.getInt(getSnakeCase(field.getName())));
                        } else if (intCount == 2) {
                            result.setExchangeSourceId(resultSet.getInt(getSnakeCase(field.getName())));
                        } else if (intCount == 3) {
                            result.setExchangeDestinationId(resultSet.getInt(getSnakeCase(field.getName())));
                        }
                    } else if (fieldType == LocalDateTime.class) {
                        result.setExchangeDatetime(resultSet.getTimestamp(getSnakeCase(field.getName())).toLocalDateTime());
                    } else if (fieldType == BigDecimal.class) {
                        result.setExchangeValue(resultSet.getBigDecimal(getSnakeCase(field.getName())));
                    }
                }
                tList.add((T) result);
            } else if (getT().getClass() == Transaction.class) {
                Transaction result = new Transaction();
                int intCount = 0;
                for (Field field : fields) {
                    Type fieldType = field.getType();
                    if (fieldType == int.class) {
                        intCount++;
                        if (intCount == 1) {
                            result.setId(resultSet.getInt(getSnakeCase(field.getName())));
                        } else if (intCount == 2) {
                            result.setTransactionCategoryId(resultSet.getInt(getSnakeCase(field.getName())));
                        } else if (intCount == 3) {
                            result.setAccountId(resultSet.getInt(getSnakeCase(field.getName())));
                        }
                    } else if (fieldType == LocalDateTime.class) {
                        result.setTransactionDate(resultSet.getTimestamp(getSnakeCase(field.getName())).toLocalDateTime());
                    } else if (fieldType == BigDecimal.class) {
                        result.setAmount(resultSet.getBigDecimal(getSnakeCase(field.getName())));
                    } else if (fieldType == TransactionType.class) {
                        result.setTransactionType(GetTransactionType.getEnum(resultSet.getString(getSnakeCase(field.getName()))));
                    } else if (fieldType == String.class) {
                        result.setLabel(resultSet.getString(getSnakeCase(field.getName())));
                    }
                }
                tList.add((T) result);
            } else if (getT().getClass() == TransactionCategories.class) {
                TransactionCategories result = new TransactionCategories();
                for (Field field : fields) {
                    Type fieldType = field.getType();
                    if (fieldType == int.class) {
                        result.setId(resultSet.getInt(getSnakeCase(field.getName())));
                    } else if (fieldType == String.class) {
                        result.setCategoryName(resultSet.getString(getSnakeCase(field.getName())));
                    } else if (fieldType == CategoryGroup.class) {
                        result.setCategoryGroup(GetCategoryGroup.getEnum(resultSet.getString(getSnakeCase(field.getName()))));
                    }
                }
                tList.add((T) result);
            } else if (getT().getClass() == TransferHistory.class) {
                TransferHistory result = new TransferHistory();
                int intCount = 0;
                for (Field field : fields) {
                    Type fieldType = field.getType();
                    if (fieldType == int.class) {
                        intCount++;
                        if (intCount == 1) {
                            result.setId(resultSet.getInt(getSnakeCase(field.getName())));
                        } else if (intCount == 2) {
                            result.setDebitTransactionId(resultSet.getInt(getSnakeCase(field.getName())));
                        } else if (intCount == 3) {
                            result.setCreditTransactionId(resultSet.getInt(getSnakeCase(field.getName())));
                        }
                    } else if (fieldType == LocalDateTime.class) {
                        result.setDatetime(resultSet.getTimestamp(getSnakeCase(field.getName())).toLocalDateTime());
                    }
                }
                tList.add((T) result);
            }
        }
        return tList;
    }

    @Override
    public T save(T toSave) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        Class<?> clazz = toSave.getClass();
        String className = clazz.getSimpleName();
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        List<Field> fieldList = new ArrayList<>();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (!columns.isEmpty()) {
                    columns.append(", ");
                    values.append(", ");
                }
                columns.append(getSnakeCase(field.getName()));
                if (
                        field.getType() == AccountName.class
                                || field.getType() == AccountType.class
                                || field.getType() == CategoryGroup.class
                                || field.getType() == TransactionType.class
                ) {
                    values.append(String.format("'%s'",
                            field.get(toSave).toString().toLowerCase()));
                } else {
                    fieldList.add(field);
                    values.append("?");
                }
            }
            String insertQuery = String.format(
                    "INSERT INTO %s (" + columns + ") VALUES (" + values + ")",
                    getSnakeCase(className));
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            int parameterIndex = 1;
            for (Field field : fieldList) {
                preparedStatement.setObject(parameterIndex++, field.get(toSave));
            }
            preparedStatement.executeUpdate();
            return toSave;
        } catch (Exception exception) {
            System.out.println(
                    String.format("Error occurred while saving the %s :\n%s",
                            className,
                            exception.getMessage()
                    ));
        }
        return null;
    }

    @Override
    public List<T> findAll() {
        Currency currency = new Currency();
        DbConnect dbConnect = new DbConnect();
        Connection connection = dbConnect.createConnection();
        Class<?> clazz = getT().getClass();
        String className = clazz.getSimpleName();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<T> list  = new ArrayList<>();

        try {
            String query = "SELECT * FROM " + getSnakeCase(className);
            statement = connection.prepareStatement(query);
            statement.execute();
            resultSet  = statement.getResultSet();
            connection.close();
            return returnObjectModel(resultSet);
        } catch (SQLException exception) {
            System.out.println(String.format("Error occurred while finding all %ss  :\n%s" ,className, exception.getMessage()));
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
        return list;
    }

    public static void main(String[] args) {
        CrudOpClass<Currency> currencyCrudOp = new CrudOpClass<>(new Currency());
        CrudOpClass<Account> accountCrudOpClass = new CrudOpClass<>(new Account());
        CrudOpClass<Balance> balanceCrudOpClass = new CrudOpClass<>(new Balance());
        CrudOpClass<BalanceHistory> balanceHistoryCrudOpClass = new CrudOpClass<>(new BalanceHistory());
        CrudOpClass<CurrencyValue> currencyValueCrudOpClass = new CrudOpClass<>(new CurrencyValue());
        CrudOpClass<Transaction> transactionCrudOpClass = new CrudOpClass<>(new Transaction());
        CrudOpClass<TransactionCategories> transactionCategoriesCrudOpClass = new CrudOpClass<>(new TransactionCategories());
        CrudOpClass<TransferHistory> transferHistoryCrudOpClass = new CrudOpClass<>(new TransferHistory());

        System.out.println(currencyCrudOp.save(
                new Currency(10, "ts10", "TS10")
        ));

        System.out.println(accountCrudOpClass.save(
                new Account(
                        10,
                        AccountName.CURRENT_ACCOUNT,
                        1,
                        3,
                        AccountType.CASH
                )
        ));

        System.out.println(currencyCrudOp.findAll());
        System.out.println(accountCrudOpClass.findAll());
        System.out.println(balanceCrudOpClass.findAll());
        System.out.println(balanceHistoryCrudOpClass.findAll());
        System.out.println(currencyValueCrudOpClass.findAll());
        System.out.println(transactionCategoriesCrudOpClass.findAll());
        System.out.println(transactionCrudOpClass.findAll());
        System.out.println(transferHistoryCrudOpClass.findAll());

    }
}
