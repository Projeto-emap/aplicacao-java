import java.util.List;

public class Main {

    public static void main(String[] args) {

        String NOME_BUCKET = System.getenv("NOME_BUCKET");
        String NOME_ARQUIVO = System.getenv("NOME_ARQUIVO");
        LeitorPlanilha leitorPlanilha = new LeitorPlanilha();
        List<PontoRecarga> pontosRecarga = leitorPlanilha.lerPlanilhaBucket(NOME_BUCKET, NOME_ARQUIVO);

        PontoRecargaDAO pontoRecargaDAO = new PontoRecargaDAO();
        pontoRecargaDAO.inserirPontoRecarga(pontosRecarga);

    }
}

