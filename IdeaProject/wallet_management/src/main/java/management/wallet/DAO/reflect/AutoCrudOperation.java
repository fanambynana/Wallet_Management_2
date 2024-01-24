package management.wallet.DAO.reflect;

import lombok.AllArgsConstructor;
import lombok.Getter;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.*;
import management.wallet.model.Enum.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class AutoCrudOperation<T> implements CrudOperation<T> {
    private final T model;

    @Override
    public T save(T toSave) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        Class<?> clazz = toSave.getClass();
        String className = clazz.getSimpleName();
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        List<Field> fieldList = new ArrayList<>();
        T returned = null;

        try {
            connection = dbConnect.createConnection();
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
            preparedStatement = connection.prepareStatement(insertQuery);
            int parameterIndex = 1;
            for (Field field : fieldList) {
                preparedStatement.setObject(parameterIndex++, field.get(toSave));
            }
            preparedStatement.executeUpdate();
            returned = findById(getModelId(toSave));
        } catch (Exception exception) {
            System.err.println(
                    String.format("Error occurred while saving the %s :\n  %s\n  > %s",
                            className,
                            toSave,
                            exception.getMessage()
                    )
            );
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                System.err.println("Error while closing :\n  > "
                        + e.getMessage()
                );
            }
        }
        return returned;
    }

    @Override
    public boolean deleteById(int id) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        Class<?> clazz = getModel().getClass();
        String className = clazz.getSimpleName();
        boolean isDeleted = false;

        try {
            connection = dbConnect.createConnection();
            String query = String.format(
                    "DELETE FROM %s WHERE id = %s",
                    getSnakeCase(className),
                    id
            );
            preparedStatement = connection.prepareStatement(query);
            isDeleted = preparedStatement.executeUpdate() > 0;
        } catch (Exception exception) {
            System.err.println(
                    String.format("Error occurred while deleting the %s with id %s :\n  > %s",
                    className,
                    id,
                    exception.getMessage())
            );
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                System.err.println("Error while closing :\n  > "
                        + e.getMessage()
                );
            }
        }
        return isDeleted;
    }

    @Override
    public T update(T toUpdate) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        Class<?> clazz = toUpdate.getClass();
        String className = clazz.getSimpleName();
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder dataUpdate = new StringBuilder();
        List<Field> fieldList = new ArrayList<>();
        T returned = null;

        try {
            connection = dbConnect.createConnection();
            for (Field field : fields) {
                field.setAccessible(true);
                if (!dataUpdate.isEmpty()) {
                    dataUpdate.append(", ");
                }
                if (
                    field.getType() == AccountName.class
                    || field.getType() == AccountType.class
                    || field.getType() == CategoryGroup.class
                    || field.getType() == TransactionType.class
                ) {
                    dataUpdate.append(
                            String.format("%s = '%s'",
                            getSnakeCase(field.getName()),
                            field.get(toUpdate).toString().toLowerCase())
                    );
                } else {
                    dataUpdate.append(
                            String.format(
                                    "%s = ?",
                                    getSnakeCase(field.getName())
                            )
                    );
                    fieldList.add(field);
                }
            }
            String query = String.format(
                    "UPDATE %s SET %s WHERE id = %s",
                    getSnakeCase(className),
                    dataUpdate,
                    getModelId(toUpdate)
            );
            preparedStatement = connection.prepareStatement(query);
            int parameterIndex = 1;
            for (Field field : fieldList) {
                preparedStatement.setObject(parameterIndex++, field.get(toUpdate));
            }
            preparedStatement.executeUpdate();
            returned = findById(getModelId(toUpdate));
        } catch (Exception exception) {
            System.err.println(
                    String.format("Error occurred while updating the %s :\n  %s\n  > %s",
                            className,
                            toUpdate,
                            exception.getMessage()
                    )
            );
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                System.err.println("Error while closing :\n  > "
                        + e.getMessage()
                );
            }
        }
        return isDeleted;
    }

    @Override
    public T update(T toUpdate) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        Class<?> clazz = toUpdate.getClass();
        String className = clazz.getSimpleName();
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder dataUpdate = new StringBuilder();
        List<Field> fieldList = new ArrayList<>();
        T returned = null;

        try {
            connection = dbConnect.createConnection();
            for (Field field : fields) {
                field.setAccessible(true);
                if (!dataUpdate.isEmpty()) {
                    dataUpdate.append(", ");
                }
                if (
                    field.getType() == AccountName.class
                    || field.getType() == AccountType.class
                    || field.getType() == CategoryGroup.class
                    || field.getType() == TransactionType.class
                ) {
                    dataUpdate.append(
                            String.format("%s = '%s'",
                            getSnakeCase(field.getName()),
                            field.get(toUpdate).toString().toLowerCase())
                    );
                } else {
                    dataUpdate.append(
                            String.format(
                                    "%s = ?",
                                    getSnakeCase(field.getName())
                            )
                    );
                    fieldList.add(field);
                }
            }
            String query = String.format(
                    "UPDATE %s SET %s WHERE id = %s",
                    getSnakeCase(className),
                    dataUpdate,
                    getModelId(toUpdate)
            );
            preparedStatement = connection.prepareStatement(query);
            int parameterIndex = 1;
            for (Field field : fieldList) {
                preparedStatement.setObject(parameterIndex++, field.get(toUpdate));
            }
            preparedStatement.executeUpdate();
            returned = findById(getModelId(toUpdate));
        } catch (Exception exception) {
            System.err.println(
                    String.format("Error occurred while updating the %s :\n  %s\n  > %s",
                            className,
                            toUpdate,
                            exception.getMessage()
                    )
            );
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                System.err.println("Error while closing :\n"
                        + e.getMessage()
                );
            }
        }
        return returned;
    }

    @Override
    public T findById(int id) {
        DbConnect dbConnect = new DbConnect();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Class<?> clazz = getModel().getClass();
        String className = clazz.getSimpleName();
        String findingError = String.format("Error occurred while finding the %s with id %s :\n  > ",
                className,
                id
        );
        T returned = null;

        try {
            connection = dbConnect.createConnection();
            String query = String.format(
                    "SELECT * FROM %s WHERE id = %s",
                    getSnakeCase(className),
                    id
            );
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();
            List<T> result = getModelObject(resultSet);
            if (!result.isEmpty()) {
                returned = result.get(0);
            } else {
                System.err.println(findingError + className + " does not exists");
            }
        } catch (Exception exception) {
            System.err.println(findingError + exception.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                System.err.println("Error while closing :\n  > "
                        + e.getMessage()
                );
            }
        }
        return returned;
    }

    @Override
    public List<T> findAll() {
        DbConnect dbConnect = new DbConnect();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Class<?> clazz = getModel().getClass();
        String className = clazz.getSimpleName();
        List<T> tList  = new ArrayList<>();

        try {
            connection = dbConnect.createConnection();
            String query = "SELECT * FROM " + getSnakeCase(className);
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            resultSet  = preparedStatement.getResultSet();
            tList = getModelObject(resultSet);
        } catch (Exception exception) {
            System.err.println(String.format("Error occurred while finding all %ss  :\n  > %s",
                    className,
                    exception.getMessage()));
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                System.err.println("Error while closing :\n  > "
                        + e.getMessage()
                );
            }
        }
        return tList;
    }

    private Integer getModelId(Object objectModel) throws Exception{
        Class<?> clazz = getModel().getClass();
        Field[] fields = clazz.getDeclaredFields();
        Integer id = null;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals("id")) {
                id = (int) field.get(objectModel);
            }
        }
        return id;
    }

    private static String getSnakeCase(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Character c : name.toCharArray()) {
            if (!stringBuilder.isEmpty()) {
                if (c.toString().toUpperCase().equals(c.toString())) {
                    stringBuilder.append("_").append(c);
                    continue;
                }
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString().toLowerCase();
    }

    public List<T> getModelObject(ResultSet resultSet) throws Exception {
        Class<?> clazz = getModel().getClass();
        Field[] fields = clazz.getDeclaredFields();
        List<T> tList = new ArrayList<>();
        while (resultSet.next()) {
            if (getModel().getClass() == Currency.class) {
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
            } else if (getModel().getClass() == Account.class){
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
            } else if (getModel().getClass() == Balance.class) {
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
            } else if (getModel().getClass() == BalanceHistory.class) {
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
            } else if (getModel().getClass() == CurrencyValue.class) {
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
            } else if (getModel().getClass() == Transaction.class) {
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
            } else if (getModel().getClass() == TransactionCategories.class) {
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
            } else if (getModel().getClass() == TransferHistory.class) {
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
}
