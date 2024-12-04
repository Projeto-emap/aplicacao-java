import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PontoRecarga {
    private String nome;
    private String cep;
    private String bairro;
    private String logradouro;
    private String numero;
    private Integer qtdEstacoes;
    private String tipoConector;
    private String redeDeRecarga;

    private static final Logger logger = LoggerFactory.getLogger(PontoRecarga.class);

    public PontoRecarga () {}

    public PontoRecarga(String nome, String cep, String bairro, String logradouro, String numero, Integer qtdEstacoes, String tipoConector, String redeDeRecarga) {
        this.nome = nome;
        this.cep = cep;
        this.bairro = bairro;
        this.logradouro = logradouro;
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

        String sqlVerificar = """
                SELECT COUNT(*)
                FROM pontoDeRecarga
                WHERE nome = ? AND cep = ? AND numero = ?
        """;

        String sqlInserir = "INSERT INTO pontoDeRecarga (nome, cep, bairro, rua, numero, qtdEstacoes, tipoConector, redeDeRecarga) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmtVerificar = con.prepareStatement(sqlVerificar);
             PreparedStatement stmtInserir = con.prepareStatement(sqlInserir)) {

            int totalLinhasInseridas = 0;
            logger.info("Iniciando inserção de dados.");
            for (PontoRecarga pontoRecarga : pontosRecarga) {

                stmtVerificar.setString(1, pontoRecarga.getNome());
                stmtVerificar.setString(2, pontoRecarga.getCep());
                stmtVerificar.setString(3, pontoRecarga.getNumero());

                if (totalLinhasInseridas < 10) {

                    try (ResultSet rs = stmtVerificar.executeQuery()) {
                        if (rs.next() && rs.getInt(1) == 0) {

                            if (!pontoRecarga.getRedeDeRecarga().isEmpty()) {

                                extrairEndereco(pontoRecarga);

                                stmtInserir.setString(1, pontoRecarga.getNome());
                                stmtInserir.setString(2, pontoRecarga.getCep());
                                stmtInserir.setString(3, pontoRecarga.getBairro());
                                stmtInserir.setString(4, pontoRecarga.getLogradouro());
                                stmtInserir.setString(5, pontoRecarga.getNumero());
                                stmtInserir.setInt(6, pontoRecarga.getQtdEstacoes());
                                stmtInserir.setString(7, pontoRecarga.getTipoConector());
                                stmtInserir.setString(8, pontoRecarga.getRedeDeRecarga());

                                stmtInserir.executeUpdate();

                                logger.debug("Linha inserida com sucesso: Nome - {}, CEP - {}, Bairro - {}, Rua - {}, Número {}, Quantidade de Estações - {}, Tipo de Conector - {}, Rede de Recarga {}", pontoRecarga.getNome(), pontoRecarga.getCep(), pontoRecarga.getBairro(), pontoRecarga.getLogradouro(), pontoRecarga.getNumero(), pontoRecarga.getQtdEstacoes(), pontoRecarga.getTipoConector(), pontoRecarga.getRedeDeRecarga());

                                totalLinhasInseridas++;
                            }

                        }
                    }
                }
            }

            if (totalLinhasInseridas > 0) {
                Slack slack = new Slack();
                slack.enviarMensagemPontoRecarga();
            }

        } catch (SQLException e) {
            logger.error("Erro ao se conectar com o banco de dados: {}", e.getMessage());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void extrairEndereco(PontoRecarga pontoRecarga) {
        String url = "https://viacep.com.br/ws/" + pontoRecarga.getCep() + "/json/";

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {

                ObjectMapper mapper = new ObjectMapper();
                PontoRecarga pontoRecargaMapper = mapper.readValue(response.body(), PontoRecarga.class);

                String logradouro = pontoRecargaMapper.getLogradouro();
                String bairro = pontoRecargaMapper.getBairro();

                pontoRecarga.setLogradouro(logradouro);
                pontoRecarga.setBairro(bairro);

            } else {
                System.out.println("Erro na consulta do CEP. Código: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
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

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String rua) {
        this.logradouro = rua;
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
