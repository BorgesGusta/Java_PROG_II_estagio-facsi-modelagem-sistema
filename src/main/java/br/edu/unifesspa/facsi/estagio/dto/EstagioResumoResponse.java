package br.edu.unifesspa.facsi.estagio.dto;

public record EstagioResumoResponse(
        Long id,
        String aluno,
        String concedente,
        String modalidade,
        String origem,
        String ambito,
        String status
) {
}
