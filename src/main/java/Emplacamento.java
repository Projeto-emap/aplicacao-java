import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Emplacamento extends BaseDeDados {
    private String caminhoPlanilha;
    private String ano;
    private String mes;
    private String municipio;
    private String tipoCombustivel;
    private String procedenciaDoVeiculo;
    private Integer qtdVeiculos;

    public static final Logger logger = LoggerFactory.getLogger(Emplacamento.class);

    public Emplacamento () {}

    public Emplacamento (String ano, String mes, String municipio, String tipoCombustivel, String procedenciaDoVeiculo, Integer qtdVeiculos) {
        this.ano = ano;
        this.mes = mes;
        this.municipio = municipio;
        this.tipoCombustivel = tipoCombustivel;
        this.procedenciaDoVeiculo = procedenciaDoVeiculo;
        this.qtdVeiculos = qtdVeiculos;
    }

    @Override
    public Integer verificarQtdLinhasInseridas() {
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
        int totalLinhasBanco = verificarQtdLinhasInseridas();

        String sql = "INSERT INTO emplacamento (ano, mes, municipio, tipoCombustivel, procedencia, qtdVeiculos) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            int totalLinhasInseridas = 0;
            logger.info("Iniciando inserção de dados.");
            for (Emplacamento emplacamento : emplacamentos) {
                if (emplacamentos.indexOf(emplacamento) > totalLinhasBanco && totalLinhasInseridas < 100) {

                    try {

                        stmt.setString(1, emplacamento.getAno());
                        stmt.setString(2, emplacamento.getMes());
                        stmt.setString(3, emplacamento.getMunicipio());
                        stmt.setString(4, emplacamento.getTipoCombustivel());
                        stmt.setString(5, emplacamento.getProcedenciaDoVeiculo());
                        stmt.setInt(6, emplacamento.getQtdVeiculos());

                        stmt.executeUpdate();
                        logger.debug("Linha inserida com sucesso: Ano - {}, Mês - {}, Município - {}, Tipo de combustível - {}, Procedência do veículo - {}, Quantidade de veículos - {}", emplacamento.getAno(), emplacamento.getMes(), emplacamento.getMunicipio(), emplacamento.getTipoCombustivel(), emplacamento.getProcedenciaDoVeiculo(), emplacamento.getQtdVeiculos());

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

    public String getCaminhoPlanilha() {
        return caminhoPlanilha;
    }

    public void setCaminhoPlanilha(String caminhoPlanilha) {
        this.caminhoPlanilha = caminhoPlanilha;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(String tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    public String getProcedenciaDoVeiculo() {
        return procedenciaDoVeiculo;
    }

    public void setProcedenciaDoVeiculo(String procedenciaDoVeiculo) {
        this.procedenciaDoVeiculo = procedenciaDoVeiculo;
    }

    public Integer getQtdVeiculos() {
        return qtdVeiculos;
    }

    public void setQtdVeiculos(Integer qtdVeiculos) {
        this.qtdVeiculos = qtdVeiculos;
    }
}
