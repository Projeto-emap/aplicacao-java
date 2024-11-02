import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PontoRecarga {
    private String nome;
    private String tipoDeLocal;
    private String endereco;
    private String tipoDeRecarga;
    private String qtdEstacoes;
    private String tipoConector;
    private String redeDeRecarga;

    public PontoRecarga () {}

    public PontoRecarga(String nome, String tipoDeLocal, String endereco, String tipoDeRecarga, String qtdEstacoes, String tipoConector, String redeDeRecarga) {
        this.nome = nome;
        this.tipoDeLocal = tipoDeLocal;
        this.endereco = endereco;
        this.tipoDeRecarga = tipoDeRecarga;
        this.qtdEstacoes = qtdEstacoes;
        this.tipoConector = tipoConector;
        this.redeDeRecarga = redeDeRecarga;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoDeLocal() {
        return tipoDeLocal;
    }

    public void setTipoDeLocal(String tipoDeLocal) {
        this.tipoDeLocal = tipoDeLocal;
    }

    public String getQtdestacoes() {
        return qtdEstacoes;
    }

    public void setQtdestacoes(String qtdestacoes) {
        this.qtdEstacoes = qtdestacoes;
    }

    public String getTipoConector() {
        return tipoConector;
    }

    public void setTipoConector(String tipoConector) {
        this.tipoConector = tipoConector;
    }

    public String getRedeDeRecarga() {
        return redeDeRecarga;
    }

    public void setRedeDeRecarga(String redeDeRecarga) {
        this.redeDeRecarga = redeDeRecarga;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTipoDeRecarga() {
        return tipoDeRecarga;
    }

    public void setTipoDeRecarga(String tipoDeRecarga) {
        this.tipoDeRecarga = tipoDeRecarga;
    }

    public String getQtdEstacoes() {
        return qtdEstacoes;
    }

    public void setQtdEstacoes(String qtdEstacoes) {
        this.qtdEstacoes = qtdEstacoes;
    }
}
