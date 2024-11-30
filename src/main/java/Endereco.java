import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Endereco {
    private String logradouro;
    private String bairro;
    private String localidade;

    public void extrairEndereco(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                Endereco endereco = mapper.readValue(response.body(), Endereco.class);

                String logradouro = endereco.getLogradouro();
                String bairro = endereco.getBairro();
                String localidade = endereco.getLocalidade();

                System.out.println("Logradouro: " + logradouro);
                System.out.println("Bairro: " + bairro);
                System.out.println("Cidade: " + localidade);

                inserir(logradouro, bairro, localidade);

            } else {
                System.out.println("Erro na consulta do CEP. Código: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void inserir(String logradouro, String bairro, String localidade) {
        String sql = "INSERT INTO endereco (logradouro, bairro, localidade) VALUES (?, ?, ?)";

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, logradouro);
            stmt.setString(2, bairro);
            stmt.setString(3, localidade);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir endereco: " + e.getMessage());
        }
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }
}
