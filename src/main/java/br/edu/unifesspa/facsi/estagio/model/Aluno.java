package br.edu.unifesspa.facsi.estagio.model;

public class Aluno extends Pessoa {

    private String matricula;
    private String curso;
    private String situacaoAcademica;

    public Aluno(
            Long id,
            String nome,
            String cpf,
            String email,
            String telefone,
            String matricula,
            String curso,
            String situacaoAcademica
    ) {
        super(id, nome, cpf, email, telefone);
        this.matricula = matricula;
        this.curso = curso;
        this.situacaoAcademica = situacaoAcademica;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getSituacaoAcademica() {
        return situacaoAcademica;
    }

    public void setSituacaoAcademica(String situacaoAcademica) {
        this.situacaoAcademica = situacaoAcademica;
    }
}
