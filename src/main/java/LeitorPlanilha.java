import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class LeitorPlanilha {
    protected Workbook workbook;

    private Logger logger = LoggerFactory.getLogger(LeitorPlanilha.class);

    public void carregarPlanilha(String caminhoPlanilha) {
        try {
            logger.info("Iniciando carregamento da planilha");
            Path caminho = Path.of(caminhoPlanilha);
            InputStream arquivo = Files.newInputStream(caminho);

            this.workbook = new XSSFWorkbook(arquivo);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar a planilha: " + caminhoPlanilha, e);
        }
    }

    public abstract void processarDados();

}
