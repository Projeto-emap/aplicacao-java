import java.sql.*;

import javax.sql.DataSource;

public class Conexao {

    private static final String URL = System.getenv("BD_URL");
    private static final String USER = System.getenv("BD_USER");
    private static final String PASSWORD = System.getenv("BD_PASSWORD");
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
