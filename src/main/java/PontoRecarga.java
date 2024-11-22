import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PontoRecarga {
    private String nome;
    private String tipoDeLocal;
    private String endereco;
    private String tipoDeRecarga;
    private Integer qtdEstacoes;
    private String tipoConector;
    private String redeDeRecarga;

    private static final Logger logger = LoggerFactory.getLogger(PontoRecarga.class);

    public PontoRecarga () {}

    public PontoRecarga(String nome, String tipoDeLocal, String endereco, String tipoDeRecarga, Integer qtdEstacoes, String tipoConector, String redeDeRecarga) {
        this.nome = nome;
        this.tipoDeLocal = tipoDeLocal;
        this.endereco = endereco;
        this.tipoDeRecarga = tipoDeRecarga;
        this.qtdEstacoes = qtdEstacoes;
        this.tipoConector = tipoConector;
        this.redeDeRecarga = redeDeRecarga;
    }

    private Integer contarLinhasBanco() {
        String sql = "SELECT COUNT(*) as totalLinhas FROM pontoDeRecarga";
        int totalLinhasBanco = 0;

        logger.info("Verificando dados existentes no banco de dados.");
        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
               totalLinhasBanco = rs.getInt("totalLinhas");
            }
        } catch (SQLException e) {
            logger.error("Falha ao capturar arquivos existentes no banco de dados: {}", e.getMessage());
        }

        return totalLinhasBanco;
    }

    public void inserirPontoRecarga(List<PontoRecarga> pontosRecarga) {

        int totalLinhasBanco = contarLinhasBanco();

        String sql = "INSERT INTO pontoDeRecarga (nome, tipoDeLocal, endereco, tipoDeRecarga, qtdEstacoes, tipoConector, redeDeRecarga) VALUES (?, ?, ?, ?, ?, ?, ?)";

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
                        stmt.setInt(5, pontoRecarga.getQtdEstacoes());
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoDeLocal() {
        return tipoDeLocal;
    }

    public void setTipoDeLocal(String tipoDeLocal) {
        this.tipoDeLocal = tipoDeLocal;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTipoDeRecarga() {
        return tipoDeRecarga;
    }

    public void setTipoDeRecarga(String tipoDeRecarga) {
        this.tipoDeRecarga = tipoDeRecarga;
    }

    public Integer getQtdEstacoes() {
        return qtdEstacoes;
    }

    public void setQtdEstacoes(Integer qtdEstacoes) {
        this.qtdEstacoes = qtdEstacoes;
    }

    public String getTipoConector() {
        return tipoConector;
    }

    public void setTipoConector(String tipoConector) {
        this.tipoConector = tipoConector;
    }

    public String getRedeDeRecarga() {
        return redeDeRecarga;
    }

    public void setRedeDeRecarga(String redeDeRecarga) {
        this.redeDeRecarga = redeDeRecarga;
    }
}
