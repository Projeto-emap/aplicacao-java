import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("""
                    * * * * * * * * * * * * * * * * * * * *
                               A P L I C A Ç Ã O
                    * * * * * * * * * * * * * * * * * * * *
                    
                    1 - Ler planilha de Pontos de Recarga
                    2 - Ler planilha de Emplacamentos
                    3 - Sair
                    """);

            opcao = scanner.nextInt();

            if (opcao == 1) {
                PontoRecargaHandler pontoRecargaHandler = new PontoRecargaHandler();
                pontoRecargaHandler.carregarPlanilha("C:/Users/natha/emap/pontos-recarga.xlsx");

                pontoRecargaHandler.processarDados();

                PontoRecarga pontoRecarga = new PontoRecarga();
                pontoRecarga.inserirDados(pontoRecargaHandler.getPontos());

            } else if (opcao == 2){
                EmplacamentoHandler emplacamentoHandler = new EmplacamentoHandler();
                emplacamentoHandler.carregarPlanilha("C:/Users/natha/emap/emplacamentos.xlsx");

                emplacamentoHandler.processarDados();

                Emplacamento emplacamento = new Emplacamento();
                emplacamento.inserirDados(emplacamentoHandler.getEmplacamentos());

            } else {
                System.out.println("Testar outro cenario");
            }
        } while (opcao != 3);

//        String NOME_BUCKET = System.getenv("NOME_BUCKET");
//        String NOME_ARQUIVO = System.getenv("NOME_ARQUIVO");

//        List<PontoRecarga> pontosRecarga = leitorPlanilha.lerPlanilhaPontoRecargaBucket(NOME_BUCKET, NOME_ARQUIVO);

//        List<Emplacamento> emplacamentos = leitorPlanilha.lerPlanilhaPontoRecargaLocal("C:/Users/natha/emap/pontos-recarga.xlsx");

//        pontoRecarga.inserirPontoRecarga(pontosRecarga);

//        JSONObject json = new JSONObject();
//
//        json.put("text", "Fácil né? :shrug:");
//
//        Slack.sendMessage(json);


    }
}

