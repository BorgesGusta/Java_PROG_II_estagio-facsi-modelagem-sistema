package br.edu.unifesspa.facsi.estagio.model;

import br.edu.unifesspa.facsi.estagio.enums.AmbitoEstagio;
import br.edu.unifesspa.facsi.estagio.enums.ModalidadeEstagio;
import br.edu.unifesspa.facsi.estagio.enums.OrigemEstagio;
import br.edu.unifesspa.facsi.estagio.enums.StatusEstagio;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EstagioTest {

    private static final LocalDate SEGUNDA_FEIRA = LocalDate.of(2026, 3, 9);
    private static final LocalDate INICIO_PERIODO = LocalDate.of(2026, 3, 1);
    private static final LocalDate FIM_PERIODO = LocalDate.of(2026, 6, 30);

    private final Aluno aluno = new Aluno(1L, "Aluno Teste", "000.000.000-00", "aluno@teste.com",
            "(94) 99999-0000", "2026000000", "Sistemas de Informação", "ATIVO");
    private final Concedente concedente = new Concedente("Empresa Teste", "00.000.000/0001-00");

    @Test
    void deveSomarRegistrosDaMesmaData() {
        Estagio estagio = criarEstagioAtivo();
        registrarHoras(estagio, SEGUNDA_FEIRA, 2);
        registrarHoras(estagio, SEGUNDA_FEIRA, 4);
        registrarHoras(estagio, SEGUNDA_FEIRA.plusDays(1), 5);

        assertEquals(6, estagio.calcularHorasNaData(SEGUNDA_FEIRA));
    }

    @Test
    void deveSomarRegistrosDeSegundaASextaDaMesmaSemana() {
        Estagio estagio = criarEstagioAtivo();
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

    @Test
    void deveRegistrarFrequenciaDentroDoPeriodo() {
        Estagio estagio = criarEstagioAtivo();

        assertTrue(estagio.adicionarRegistroFrequencia(criarRegistro(estagio, SEGUNDA_FEIRA, 4)));
        assertEquals(1, estagio.getRegistrosFrequencia().size());
        assertEquals(4, estagio.calcularHorasRealizadas());
    }

    @Test
    void deveRegistrarFrequenciaNoPrimeiroENoUltimoDiaDoPeriodo() {
        Estagio estagio = criarEstagioAtivo();

        assertTrue(estagio.adicionarRegistroFrequencia(criarRegistro(estagio, INICIO_PERIODO, 4)));
        assertTrue(estagio.adicionarRegistroFrequencia(criarRegistro(estagio, FIM_PERIODO, 4)));
        assertEquals(2, estagio.getRegistrosFrequencia().size());
    }

    @Test
    void naoDeveRegistrarFrequenciaAntesDoInicio() {
        Estagio estagio = criarEstagioAtivo();

        assertFalse(estagio.adicionarRegistroFrequencia(criarRegistro(estagio, INICIO_PERIODO.minusDays(1), 4)));
        assertTrue(estagio.getRegistrosFrequencia().isEmpty());
    }

    @Test
    void naoDeveRegistrarFrequenciaDepoisDoFimPrevisto() {
        Estagio estagio = criarEstagioAtivo();

        assertFalse(estagio.adicionarRegistroFrequencia(criarRegistro(estagio, FIM_PERIODO.plusDays(1), 4)));
        assertTrue(estagio.getRegistrosFrequencia().isEmpty());
    }

    @Test
    void naoDeveRegistrarFrequenciaSemData() {
        Estagio estagio = criarEstagioAtivo();

        assertFalse(estagio.adicionarRegistroFrequencia(criarRegistro(estagio, null, 4)));
        assertTrue(estagio.getRegistrosFrequencia().isEmpty());
    }

    @Test
    void naoDeveRegistrarFrequenciaComZeroHoras() {
        Estagio estagio = criarEstagioAtivo();

        assertFalse(estagio.adicionarRegistroFrequencia(criarRegistro(estagio, SEGUNDA_FEIRA, 0)));
        assertTrue(estagio.getRegistrosFrequencia().isEmpty());
    }

    @Test
    void naoDeveRegistrarFrequenciaComHorasNegativas() {
        Estagio estagio = criarEstagioAtivo();

        assertFalse(estagio.adicionarRegistroFrequencia(criarRegistro(estagio, SEGUNDA_FEIRA, -2)));
        assertTrue(estagio.getRegistrosFrequencia().isEmpty());
    }

    @Test
    void naoDeveRegistrarFrequenciaQuandoEstagioNaoEstaAtivo() {
        Estagio estagio = criarEstagio(INICIO_PERIODO, FIM_PERIODO);

        assertEquals(StatusEstagio.EM_FORMALIZACAO, estagio.getStatus());
        assertFalse(estagio.adicionarRegistroFrequencia(criarRegistro(estagio, SEGUNDA_FEIRA, 4)));
        assertTrue(estagio.getRegistrosFrequencia().isEmpty());
    }

    @Test
    void deveRegistrarFrequenciaQueExcedeLimiteDiarioESinalizarIrregularidade() {
        Estagio estagio = criarEstagioAtivo();
        registrarHoras(estagio, SEGUNDA_FEIRA, 6);

        assertTrue(estagio.adicionarRegistroFrequencia(criarRegistro(estagio, SEGUNDA_FEIRA, 2)));
        assertEquals(8, estagio.calcularHorasNaData(SEGUNDA_FEIRA));
        assertFalse(estagio.respeitaLimiteDiario());
    }

    private Estagio criarEstagio(LocalDate dataInicio, LocalDate dataFimPrevista) {
        Estagio estagio = new Estagio(aluno, concedente, ModalidadeEstagio.OBRIGATORIO,
                OrigemEstagio.CONTATO_DIRETO, AmbitoEstagio.EXTERNO);
        estagio.setDataInicio(dataInicio);
        estagio.setDataFimPrevista(dataFimPrevista);
        return estagio;
    }

    private Estagio criarEstagioAtivo() {
        Estagio estagio = criarEstagio(INICIO_PERIODO, FIM_PERIODO);

        Convenio convenio = new Convenio("CONV-TESTE", concedente);
        convenio.setDataInicio(LocalDate.of(2026, 1, 1));
        convenio.setDataFim(LocalDate.of(2030, 12, 31));
        estagio.setConvenio(convenio);

        estagio.setTermoCompromisso(new TermoCompromisso(estagio, INICIO_PERIODO));
        estagio.adicionarPlanoAtividades(new PlanoAtividades(estagio, "Desenvolvimento de Software"));

        ApoliceSeguro apolice = new ApoliceSeguro("AP-TESTE", "Seguradora Teste");
        apolice.setDataInicio(LocalDate.of(2026, 1, 1));
        apolice.setDataFim(LocalDate.of(2026, 12, 31));
        estagio.adicionarApolice(apolice);

        assertTrue(estagio.iniciar(), "O estágio de teste deveria iniciar com a documentação completa");
        return estagio;
    }

    private RegistroFrequencia criarRegistro(Estagio estagio, LocalDate data, int horas) {
        return new RegistroFrequencia(data, horas, "Atividade de teste", estagio);
    }

    private void registrarHoras(Estagio estagio, LocalDate data, int horas) {
        estagio.adicionarRegistroFrequencia(criarRegistro(estagio, data, horas));
    }
}
