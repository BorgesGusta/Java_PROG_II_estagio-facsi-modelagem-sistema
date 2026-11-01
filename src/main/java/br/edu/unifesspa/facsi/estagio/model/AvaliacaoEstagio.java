package br.edu.unifesspa.facsi.estagio.model;

import java.time.LocalDate;

public class AvaliacaoEstagio {

    private Long id;
    private LocalDate dataAvaliacao;
    private String parecerSupervisor;
    private String parecerOrientador;
    private String resultado;
    private String observacao;
    private Estagio estagio;

    public AvaliacaoEstagio(Estagio estagio, LocalDate dataAvaliacao) {
        this.estagio = estagio;
        this.dataAvaliacao = dataAvaliacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(LocalDate dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    public String getParecerSupervisor() {
        return parecerSupervisor;
    }

    public void setParecerSupervisor(String parecerSupervisor) {
        this.parecerSupervisor = parecerSupervisor;
    }

    public String getParecerOrientador() {
        return parecerOrientador;
    }

    public void setParecerOrientador(String parecerOrientador) {
        this.parecerOrientador = parecerOrientador;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Estagio getEstagio() {
        return estagio;
    }

    public void setEstagio(Estagio estagio) {
        this.estagio = estagio;
    }
}
