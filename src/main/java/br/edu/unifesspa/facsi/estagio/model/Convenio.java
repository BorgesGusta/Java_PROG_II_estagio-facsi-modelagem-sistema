package br.edu.unifesspa.facsi.estagio.model;

import java.time.LocalDate;

public class Convenio {

    private Long id;
    private String numeroReferencia;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String status;
    private Concedente concedente;

    public Convenio(String numeroReferencia, Concedente concedente) {
        this.numeroReferencia = numeroReferencia;
        this.concedente = concedente;
    }

    public boolean estaVigenteEm(LocalDate data) {
        if (data == null || dataInicio == null || dataFim == null) {
            return false;
        }
        return !data.isBefore(dataInicio) && !data.isAfter(dataFim);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Concedente getConcedente() {
        return concedente;
    }

    public void setConcedente(Concedente concedente) {
        this.concedente = concedente;
    }
}
