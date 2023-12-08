package management.wallet.DAO;

import management.wallet.dbConnection.DbConnect;
import management.wallet.model.Transaction;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ManageTransactional {
    DbConnect dbConnect = new DbConnect();
    Connection connection = dbConnect.createConnection();

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

    public boolean makeTransaction(int accountId, Transaction transaction) {
        beginTransactional();
        // transaction here
        // commit if no error occurred, else rollback
        return false;
    }
}
