import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

                String anoEmplacamento = tratarAnoEmplacamento(colAno);

                if (colTipoCombustivel.equalsIgnoreCase("El Font In") || colTipoCombustivel.equalsIgnoreCase("El Font Ex")) {

                    if (colMunicipio.equalsIgnoreCase("São Paulo")) {
                        Emplacamento emplacamento = new Emplacamento(colQtdVeiculos, colTipoCombustivel, colMes, anoEmplacamento, colProcedencia, colMunicipio);
                        emplacamentos.add(emplacamento);
                    }
                }

            } catch (Exception rowException) {
                logger.error("Erro ao processar a linha: {}", row.getRowNum(), rowException);
            }
        }
    }

    public String tratarAnoEmplacamento(String anoEmplacamento) {
        String regex = "\\d+(?=\\.)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(anoEmplacamento);

        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public List<Emplacamento> getEmplacamentos() {
        return emplacamentos;
    }
}
