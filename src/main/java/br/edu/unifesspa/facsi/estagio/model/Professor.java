package br.edu.unifesspa.facsi.estagio.model;

public class Professor extends Pessoa {

    private String siape;
    private String unidadeAcademica;

    public Professor(
            Long id,
            String nome,
            String cpf,
            String email,
            String telefone,
            String siape,
            String unidadeAcademica
    ) {
        super(id, nome, cpf, email, telefone);
        this.siape = siape;
        this.unidadeAcademica = unidadeAcademica;
    }

    public String getSiape() {
        return siape;
    }

    public void setSiape(String siape) {
        this.siape = siape;
    }

    public String getUnidadeAcademica() {
        return unidadeAcademica;
    }

    public void setUnidadeAcademica(String unidadeAcademica) {
        this.unidadeAcademica = unidadeAcademica;
    }
}
