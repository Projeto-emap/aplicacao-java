import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    public void listarUsuarios() {
        String sql = "SELECT * FROM usuario WHERE idUsuario";

        try (Connection con = Conexao.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            //       stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.printf("""
                        ID: %d
                        Nome: %s
                        CPF: %s
                        Email: %s
                        Celular: %s
                        Senha: %s
                        """,
                        rs.getInt("idUsuario"),
                        rs.getString("nomeUsuario"),
                        rs.getString("cpf"),
                        rs.getString("emailUsuario"),
                        rs.getString("numeroCelular"),
                        rs.getString("senha"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void consultarUsuario(int id) {
        String sql = "SELECT * FROM usuario WHERE idUsuario = ?";

        try (Connection con = Conexao.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.printf("""
                        ID: %d
                        Nome: %s
                        CPF: %s
                        Email: %s
                        Celular: %s
                        Senha: %s
                        """,
                        rs.getInt("idUsuario"),
                        rs.getString("nomeUsuario"),
                        rs.getString("cpf"),
                        rs.getString("emailUsuario"),
                        rs.getString("numeroCelular"),
                        rs.getString("senha"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inserirUsuario(String nome, String cpf, String email, String senha) {
        String sql = "INSERT INTO usuario (nomeUsuario, cpf, emailUsuario, senha) VALUES (?, ?, ?, ?)";

        try (Connection con = Conexao.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1,nome);
            stmt.setString(2,cpf);
            stmt.setString(3,email);
            stmt.setString(4,senha);

            int linhasAfetadas = stmt.executeUpdate();
            System.out.println("Linhas inseridas: " + linhasAfetadas);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
