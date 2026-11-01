package br.edu.unifesspa.facsi.estagio.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TermoCompromisso {

    private Long id;
    private LocalDate dataCriacao;
    private boolean remunerado;
    private BigDecimal valorRemuneracao;
    private BigDecimal valorAuxilioTransporte;
    private String status;
    private Estagio estagio;

    public TermoCompromisso(Estagio estagio, LocalDate dataCriacao) {
        this.estagio = estagio;
        this.dataCriacao = dataCriacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public boolean isRemunerado() {
        return remunerado;
    }

    public void setRemunerado(boolean remunerado) {
        this.remunerado = remunerado;
    }

    public BigDecimal getValorRemuneracao() {
        return valorRemuneracao;
    }

    public void setValorRemuneracao(BigDecimal valorRemuneracao) {
        this.valorRemuneracao = valorRemuneracao;
    }

    public BigDecimal getValorAuxilioTransporte() {
        return valorAuxilioTransporte;
    }

    public void setValorAuxilioTransporte(BigDecimal valorAuxilioTransporte) {
        this.valorAuxilioTransporte = valorAuxilioTransporte;
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
