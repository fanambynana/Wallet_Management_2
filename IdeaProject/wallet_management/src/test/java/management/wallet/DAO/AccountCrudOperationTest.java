package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.AccountSave;
import management.wallet.model.Enum.AccountName;
import management.wallet.model.Enum.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;
import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountCrudOperationTest {
    private AccountCrudOperation accountCrudOperation;
/*    private DbConnect dbConnect;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;*/
    @BeforeEach
    void setUp() {
       /* dbConnect = mock(DbConnect.class);*/
       /* connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);*/
       accountCrudOperation = new AccountCrudOperation();
    }

    @Test
    void findAll() throws SQLException {

        AccountSave account = new AccountSave(1, AccountName.SAVINGS_ACCOUNT, 1001, 1, AccountType.CASH);

        assertEquals(1, account.getId());
        assertEquals( AccountName.SAVINGS_ACCOUNT , account.getAccountName());
        assertEquals(1001, account.getBalanceId());
        assertEquals(1, account.getCurrencyId());
        assertEquals(AccountType.CASH, account.getAccountType());


    }

    @Test
    void saveAll() {
    }
}