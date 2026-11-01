package br.edu.unifesspa.facsi.estagio.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApoliceSeguro {

    private Long id;
    private String numero;
    private String seguradora;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String documentoReferencia;
    private List<Estagio> estagios = new ArrayList<>();

    public ApoliceSeguro(String numero, String seguradora) {
        this.numero = numero;
        this.seguradora = seguradora;
    }

    public void adicionarEstagio(Estagio estagio) {
        this.estagios.add(estagio);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getSeguradora() {
        return seguradora;
    }

    public void setSeguradora(String seguradora) {
        this.seguradora = seguradora;
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

    public String getDocumentoReferencia() {
        return documentoReferencia;
    }

    public void setDocumentoReferencia(String documentoReferencia) {
        this.documentoReferencia = documentoReferencia;
    }

    public List<Estagio> getEstagios() {
        return estagios;
    }

    public void setEstagios(List<Estagio> estagios) {
        this.estagios = estagios;
    }
}
