package br.edu.unifesspa.facsi.estagio.model;

import br.edu.unifesspa.facsi.estagio.enums.ModalidadeEstagio;

import java.math.BigDecimal;

public class Vaga {

    private Long id;
    private String titulo;
    private String descricao;
    private ModalidadeEstagio modalidade;
    private Integer cargaHorariaDiaria;
    private Integer cargaHorariaSemanal;
    private BigDecimal valorBolsaOfertado;
    private BigDecimal valorAuxilioTransporteOfertado;
    private String duracaoPrevista;
    private String requisitos;
    private String status;
    private Concedente concedente;

    public Vaga(String titulo, Concedente concedente) {
        this.titulo = titulo;
        this.concedente = concedente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ModalidadeEstagio getModalidade() {
        return modalidade;
    }

    public void setModalidade(ModalidadeEstagio modalidade) {
        this.modalidade = modalidade;
    }

    public Integer getCargaHorariaDiaria() {
        return cargaHorariaDiaria;
    }

    public void setCargaHorariaDiaria(Integer cargaHorariaDiaria) {
        this.cargaHorariaDiaria = cargaHorariaDiaria;
    }

    public Integer getCargaHorariaSemanal() {
        return cargaHorariaSemanal;
    }

    public void setCargaHorariaSemanal(Integer cargaHorariaSemanal) {
        this.cargaHorariaSemanal = cargaHorariaSemanal;
    }

    public BigDecimal getValorBolsaOfertado() {
        return valorBolsaOfertado;
    }

    public void setValorBolsaOfertado(BigDecimal valorBolsaOfertado) {
        this.valorBolsaOfertado = valorBolsaOfertado;
    }

    public BigDecimal getValorAuxilioTransporteOfertado() {
        return valorAuxilioTransporteOfertado;
    }

    public void setValorAuxilioTransporteOfertado(BigDecimal valorAuxilioTransporteOfertado) {
        this.valorAuxilioTransporteOfertado = valorAuxilioTransporteOfertado;
    }

    public String getDuracaoPrevista() {
        return duracaoPrevista;
    }

    public void setDuracaoPrevista(String duracaoPrevista) {
        this.duracaoPrevista = duracaoPrevista;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
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
