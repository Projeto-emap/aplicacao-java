import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PontoRecarga {
    private String nome;
    private String cep;
    private String bairro;
    private String rua;
    private String numero;
    private Integer qtdEstacoes;
    private String tipoConector;
    private String redeDeRecarga;

    private static final Logger logger = LoggerFactory.getLogger(PontoRecarga.class);

    public PontoRecarga () {}

    public PontoRecarga(String nome, String cep, String bairro, String rua, String numero, Integer qtdEstacoes, String tipoConector, String redeDeRecarga) {
        this.nome = nome;
        this.cep = cep;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.qtdEstacoes = qtdEstacoes;
        this.tipoConector = tipoConector;
        this.redeDeRecarga = redeDeRecarga;
    }

    public Integer obterLinhasInseridas() {
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

    public void inserirDados(List<PontoRecarga> pontosRecarga) {

        int totalLinhasBanco = obterLinhasInseridas();

        String sql = "INSERT INTO pontoDeRecarga (nome, cep, bairro, rua, numero, qtdEstacoes, tipoConector, redeDeRecarga) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            int totalLinhasInseridas = 0;
            logger.info("Iniciando inserção de dados.");
            for (PontoRecarga pontoRecarga : pontosRecarga) {
                if (pontosRecarga.indexOf(pontoRecarga) > totalLinhasBanco) {

                    try {

                        stmt.setString(1, pontoRecarga.getNome());
                        stmt.setString(2, pontoRecarga.getCep());
                        stmt.setString(3, pontoRecarga.getBairro());
                        stmt.setString(4, pontoRecarga.getRua());
                        stmt.setString(5, pontoRecarga.getNumero());
                        stmt.setInt(6, pontoRecarga.getQtdEstacoes());
                        stmt.setString(7, pontoRecarga.getTipoConector());
                        stmt.setString(8, pontoRecarga.getRedeDeRecarga());

                        stmt.executeUpdate();
                        logger.debug("Linha inserida com sucesso: Nome - {}, CEP - {}, Bairro - {}, Rua - {}, Número {}, Quantidade de Estações - {}, Tipo de Conector - {}, Rede de Recarga {}", pontoRecarga.getNome(), pontoRecarga.getCep(), pontoRecarga.getBairro(), pontoRecarga.getRua(), pontoRecarga.getNumero(), pontoRecarga.getQtdEstacoes(), pontoRecarga.getTipoConector(), pontoRecarga.getRedeDeRecarga());

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

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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
