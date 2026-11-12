package br.edu.unifesspa.facsi.estagio;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MenuTest {

    @Test
    void deveEncerrarDeFormaControladaQuandoEntradaTerminaNoMeio() {
        InputStream entradaOriginal = System.in;
        PrintStream saidaOriginal = System.out;
        ByteArrayOutputStream saida = new ByteArrayOutputStream();

        try {
            System.setIn(new ByteArrayInputStream("1\n1\n".getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(saida, true, StandardCharsets.UTF_8));

            assertDoesNotThrow(() -> Menu.main(new String[]{}));
        } finally {
            System.setIn(entradaOriginal);
            System.setOut(saidaOriginal);
        }

        String texto = saida.toString(StandardCharsets.UTF_8);
        assertTrue(texto.contains("Entrada encerrada."));
        assertTrue(texto.contains("Finalizando o sistema..."));
    }
}
