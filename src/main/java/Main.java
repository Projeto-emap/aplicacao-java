import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class Main {
    public static void main(String[] args) throws IOException {

        String insertSQL = "INSERT INTO pontoRecarga (nome, tipoLocal, endereco, tipoRecarga, qtdEstacoes, tipoConector, rede) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Path caminho = Path.of("C:/Users/natha/emap/pontos-recarga.xlsx");
            InputStream arquivo = Files.newInputStream(caminho);

            Workbook workbook = new XSSFWorkbook(arquivo);

            Sheet sheet = workbook.getSheetAt(0);

            Connection con = Conexao.getConnection();
            PreparedStatement stmt = con.prepareStatement(insertSQL);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                String colTipoLocal = row.getCell(1).getStringCellValue();
                String colNome = row.getCell(2).getStringCellValue();
                String colEndereco = row.getCell(3).getStringCellValue();
                String colTipoRecarga = row.getCell(4).getStringCellValue();
                String colQtdEstacoes = row.getCell(5).getStringCellValue();
                String colTipoConector = row.getCell(6).getStringCellValue();
                String colRede = row.getCell(7).getStringCellValue();

                stmt.setString(1, colNome);
                stmt.setString(2, colTipoLocal);
                stmt.setString(3, colEndereco);
                stmt.setString(4, colTipoRecarga);
                stmt.setString(5, colQtdEstacoes);
                stmt.setString(6, colTipoConector);
                stmt.setString(7, colRede);

                stmt.executeUpdate();
            }
            System.out.println("Dados inseridos com sucesso no banco!");
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}