package br.edu.unifesspa.facsi.estagio.model;

public class PlanoAtividades {

    private Long id;
    private String areaAtuacao;
    private String justificativa;
    private String objetivoGeral;
    private String objetivosEspecificos;
    private String descricaoAtividades;
    private String adequacaoPedagogica;
    private String cronograma;
    private String status;
    private Estagio estagio;

    public PlanoAtividades(Estagio estagio, String areaAtuacao) {
        this.estagio = estagio;
        this.areaAtuacao = areaAtuacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreaAtuacao() {
        return areaAtuacao;
    }

    public void setAreaAtuacao(String areaAtuacao) {
        this.areaAtuacao = areaAtuacao;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public String getObjetivoGeral() {
        return objetivoGeral;
    }

    public void setObjetivoGeral(String objetivoGeral) {
        this.objetivoGeral = objetivoGeral;
    }

    public String getObjetivosEspecificos() {
        return objetivosEspecificos;
    }

    public void setObjetivosEspecificos(String objetivosEspecificos) {
        this.objetivosEspecificos = objetivosEspecificos;
    }

    public String getDescricaoAtividades() {
        return descricaoAtividades;
    }

    public void setDescricaoAtividades(String descricaoAtividades) {
        this.descricaoAtividades = descricaoAtividades;
    }

    public String getAdequacaoPedagogica() {
        return adequacaoPedagogica;
    }

    public void setAdequacaoPedagogica(String adequacaoPedagogica) {
        this.adequacaoPedagogica = adequacaoPedagogica;
    }

    public String getCronograma() {
        return cronograma;
    }

    public void setCronograma(String cronograma) {
        this.cronograma = cronograma;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Estagio getEstagio() {
        return estagio;
    }

    public void setEstagio(Estagio estagio) {
        this.estagio = estagio;
    }
}
