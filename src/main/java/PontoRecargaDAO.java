import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PontoRecargaDAO {
    private Integer totalLinhasBanco;

    private static final Logger logger = LoggerFactory.getLogger(PontoRecargaDAO.class);

    private void contarLinhasBanco() {
        String sql = "SELECT COUNT(*) as totalLinhas FROM pontoRecarga";

        logger.info("Verificando dados existentes no banco de dados.");
        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                totalLinhasBanco = rs.getInt("totalLinhas");
            }
        } catch (SQLException e) {
            logger.error("Falha ao capturar arquivos existentes no banco de dados.");
        }
    }

    public void inserirPontoRecarga() {

        LeitorPlanilha leitorPlanilha = new LeitorPlanilha();
        List<PontoRecarga> pontosRecarga = leitorPlanilha.lerPlanilhaLocal("C:/Users/natha/emap/pontos-recarga.xlsx");

        contarLinhasBanco();

        String sql = "INSERT INTO pontoRecarga (nome, tipoLocal, endereco, tipoRecarga, qtdEstacoes, tipoConector, rede) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            int totalLinhasInseridas = 0;
            logger.info("Iniciando inserção de dados.");
            for (PontoRecarga pontoRecarga : pontosRecarga) {
                if (pontosRecarga.indexOf(pontoRecarga) > totalLinhasBanco && totalLinhasInseridas < 100) {

                    try {

                        stmt.setString(1, pontoRecarga.getNome());
                        stmt.setString(2, pontoRecarga.getTipoDeLocal());
                        stmt.setString(3, pontoRecarga.getEndereco());
                        stmt.setString(4, pontoRecarga.getTipoDeRecarga());
                        stmt.setString(5, pontoRecarga.getQtdEstacoes());
                        stmt.setString(6, pontoRecarga.getTipoConector());
                        stmt.setString(7, pontoRecarga.getRedeDeRecarga());

                        stmt.executeUpdate();
                        logger.debug("Linha inserida com sucesso: Nome - {}, Tipo Local - {}, Endereço - {}, Tipo Recarga - {}, Quantidade Estações {}, Tipo Conector - {}, Rede - {}", pontoRecarga.getNome(), pontoRecarga.getTipoDeLocal(), pontoRecarga.getEndereco(), pontoRecarga.getTipoDeRecarga(), pontoRecarga.getQtdEstacoes(), pontoRecarga.getTipoConector(), pontoRecarga.getRedeDeRecarga());

                    } catch (SQLException e) {
                        logger.error("Erro ao inserir ponto de recarga: {}", e.getMessage());
                    }

                    totalLinhasInseridas++;
                }
            }

        } catch (SQLException e) {
            logger.error("Erro ao se conectar com o banco de dados: {}", e.getMessage());
        }
    }
}
