import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PontoRecargaHandler extends LeitorPlanilha {

    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String numero;

    private List<PontoRecarga> pontos = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(PontoRecargaHandler.class);

    @Override
    public void processarDados() {
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                logger.debug("Ignorando a linha de cabeçalho.");
                continue;
            }

            try {

                String colNome = row.getCell(2).getStringCellValue();
                String colEndereco = row.getCell(3).getStringCellValue();
                String colQtdEstacoes = row.getCell(5).getStringCellValue();
                String colTipoConector = row.getCell(6).getStringCellValue();
                String colRede = row.getCell(10).getStringCellValue();

                Integer qtdEstacoes = formatarQtdEstacoes(colQtdEstacoes);
                String tipoConector = formatarTipoConector(colTipoConector);
                numero = extrairNumero(colEndereco);
                cep = extrairCep(colEndereco);

                if (cep != null && numero != null && tipoConector != null && !colRede.isEmpty()) {
                    PontoRecarga pontoRecarga = new PontoRecarga(colNome, cep, "", "", numero, qtdEstacoes, tipoConector, colRede);
                    pontos.add(pontoRecarga);
                }

            } catch (Exception rowException) {
                logger.error("Erro ao inserir a linha: {}", row.getRowNum(), rowException);
            }
        }
    }

    public Integer formatarQtdEstacoes(String qtdEstacoes) {
        String regex = "\\d";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(qtdEstacoes);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }

        return null;
    }

    public String formatarTipoConector(String tipoConector) {
        String regex1 = "\\bTipo \\d+\\b";
        String regex2 = "\\bCCS\\b";

        Pattern pattern1 = Pattern.compile(regex1);
        Matcher matcher1 = pattern1.matcher(tipoConector);

        Pattern pattern2 = Pattern.compile(regex2);
        Matcher matcher2 = pattern2.matcher(tipoConector);

        if (matcher1.find()) {
            return matcher1.group().toLowerCase().replaceAll("\\s", "");
        } else if (matcher2.find()) {
            return matcher2.group().toLowerCase().replaceAll("\\s", "");
        } else {
            return null;
        }
    }

    public String extrairNumero(String endereco) {

        String regex = ",\\s(\\d+)\\s";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(endereco);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    public String extrairCep(String endereco) {
        String regex = ",(\\d+-\\d+)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(endereco.replaceAll("\\s", ""));

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    public String getCep () {
        return cep;
    }

    public void setCep (String cep){
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro){
        this.logradouro = logradouro;
    }

    public String getBairro () {
        return bairro;
    }

    public void setBairro (String bairro){
        this.bairro = bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade){
        this.localidade = localidade;
    }

    public String getNumero () {
        return numero;
    }

    public void setNumero (String numero){
        this.numero = numero;
    }

    public List<PontoRecarga> getPontos() {
        return pontos;
    }
}


