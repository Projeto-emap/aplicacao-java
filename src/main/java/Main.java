import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;


public class Main {
    public static void main(String[] args) throws IOException {
//        UsuarioDAO usuarioDAO = new UsuarioDAO();
//
//        usuarioDAO.consultarUsuario(1);

        try {
            Path caminho = Path.of("C:/Users/natha/emap/pontos-recarga.xlsx");
            InputStream arquivo = Files.newInputStream(caminho);

            Workbook workbook = new XSSFWorkbook(arquivo);

            // Acessando a primeira planilha
            Sheet sheet = workbook.getSheetAt(0);
            System.out.println("Planilha lida com sucesso!");

            // Iterando pelas linhas da planilha
            for (Row row : sheet) {
                // Iterando pelas células de cada linha
                for (Cell cell : row) {
                    // Obtendo o valor da célula como String, dependendo do tipo
                    switch (cell.getCellType()) {
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "\t");
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                System.out.print(cell.getDateCellValue() + "\t");
                            } else {
                                System.out.print(cell.getNumericCellValue() + "\t");
                            }
                            break;
                        case BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t");
                            break;
                        case FORMULA:
                            System.out.print(cell.getCellFormula() + "\t");
                            break;
                        default:
                            System.out.print(" ");
                    }
                }
                // Pula para a próxima linha
                System.out.println();
            }

//        // Acessando a primeira linha da planilha
//        Row row = sheet.getRow(0);
//
//        // Acessando a primeira célula da linha
//        Cell cell = row.getCell(0);
//
//        String valor = cell.getStringCellValue();

            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}