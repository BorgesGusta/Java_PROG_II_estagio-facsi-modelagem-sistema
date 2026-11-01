package br.edu.unifesspa.facsi.estagio.model;

import java.time.LocalDate;

public class Candidatura {

    private Long id;
    private LocalDate dataCandidatura;
    private LocalDate dataDecisao;
    private String status;
    private String observacao;
    private Aluno aluno;
    private Vaga vaga;

    public Candidatura(Aluno aluno, Vaga vaga, LocalDate dataCandidatura) {
        this.aluno = aluno;
        this.vaga = vaga;
        this.dataCandidatura = dataCandidatura;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataCandidatura() {
        return dataCandidatura;
    }

    public void setDataCandidatura(LocalDate dataCandidatura) {
        this.dataCandidatura = dataCandidatura;
    }

    public LocalDate getDataDecisao() {
        return dataDecisao;
    }

    public void setDataDecisao(LocalDate dataDecisao) {
        this.dataDecisao = dataDecisao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
    }
}
