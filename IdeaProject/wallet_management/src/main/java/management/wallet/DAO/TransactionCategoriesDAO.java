package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import org.springframework.stereotype.Repository;

import java.sql.Connection;

@Repository
public class TransactionCategoriesDAO {
    DbConnect connect = new DbConnect();
    Connection connection = connect.createConnection();


}
