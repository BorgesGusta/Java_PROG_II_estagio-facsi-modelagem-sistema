package br.edu.unifesspa.facsi.estagio.model;

import java.util.ArrayList;
import java.util.List;

public class Concedente {

    private Long id;
    private String nome;
    private String tipo;
    private String documento;
    private String email;
    private String telefone;
    private List<Convenio> convenios = new ArrayList<>();

    public Concedente(String nome, String documento) {
        this.nome = nome;
        this.documento = documento;
    }

    public void adicionarConvenio(Convenio convenio) {
        this.convenios.add(convenio);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<Convenio> getConvenios() {
        return convenios;
    }

    public void setConvenios(List<Convenio> convenios) {
        this.convenios = convenios;
    }
}
