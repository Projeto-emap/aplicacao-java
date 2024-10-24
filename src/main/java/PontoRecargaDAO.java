import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PontoRecargaDAO {
    public void inserirPontoRecarga(String nome, String tipoDeLocal, String endereco, String tipoDeRecarga, Integer qtdEstacoes, String tipoConector, String redeDeRecarga) {
        String sql = "INSERT INTO pontoRecarga (nome, tipoLocal, endereco, tipoRecarga, qtdEstacoes, tipoConector, rede) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexao.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, tipoDeLocal);
            stmt.setString(3, endereco);
            stmt.setString(4, tipoDeRecarga);
            stmt.setInt(5,qtdEstacoes);
            stmt.setString(6, tipoConector);
            stmt.setString(7, redeDeRecarga);

            int linhasAfetadas = stmt.executeUpdate();
            System.out.println("Linhas inseridas: " + linhasAfetadas);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
