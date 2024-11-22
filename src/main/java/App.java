import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {

//        String NOME_BUCKET = System.getenv("NOME_BUCKET");
//        String NOME_ARQUIVO = System.getenv("NOME_ARQUIVO");

        LeitorPlanilha leitorPlanilha = new LeitorPlanilha();
//        List<PontoRecarga> pontosRecarga = leitorPlanilha.lerPlanilhaBucket(NOME_BUCKET, NOME_ARQUIVO);
        List<PontoRecarga> pontosRecarga = leitorPlanilha.lerPlanilhaLocal("C:/Users/natha/emap/pontos-recarga.xlsx");

        PontoRecarga pontoRecarga = new PontoRecarga();
        pontoRecarga.inserirPontoRecarga(pontosRecarga);

//        JSONObject json = new JSONObject();
//
//        json.put("text", "Fácil né? :shrug:");
//
//        Slack.sendMessage(json);


    }
}

