package br.edu.unifesspa.facsi.estagio.model;

import java.time.LocalDate;

public class RelatorioFinal {

    private Long id;
    private String temaObjeto;
    private String justificativa;
    private String objetivos;
    private String metodologia;
    private String atividadesRealizadas;
    private String dificuldadesEncontradas;
    private String conclusao;
    private LocalDate dataEntrega;
    private Estagio estagio;

    public RelatorioFinal(Estagio estagio, String temaObjeto, LocalDate dataEntrega) {
        this.estagio = estagio;
        this.temaObjeto = temaObjeto;
        this.dataEntrega = dataEntrega;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemaObjeto() {
        return temaObjeto;
    }

    public void setTemaObjeto(String temaObjeto) {
        this.temaObjeto = temaObjeto;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public String getMetodologia() {
        return metodologia;
    }

    public void setMetodologia(String metodologia) {
        this.metodologia = metodologia;
    }

    public String getAtividadesRealizadas() {
        return atividadesRealizadas;
    }

    public void setAtividadesRealizadas(String atividadesRealizadas) {
        this.atividadesRealizadas = atividadesRealizadas;
    }

    public String getDificuldadesEncontradas() {
        return dificuldadesEncontradas;
    }

    public void setDificuldadesEncontradas(String dificuldadesEncontradas) {
        this.dificuldadesEncontradas = dificuldadesEncontradas;
    }

    public String getConclusao() {
        return conclusao;
    }

    public void setConclusao(String conclusao) {
        this.conclusao = conclusao;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public Estagio getEstagio() {
        return estagio;
    }

    public void setEstagio(Estagio estagio) {
        this.estagio = estagio;
    }
}
