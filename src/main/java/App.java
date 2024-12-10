import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {

        String NOME_BUCKET = System.getenv("NOME_BUCKET");

        PontoRecargaHandler pontoRecargaHandler = new PontoRecargaHandler();
        pontoRecargaHandler.carregarPlanilha("C:/Users/natha/emap/pontos-recarga.xlsx");
//        pontoRecargaHandler.carregarPlanilhaBucket(NOME_BUCKET, "pontos-recarga.xlsx");
//
        pontoRecargaHandler.processarDados();

        PontoRecarga pontoRecarga = new PontoRecarga();
        pontoRecarga.inserirDados(pontoRecargaHandler.getPontos());


        EmplacamentoHandler emplacamentoHandler = new EmplacamentoHandler();
        emplacamentoHandler.carregarPlanilha("C:/Users/natha/emap/emplacamentos.xlsx");
//        emplacamentoHandler.carregarPlanilhaBucket(NOME_BUCKET, "emplacamentos.xlsx");
//
        emplacamentoHandler.processarDados();

        Emplacamento emplacamento = new Emplacamento();
        emplacamento.inserirDados(emplacamentoHandler.getEmplacamentos());

    }
}

