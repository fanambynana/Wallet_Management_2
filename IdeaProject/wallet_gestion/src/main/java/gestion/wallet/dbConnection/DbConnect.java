package gestion.wallet.dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnect {
    public Connection createConnection() {
        try {
            return DriverManager.getConnection(
                    PsqlConf.URL,
                    PsqlConf.USERNAME,
                    PsqlConf.PASSWORD
            );
        } catch (Exception exception) {
            System.out.println("Connection error :\n" + exception.getMessage());
            return null;
        }
    }
}
