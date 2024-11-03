import java.sql.*;

public class ConexaoBanco {

    private static final String URL = System.getenv("BD_URL");
    private static final String USER = System.getenv("BD_USER");
    private static final String PASSWORD = System.getenv("BD_PASSWORD");
    public static Connection getConnection() throws SQLException {

        if (URL != null) {
            System.out.println(URL);
        }

        if (USER != null) {
            System.out.println(USER);
        }

        if (PASSWORD != null) {
            System.out.println(PASSWORD);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
