public class PontoRecarga {
    private String nome;
    private String tipoDeLocal;
    private Integer qtdestacoes;
    private String tipoConector;
    private String redeDeRecarga;

    public PontoRecarga(String nome, String tipoDeLocal, Integer qtdestacoes, String tipoConector, String redeDeRecarga) {
        this.nome = nome;
        this.tipoDeLocal = tipoDeLocal;
        this.qtdestacoes = qtdestacoes;
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

    public Integer getQtdestacoes() {
        return qtdestacoes;
    }

    public void setQtdestacoes(Integer qtdestacoes) {
        this.qtdestacoes = qtdestacoes;
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
}
