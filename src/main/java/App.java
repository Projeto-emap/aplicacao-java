import java.io.IOException;
import java.util.Scanner;


public class App {

    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("""
                    * * * * * * * * * * * * * * * * * * * *
                               A P L I C A Ç Ã O
                    * * * * * * * * * * * * * * * * * * * *
                    
                    1 - Ler planilha de Pontos de Recarga - Local
                    2 - Ler planilha de Emplacamentos - Local
                    3 - Ler planilha de Pontos de Recarga - Bucket
                    4 - Ler planilha de Emplacamentos - Bucket
                    5 - Sair
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

            } else if (opcao == 3) {
              String NOME_BUCKET = System.getenv("NOME_BUCKET");

              PontoRecargaHandler pontoRecargaHandler = new PontoRecargaHandler();
              pontoRecargaHandler.carregarPlanilhaBucket(NOME_BUCKET, "pontos-recarga.xlsx");

              pontoRecargaHandler.processarDados();

              PontoRecarga pontoRecarga = new PontoRecarga();
              pontoRecarga.inserirDados(pontoRecargaHandler.getPontos());

            } else if (opcao == 4){
                String NOME_BUCKET = System.getenv("NOME_BUCKET");
//                String NOME_ARQUIVO = System.getenv("NOME_ARQUIVO");

                EmplacamentoHandler emplacamentoHandler = new EmplacamentoHandler();
                emplacamentoHandler.carregarPlanilhaBucket(NOME_BUCKET, "emplacamentos.xlsx");

                emplacamentoHandler.processarDados();

                Emplacamento emplacamento = new Emplacamento();
                emplacamento.inserirDados(emplacamentoHandler.getEmplacamentos());
            }
        } while (opcao != 5);


//        pontoRecarga.inserirPontoRecarga(pontosRecarga);

//        JSONObject json = new JSONObject();
//
//        json.put("text", "Fácil né? :shrug:");
//
//        Slack.sendMessage(json);


    }
}

