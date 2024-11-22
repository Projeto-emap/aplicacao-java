import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeitorPlanilha {
    private List<PontoRecarga> pontosRecarga = new ArrayList<>();

    private Logger logger = LoggerFactory.getLogger(LeitorPlanilha.class);

    public List<PontoRecarga> lerPlanilhaLocal(String caminhoPlanilha) {
        try {

            logger.info("Iniciando leitura da planilha.");
            Path caminho = Path.of(caminhoPlanilha);
            InputStream arquivo = Files.newInputStream(caminho);

            Workbook workbook = new XSSFWorkbook(arquivo);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    logger.debug("Ignorando a linha de cabeçalho.");
                    continue;
                }

                try {

                    String colTipoLocal = row.getCell(1).getStringCellValue();
                    String colNome = row.getCell(2).getStringCellValue();
                    String colEndereco = row.getCell(3).getStringCellValue();
                    String colTipoRecarga = row.getCell(4).getStringCellValue();
                    String colQtdEstacoes = row.getCell(5).getStringCellValue();
                    String colTipoConector = row.getCell(6).getStringCellValue();
                    String colRede = row.getCell(10).getStringCellValue();

                    Integer qtdEstacoes = tratarDadosQtdEstacoes(colQtdEstacoes);

                    PontoRecarga pontoRecarga = new PontoRecarga(colNome, colTipoLocal, colEndereco, colTipoRecarga, qtdEstacoes, colTipoConector, colRede);
                    pontosRecarga.add(pontoRecarga);

                } catch (Exception rowException) {
                    logger.error("Erro ao inserir a linha: {}", row.getRowNum(), rowException);
                }
            }

            workbook.close();
            logger.info("Arquivo de planilha fechado.");

        } catch (IOException e) {
            logger.error("Erro ao tentar acessar o arquivo: {}", e.getMessage());
        }

        return pontosRecarga;
    }

    public List<PontoRecarga> lerPlanilhaBucket(String nomeBucket, String nomeArquivo) {

        AmazonS3 s3Client = ConexaoBucket.getS3Client();

        try (S3Object s3Object = s3Client.getObject(nomeBucket, nomeArquivo);
             InputStream arquivo = s3Object.getObjectContent()) {

            logger.info("Iniciando leitura da planilha.");

            Workbook workbook = new XSSFWorkbook(arquivo);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    logger.debug("Ignorando a linha de cabeçalho.");
                    continue;
                }

                try {

                    String colTipoLocal = row.getCell(1).getStringCellValue();
                    String colNome = row.getCell(2).getStringCellValue();
                    String colEndereco = row.getCell(3).getStringCellValue();
                    String colTipoRecarga = row.getCell(4).getStringCellValue();
                    String colQtdEstacoes = row.getCell(5).getStringCellValue();
                    String colTipoConector = row.getCell(6).getStringCellValue();
                    String colRede = row.getCell(10).getStringCellValue();

                    Integer qtdEstacoes = tratarDadosQtdEstacoes(colQtdEstacoes);

                    PontoRecarga pontoRecarga = new PontoRecarga(colNome, colTipoLocal, colEndereco, colTipoRecarga, qtdEstacoes, colTipoConector, colRede);
                    pontosRecarga.add(pontoRecarga);
                    logger.debug("Linha lida com sucesso: Nome - {}, Tipo Local - {}, Endereço - {}, Tipo Recarga - {}, Quantidade Estações {}, Tipo Conector - {}, Rede - {}", colNome, colTipoLocal, colEndereco, colTipoRecarga, colQtdEstacoes, colTipoConector, colRede);

                } catch (Exception rowException) {
                    logger.error("Erro ao inserir a linha: {}", row.getRowNum(), rowException);
                }
            }

            workbook.close();
            logger.info("Arquivo de planilha fechado.");

        } catch (Exception e) {
            logger.error("Erro durante o processo de inserção de dados: {}", e.getMessage());
        }

        return pontosRecarga;
    }

    public Integer tratarDadosQtdEstacoes(String qtdEstacoes) {
        String regex = "\\d";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(qtdEstacoes);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }

        return null;
    }

}
