import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {

        int totalLinhasInseridas = 0;
        int totalLinhasBanco = contarLinhasBanco();
        logger.info("Relizado consulta ao banco de dados.");

        String nomeBucket = "bucket-emap";
        String nomeArquivo = "pontosRecarga.xlsx";

        String insertSQL = "INSERT INTO pontoRecarga (nome, tipoLocal, endereco, tipoRecarga, qtdEstacoes, tipoConector, rede) VALUES (?, ?, ?, ?, ?, ?, ?)";

        logger.info("Iniciando a leitura da planilha e inserção no banco de dados.");

        AmazonS3 s3Client = ConexaoBucket.getS3Client();

        try (S3Object s3Object = s3Client.getObject(nomeBucket, nomeArquivo);
              InputStream arquivo = s3Object.getObjectContent())
        {
//            Path caminho = Path.of("C:/Users/natha/emap/pontos-recarga.xlsx");
//            InputStream arquivo = Files.newInputStream(caminho);

            Workbook workbook = new XSSFWorkbook(arquivo);
            Sheet sheet = workbook.getSheetAt(0);

            Connection con = Conexao.getConnection();
            logger.info("Conexão com o banco de dados estabelecida com sucesso.");

            PreparedStatement stmt = con.prepareStatement(insertSQL);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    logger.debug("Ignorando a linha de cabeçalho.");
                    continue;
                }

                if (row.getRowNum() > totalLinhasBanco && totalLinhasInseridas < 100) {
                    try {
                        String colTipoLocal = row.getCell(1).getStringCellValue();
                        String colNome = row.getCell(2).getStringCellValue();
                        String colEndereco = row.getCell(3).getStringCellValue();
                        String colTipoRecarga = row.getCell(4).getStringCellValue();
                        String colQtdEstacoes = row.getCell(5).getStringCellValue();
                        String colTipoConector = row.getCell(6).getStringCellValue();
                        String colRede = row.getCell(10).getStringCellValue();

                        stmt.setString(1, colNome);
                        stmt.setString(2, colTipoLocal);
                        stmt.setString(3, colEndereco);
                        stmt.setString(4, colTipoRecarga);
                        stmt.setString(5, colQtdEstacoes);
                        stmt.setString(6, colTipoConector);
                        stmt.setString(7, colRede);

                        stmt.executeUpdate();
                        logger.debug("Linha inserida com sucesso: Nome - {}, Tipo Local - {}, Endereço - {}, Tipo Recarga - {}, Quantidade Estações {}, Tipo Conector - {}, Rede - {}", colNome, colTipoLocal, colEndereco, colTipoRecarga, colQtdEstacoes, colTipoConector, colRede);
                    } catch (Exception rowException) {
                        logger.error("Erro ao inserir a linha: {}", row.getRowNum(), rowException);
                    }

                    totalLinhasInseridas++;
                }
            }

            workbook.close();
            logger.info("Dados inseridos com sucesso no banco de dados.");
            logger.info("Arquivo de planilha fechado.");

        } catch (Exception e) {
            logger.error("Erro durante o processo de leitura e inserção de dados.", e);
        }
    }

    public static int contarLinhasBanco() {
        String sql = "SELECT COUNT(*) as totalLinhas FROM pontoRecarga";

        int totalLinhasBanco = 0;

        try (Connection con = Conexao.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                totalLinhasBanco = rs.getInt("totalLinhas");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalLinhasBanco;
    }
}