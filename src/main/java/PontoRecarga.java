import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
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

        int totalLinhasBanco = obterLinhasInseridas();

        String sqlVerificar = """
                SELECT COUNT(*)
                FROM pontoDeRecarga
                WHERE nome = ? AND cep = ? AND numero = ?
        """;

        String sqlInserir = "INSERT INTO pontoDeRecarga (nome, cep, bairro, rua, numero, qtdEstacoes, tipoConector, redeDeRecarga) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PontoRecargaHandler pontoRecargaHandler = new PontoRecargaHandler();
//        pontoRecargaHandler.extrairEndereco(cep);

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmtVerificar = con.prepareStatement(sqlVerificar);
             PreparedStatement stmtInserir = con.prepareStatement(sqlInserir)) {

            int totalLinhasInseridas = 0;
            logger.info("Iniciando inserção de dados.");
            for (PontoRecarga pontoRecarga : pontosRecarga) {
//                if (pontosRecarga.indexOf(pontoRecarga) > totalLinhasBanco && totalLinhasInseridas < 10) {

                stmtVerificar.setString(1, pontoRecarga.getNome());
                stmtVerificar.setString(2, pontoRecarga.getCep());
                stmtVerificar.setString(3, pontoRecarga.getNumero());

                if (totalLinhasInseridas < 10) {

                    try (ResultSet rs = stmtVerificar.executeQuery()) {
                        if (rs.next() && rs.getInt(1) == 0) {
                            logger.debug("Registro não encontrado no banco, preparando para inserção.");

                            extrairEndereco(cep);

                            stmtInserir.setString(1, pontoRecarga.getNome());
                            stmtInserir.setString(2, pontoRecarga.getCep());
                            stmtInserir.setString(3, pontoRecarga.getBairro());
                            stmtInserir.setString(4, pontoRecarga.getRua());
                            stmtInserir.setString(5, pontoRecarga.getNumero());
                            stmtInserir.setInt(6, pontoRecarga.getQtdEstacoes());
                            stmtInserir.setString(7, pontoRecarga.getTipoConector());
                            stmtInserir.setString(8, pontoRecarga.getRedeDeRecarga());

                            stmtInserir.executeUpdate();
//                        logger.info("Registro inserido com sucesso: {}", pontoRecarga);
                            logger.debug("Linha inserida com sucesso: Nome - {}, CEP - {}, Bairro - {}, Rua - {}, Número {}, Quantidade de Estações - {}, Tipo de Conector - {}, Rede de Recarga {}", pontoRecarga.getNome(), pontoRecarga.getCep(), pontoRecarga.getBairro(), pontoRecarga.getRua(), pontoRecarga.getNumero(), pontoRecarga.getQtdEstacoes(), pontoRecarga.getTipoConector(), pontoRecarga.getRedeDeRecarga());

                            totalLinhasInseridas++;

                        } else {
                            logger.debug("Registro já existente: {}", pontoRecarga);
                        }
                    }
                }

//                    try {
//
//                        stmtInserir.setString(1, pontoRecarga.getNome());
//                        stmtInserir.setString(2, pontoRecarga.getCep());
//                        stmtInserir.setString(3, pontoRecarga.getBairro());
//                        stmtInserir.setString(4, pontoRecarga.getRua());
//                        stmtInserir.setString(5, pontoRecarga.getNumero());
//                        stmtInserir.setInt(6, pontoRecarga.getQtdEstacoes());
//                        stmtInserir.setString(7, pontoRecarga.getTipoConector());
//                        stmtInserir.setString(8, pontoRecarga.getRedeDeRecarga());
//
//                        stmtInserir.executeUpdate();
//                        logger.debug("Linha inserida com sucesso: Nome - {}, CEP - {}, Bairro - {}, Rua - {}, Número {}, Quantidade de Estações - {}, Tipo de Conector - {}, Rede de Recarga {}", pontoRecarga.getNome(), pontoRecarga.getCep(), pontoRecarga.getBairro(), pontoRecarga.getRua(), pontoRecarga.getNumero(), pontoRecarga.getQtdEstacoes(), pontoRecarga.getTipoConector(), pontoRecarga.getRedeDeRecarga());
//
//                        totalLinhasInseridas++;
//
//                    } catch (SQLException e) {
//                        logger.error("Erro ao inserir ponto de recarga: {}", e.getMessage());
//                    }

//                    totalLinhasInseridas++;
//                }
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

    public void extrairEndereco(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

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
                PontoRecarga pontoRecarga = mapper.readValue(response.body(), PontoRecarga.class);


                String logradouro = pontoRecarga.getRua();
                String bairro = pontoRecarga.getBairro();
//                String localidade = pontoRecargaHandler.getLocalidade();

                this.cep = cep;
                this.logradouro = logradouro;
                this.bairro = bairro;

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

    public String getRua() {
        return logradouro;
    }

    public void setRua(String rua) {
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
