package br.edu.unifesspa.facsi.estagio;

import br.edu.unifesspa.facsi.estagio.enums.StatusEstagio;
import br.edu.unifesspa.facsi.estagio.model.Estagio;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MenuEstagioTest {

    @Test
    void naoDeveRegistrarAvaliacaoAntesDoRelatorioFinal() {
        Estagio estagio = DadosExemplo.criarEstagioFormalizado();
        Scanner entrada = new Scanner("2\n\n7\n\n0\n");
        PrintStream saidaOriginal = System.out;
        ByteArrayOutputStream saida = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(saida, true, StandardCharsets.UTF_8));
            MenuEstagio.executar(entrada, estagio);
        } finally {
            System.setOut(saidaOriginal);
        }

        assertEquals(StatusEstagio.ATIVO, estagio.getStatus());
        assertNull(estagio.getAvaliacaoEstagio());
        assertTrue(saida.toString(StandardCharsets.UTF_8)
                .contains("É necessário registrar o relatório final antes da avaliação."));
    }

    @Test
    void deveBloquearEncerramentoPeloMenuQuandoLimiteDiarioFoiExcedido() {
        Estagio estagio = DadosExemplo.criarEstagioFormalizado();

        String entradaTexto = String.join("\n", List.of(
                "2", "",
                "3", "02/03/2026", "15", "Atividade irregular de teste", "",
                "10", "",
                "6", "31/08/2026", "Relatório final do estágio",
                "", "", "", "", "", "", "",
                "7", "31/08/2026", "bom desempenho", "", "APROVADO", "", "",
                "8", "",
                "9", "31/08/2026", "",
                "0"
        )) + "\n";

        Scanner entrada = new Scanner(entradaTexto);
        PrintStream saidaOriginal = System.out;
        ByteArrayOutputStream saida = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(saida, true, StandardCharsets.UTF_8));
            MenuEstagio.executar(entrada, estagio);
        } finally {
            System.setOut(saidaOriginal);
        }

        String texto = saida.toString(StandardCharsets.UTF_8);

        assertEquals(170, estagio.calcularHorasRealizadas());
        assertFalse(estagio.respeitaLimiteDiario());
        assertEquals(StatusEstagio.ATIVO, estagio.getStatus());
        assertTrue(texto.contains("Carga horária diária: limite de 6h excedido"));
        assertTrue(texto.contains("O estágio ainda não atende aos requisitos de encerramento:"));
        assertFalse(texto.contains("Estágio encerrado com sucesso."));
    }
}
