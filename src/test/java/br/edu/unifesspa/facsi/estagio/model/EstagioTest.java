package br.edu.unifesspa.facsi.estagio.model;

import br.edu.unifesspa.facsi.estagio.enums.AmbitoEstagio;
import br.edu.unifesspa.facsi.estagio.enums.ModalidadeEstagio;
import br.edu.unifesspa.facsi.estagio.enums.OrigemEstagio;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EstagioTest {

    private static final LocalDate SEGUNDA_FEIRA = LocalDate.of(2026, 3, 9);

    private final Aluno aluno = new Aluno(1L, "Aluno Teste", "000.000.000-00", "aluno@teste.com",
            "(94) 99999-0000", "2026000000", "Sistemas de Informação", "ATIVO");
    private final Concedente concedente = new Concedente("Empresa Teste", "00.000.000/0001-00");

    @Test
    void deveSomarRegistrosDaMesmaData() {
        Estagio estagio = criarEstagio(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 6, 30));
        registrarHoras(estagio, SEGUNDA_FEIRA, 2);
        registrarHoras(estagio, SEGUNDA_FEIRA, 4);
        registrarHoras(estagio, SEGUNDA_FEIRA.plusDays(1), 5);

        assertEquals(6, estagio.calcularHorasNaData(SEGUNDA_FEIRA));
    }

    @Test
    void deveSomarRegistrosDeSegundaASextaDaMesmaSemana() {
        Estagio estagio = criarEstagio(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 6, 30));
        for (int dia = 0; dia < 5; dia++) {
            registrarHoras(estagio, SEGUNDA_FEIRA.plusDays(dia), 6);
        }
        registrarHoras(estagio, SEGUNDA_FEIRA.plusDays(7), 6);

        assertEquals(30, estagio.calcularHorasNaSemana(SEGUNDA_FEIRA));
    }

    @Test
    void deveIdentificarPeriodosSobrepostos() {
        Estagio primeiro = criarEstagio(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 6, 30));
        Estagio segundo = criarEstagio(LocalDate.of(2026, 5, 1), LocalDate.of(2026, 8, 31));

        assertTrue(primeiro.possuiPeriodoSobreposto(segundo));
        assertTrue(segundo.possuiPeriodoSobreposto(primeiro));
    }

    @Test
    void naoDeveIdentificarSobreposicaoEmPeriodosSeparados() {
        Estagio primeiro = criarEstagio(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 6, 30));
        Estagio segundo = criarEstagio(LocalDate.of(2026, 7, 1), LocalDate.of(2026, 8, 31));

        assertFalse(primeiro.possuiPeriodoSobreposto(segundo));
        assertFalse(segundo.possuiPeriodoSobreposto(primeiro));
    }

    @Test
    void deveConsiderarSobreposicaoQuandoUmTerminaNoDiaEmQueOutroComeca() {
        Estagio primeiro = criarEstagio(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 6, 30));
        Estagio segundo = criarEstagio(LocalDate.of(2026, 6, 30), LocalDate.of(2026, 8, 31));

        assertTrue(primeiro.possuiPeriodoSobreposto(segundo));
    }

    @Test
    void deveUsarDataFimRealNoLugarDaDataFimPrevista() {
        Estagio encerradoAntes = criarEstagio(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 8, 31));
        encerradoAntes.setDataFimReal(LocalDate.of(2026, 4, 30));
        Estagio posterior = criarEstagio(LocalDate.of(2026, 5, 1), LocalDate.of(2026, 8, 31));

        assertFalse(encerradoAntes.possuiPeriodoSobreposto(posterior));
    }

    private Estagio criarEstagio(LocalDate dataInicio, LocalDate dataFimPrevista) {
        Estagio estagio = new Estagio(aluno, concedente, ModalidadeEstagio.OBRIGATORIO,
                OrigemEstagio.CONTATO_DIRETO, AmbitoEstagio.EXTERNO);
        estagio.setDataInicio(dataInicio);
        estagio.setDataFimPrevista(dataFimPrevista);
        return estagio;
    }

    private void registrarHoras(Estagio estagio, LocalDate data, int horas) {
        estagio.adicionarRegistroFrequencia(new RegistroFrequencia(data, horas, "Atividade de teste", estagio));
    }
}
