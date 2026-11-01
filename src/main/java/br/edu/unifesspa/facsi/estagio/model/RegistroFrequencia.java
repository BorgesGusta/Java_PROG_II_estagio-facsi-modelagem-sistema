package br.edu.unifesspa.facsi.estagio.model;

import java.time.LocalDate;

public class RegistroFrequencia {

    private Long id;
    private LocalDate data;
    private Integer quantidadeHoras;
    private String descricaoAtividade;
    private Estagio estagio;

    public RegistroFrequencia(LocalDate data, Integer quantidadeHoras, String descricaoAtividade, Estagio estagio) {
        this.data = data;
        this.quantidadeHoras = quantidadeHoras;
        this.descricaoAtividade = descricaoAtividade;
        this.estagio = estagio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Integer getQuantidadeHoras() {
        return quantidadeHoras;
    }

    public void setQuantidadeHoras(Integer quantidadeHoras) {
        this.quantidadeHoras = quantidadeHoras;
    }

    public String getDescricaoAtividade() {
        return descricaoAtividade;
    }

    public void setDescricaoAtividade(String descricaoAtividade) {
        this.descricaoAtividade = descricaoAtividade;
    }

    public Estagio getEstagio() {
        return estagio;
    }

    public void setEstagio(Estagio estagio) {
        this.estagio = estagio;
    }
}
