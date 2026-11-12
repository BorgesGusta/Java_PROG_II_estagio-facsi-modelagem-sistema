package br.edu.unifesspa.facsi.estagio;

import br.edu.unifesspa.facsi.estagio.enums.StatusEstagio;
import br.edu.unifesspa.facsi.estagio.model.Estagio;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
