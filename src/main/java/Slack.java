import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Slack {

    private final List<String> mensagensEmplacamento;
    private final List<String> mensagensPontoRecarga;
    private Random random;


    private static HttpClient client = HttpClient.newHttpClient();
    private static final String URL = System.getenv("URL_SLACK");

    public Slack() {
        this.random = new Random();

        this.mensagensEmplacamento = new ArrayList<>();
        mensagensEmplacamento.add("🚗 Novos emplacamentos adicionados! Consulte os dados para mais insights.");
        mensagensEmplacamento.add("📊 Atualização concluída: mais emplacamentos foram registrados no sistema.");
        mensagensEmplacamento.add("⚡ Novos emplacamentos detectados!");

        this.mensagensPontoRecarga = new ArrayList<>();
        mensagensPontoRecarga.add("🔋 Novos pontos de recarga adicionados! Explore as novas oportunidades disponíveis.");
        mensagensPontoRecarga.add("📍 Atualização concluída: mais locais pontos de recarga foram registrados no sistema.");
        mensagensPontoRecarga.add("⚡ Novos registros de pontos de recarga disponíveis!");
    }

    public String getMensagemAleatoria(List<String> mensagens) {
        return mensagens.get(random.nextInt(mensagens.size()));
    }

    public void enviarMensagemEmplacamento() throws Exception {
        String mensagem = getMensagemAleatoria(mensagensEmplacamento);
        enviarMensagemParaSlack(mensagem);
    }

    public void enviarMensagemPontoRecarga() throws Exception {
        String mensagem = getMensagemAleatoria(mensagensPontoRecarga);
        enviarMensagemParaSlack(mensagem);
    }

    private void enviarMensagemParaSlack(String mensagem) {
        JSONObject payload = criarPayloadJson(mensagem);

        try {
            sendMessage(payload);
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao enviar mensagem ao Slack: " + e.getMessage());
        }
    }

    private JSONObject criarPayloadJson(String mensagem) {
        JSONObject payload = new JSONObject();
        payload.put("text", mensagem);
        return payload;
    }


    public static void sendMessage(JSONObject content) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(URL))
                .header("accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(content.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Mensagem enviada com sucesso!");
        } else {
            System.err.println("Erro ao enviar mensagem ao Slack. Código: " + response.statusCode());
            System.err.println("Resposta: " + response.body());
        }
    }
}
