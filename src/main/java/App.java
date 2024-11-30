import java.io.IOException;
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

            LeitorPlanilha leitorPlanilha = new LeitorPlanilha();

            if (opcao == 1) {
                PontoRecarga pontoRecarga = new PontoRecarga();
                pontoRecarga.setCaminhoPlanilha("C:/Users/natha/emap/pontos-recarga.xlsx");

                List<PontoRecarga> pontosRecarga = leitorPlanilha.lerPlanilhaPontoRecargaLocal(pontoRecarga.getCaminhoPlanilha());

                List<PontoRecarga> novosPontos = leitorPlanilha.lerPlanilhaPontoRecargaLocal(pontoRecarga.getCaminhoPlanilha());
                pontoRecarga.inserirDados(novosPontos);
            } else if (opcao == 2){
                Emplacamento emplacamento = new Emplacamento();
                emplacamento.setCaminhoPlanilha("C:/Users/natha/emap/emplacamentos.xlsx");

                List<Emplacamento> emplacamentos = leitorPlanilha.lerPlanilhaEmplacamentoLocal(emplacamento.getCaminhoPlanilha());

                List<Emplacamento> novoEmplacamento = leitorPlanilha.lerPlanilhaEmplacamentoLocal(emplacamento.getCaminhoPlanilha());
                emplacamento.inserirDados(novoEmplacamento);
            } else {
                Endereco endereco = new Endereco();

                endereco.extrairEndereco("07713-600");
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

