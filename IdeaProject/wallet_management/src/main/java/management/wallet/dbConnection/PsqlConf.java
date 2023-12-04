package management.wallet.dbConnection;

public class PsqlConf {
    public static final String URL = System.getenv("DB_URL");
    public static final String USERNAME = System.getenv("DB_USERNAME");
    public static final String PASSWORD = System.getenv("DB_PASSWORD");
}
