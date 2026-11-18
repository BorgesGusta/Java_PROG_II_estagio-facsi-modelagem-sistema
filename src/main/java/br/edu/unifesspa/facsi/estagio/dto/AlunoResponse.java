package br.edu.unifesspa.facsi.estagio.dto;

public record AlunoResponse(
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        String matricula,
        String curso,
        String situacaoAcademica
) {
}
