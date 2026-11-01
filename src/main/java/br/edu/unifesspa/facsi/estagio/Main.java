package br.edu.unifesspa.facsi.estagio;

import br.edu.unifesspa.facsi.estagio.enums.ModalidadeEstagio;
import br.edu.unifesspa.facsi.estagio.enums.OrigemEstagio;
import br.edu.unifesspa.facsi.estagio.enums.StatusEstagio;
import br.edu.unifesspa.facsi.estagio.model.Aluno;
import br.edu.unifesspa.facsi.estagio.model.Professor;

public class Main {

    public static void main(String[] args) {
        Aluno aluno = new Aluno(
                1L,
                "Aluno Exemplo",
                "000.000.000-00",
                "aluno@unifesspa.edu.br",
                "(94) 99999-0000",
                "2026000000",
                "Sistemas de Informação",
                "ATIVO"
        );

        Professor professor = new Professor(
                2L,
                "Professor Exemplo",
                "111.111.111-11",
                "professor@unifesspa.edu.br",
                "(94) 99999-1111",
                "1234567",
                "FACSI"
        );

        ModalidadeEstagio modalidade = ModalidadeEstagio.OBRIGATORIO;
        OrigemEstagio origem = OrigemEstagio.CONTATO_DIRETO;
        StatusEstagio status = StatusEstagio.EM_FORMALIZACAO;

        System.out.println("Aluno: " + aluno.getNome());
        System.out.println("Matrícula: " + aluno.getMatricula());
        System.out.println("Professor: " + professor.getNome());
        System.out.println("Modalidade: " + modalidade);
        System.out.println("Origem: " + origem);
        System.out.println("Status: " + status);
    }
}
