import java.sql.*;

import javax.sql.DataSource;

public class Conexao {

    private static final String URL = "jdbc:mysql://localhost:3306/emap";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

//    public static void main(String[] args) {
////        String query = "SELECT * FROM usuario";
//
//        try (Connection connection = getConnection();
////             Statement statement = connection.createStatement();
////             ResultSet resultSet = statement.executeQuery(query);
//        ) {
//
//            if (connection != null) {
//                System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
//            }
//
////            while (resultSet.next()) {
////                int id = resultSet.getInt("idUsuario");
////                String nome = resultSet.getString("nomeUsuario");
////                String cpf = resultSet.getString("cpf");
////                String email = resultSet.getString("emailUsuario");
////                String numCelular = resultSet.getString("numeroCelular");
////                String senha = resultSet.getString("senha");
////
////                System.out.printf("""
////                        ID: %d
////                        Nome: %s
////                        CPF: %s
////                        Email: %s
////                        Celular: %s
////                        Senha: %s
////                        """, id, nome, cpf, email, numCelular, senha);
////            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }
}
