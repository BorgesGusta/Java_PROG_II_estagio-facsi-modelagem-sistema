package br.edu.unifesspa.facsi.estagio.model;

public class Profissional extends Pessoa {

    private String profissao;
    private String cargo;
    private String registroProfissional;

    public Profissional(
            Long id,
            String nome,
            String cpf,
            String email,
            String telefone,
            String profissao,
            String cargo,
            String registroProfissional
    ) {
        super(id, nome, cpf, email, telefone);
        this.profissao = profissao;
        this.cargo = cargo;
        this.registroProfissional = registroProfissional;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getRegistroProfissional() {
        return registroProfissional;
    }

    public void setRegistroProfissional(String registroProfissional) {
        this.registroProfissional = registroProfissional;
    }
}
