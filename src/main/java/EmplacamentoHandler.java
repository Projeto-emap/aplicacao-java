import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmplacamentoHandler extends LeitorPlanilha{

    private List<Emplacamento> emplacamentos = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(EmplacamentoHandler.class);

    @Override
    public void processarDados() {
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                logger.debug("Ignorando a linha de cabeçalho.");
                continue;
            }

            try {
                String colAno = String.valueOf(row.getCell(0).getNumericCellValue());
                String colMes = row.getCell(1).getStringCellValue();
                String colMunicipio = row.getCell(2).getStringCellValue();
                String colTipoCombustivel = row.getCell(4).getStringCellValue();
                String colProcedencia = row.getCell(5).getStringCellValue();
                Integer colQtdVeiculos = (int) row.getCell(6).getNumericCellValue();

                if (colMunicipio.equalsIgnoreCase("São Paulo")) {
                    Emplacamento emplacamento = new Emplacamento(colQtdVeiculos, colTipoCombustivel, colMes, colAno, colProcedencia, colMunicipio);
                    emplacamentos.add(emplacamento);

                    System.out.println(colMunicipio);
                }

            } catch (Exception rowException) {
                logger.error("Erro ao inserir a linha: {}", row.getRowNum(), rowException);
            }
        }
    }

    public List<Emplacamento> getEmplacamentos() {
        return emplacamentos;
    }
}
