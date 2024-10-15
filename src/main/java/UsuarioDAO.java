import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
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
}
