package br.edu.unifesspa.facsi.estagio.dto;

public record AlunoRequest(
        String nome,
        String cpf,
        String email,
        String telefone,
        String matricula,
        String curso,
        String situacaoAcademica
) {
}
