import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Emplacamento {
    private Integer qtdVeiculos;
    private String tipoCombustivel;
    private String mesEmplacamento;
    private String anoEmplacamento;
    private String procedencia;
    private String municipio;

    public static final Logger logger = LoggerFactory.getLogger(Emplacamento.class);

    public Emplacamento () {}

    public Emplacamento (Integer qtdVeiculos, String tipoCombustivel, String mesEmplacamento, String anoEmplacamento, String procedencia, String municipio) {
        this.qtdVeiculos = qtdVeiculos;
        this.tipoCombustivel = tipoCombustivel;
        this.mesEmplacamento = mesEmplacamento;
        this.anoEmplacamento = anoEmplacamento;
        this.procedencia = procedencia;
        this.municipio = municipio;
    }

    public Integer obterLinhasInseridas() {
        String sql = "SELECT COUNT(*) as totalLinhas FROM emplacamento";
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

    public void inserirDados(List<Emplacamento> emplacamentos) {
        int totalLinhasBanco = obterLinhasInseridas();

        String sql = "INSERT INTO emplacamento (qtdCarros, tipoCombustivel, mesEmplacamento, anoEmplacamento, procedencia) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            int totalLinhasInseridas = 0;
            logger.info("Iniciando inserção de dados.");
            for (Emplacamento emplacamento : emplacamentos) {
                if (emplacamentos.indexOf(emplacamento) > totalLinhasBanco && totalLinhasInseridas < 1000) {

                    try {

                        stmt.setInt(1, emplacamento.getQtdVeiculos());
                        stmt.setString(2, emplacamento.getTipoCombustivel());
                        stmt.setString(3, emplacamento.getMesEmplacamento());
                        stmt.setString(4, emplacamento.getAnoEmplacamento());
                        stmt.setString(5, emplacamento.getProcedencia());

                        stmt.executeUpdate();
                        logger.debug("Linha inserida com sucesso: Quantidade de veículos - {}, Tipo de combustível - {}, Mês - {}, Ano - {},  Procedência do veículo - {}", emplacamento.getQtdVeiculos() , emplacamento.getTipoCombustivel(), emplacamento.getMesEmplacamento(), emplacamento.getAnoEmplacamento(), emplacamento.getProcedencia());

                    } catch (SQLException e) {
                        logger.error("Erro ao inserir emplacamento: {}", e.getMessage());
                    }

                    totalLinhasInseridas++;
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao se conectar com o banco de dados: {}", e.getMessage());
        }
    }

    public Integer getQtdVeiculos() {
        return qtdVeiculos;
    }

    public void setQtdVeiculos(Integer qtdVeiculos) {
        this.qtdVeiculos = qtdVeiculos;
    }


    public String getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(String tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    public String getMesEmplacamento() {
        return mesEmplacamento;
    }

    public void setMesEmplacamento(String mesEmplacamento) {
        this.mesEmplacamento = mesEmplacamento;
    }

    public String getAnoEmplacamento() {
        return anoEmplacamento;
    }

    public void setAnoEmplacamento(String anoEmplacamento) {
        this.anoEmplacamento = anoEmplacamento;
    }

    public String getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
}
