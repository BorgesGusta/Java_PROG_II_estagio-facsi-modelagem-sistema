package br.edu.unifesspa.facsi.estagio.regras;

import br.edu.unifesspa.facsi.estagio.enums.AmbitoEstagio;
import br.edu.unifesspa.facsi.estagio.enums.ModalidadeEstagio;
import br.edu.unifesspa.facsi.estagio.enums.OrigemEstagio;
import br.edu.unifesspa.facsi.estagio.model.Aluno;
import br.edu.unifesspa.facsi.estagio.model.ApoliceSeguro;
import br.edu.unifesspa.facsi.estagio.model.Concedente;
import br.edu.unifesspa.facsi.estagio.model.Convenio;
import br.edu.unifesspa.facsi.estagio.model.Estagio;
import br.edu.unifesspa.facsi.estagio.model.PlanoAtividades;
import br.edu.unifesspa.facsi.estagio.model.RegistroFrequencia;
import br.edu.unifesspa.facsi.estagio.model.TermoCompromisso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidadorCargaHorariaTest {

    private static final LocalDate SEGUNDA_FEIRA = LocalDate.of(2026, 3, 9);
    private static final LocalDate INICIO_SEMESTRE = LocalDate.of(2026, 3, 1);
    private static final LocalDate FIM_SEMESTRE = LocalDate.of(2026, 6, 30);

    private Aluno aluno;
    private Concedente concedente;

    @BeforeEach
    void prepararCenario() {
        aluno = new Aluno(1L, "Aluno Teste", "000.000.000-00", "aluno@teste.com", "(94) 99999-0000",
                "2026000000", "Sistemas de Informação", "ATIVO");
        concedente = new Concedente("Empresa Teste", "00.000.000/0001-00");
        concedente.adicionarConvenio(new Convenio("CONV-TESTE", concedente));
    }

    @Test
    void deveRespeitarLimiteDeSeisHorasNoDia() {
        Estagio estagio = criarEstagioAtivo(ModalidadeEstagio.OBRIGATORIO, INICIO_SEMESTRE, FIM_SEMESTRE);
        registrarHoras(estagio, SEGUNDA_FEIRA, 6);

        assertTrue(ValidadorCargaHoraria.respeitaLimiteDiario(aluno, List.of(estagio)));
    }

    @Test
    void deveRejeitarSeteHorasNoMesmoDia() {
        Estagio estagio = criarEstagioAtivo(ModalidadeEstagio.OBRIGATORIO, INICIO_SEMESTRE, FIM_SEMESTRE);
        registrarHoras(estagio, SEGUNDA_FEIRA, 4);
        registrarHoras(estagio, SEGUNDA_FEIRA, 3);

        assertFalse(ValidadorCargaHoraria.respeitaLimiteDiario(aluno, List.of(estagio)));
    }

    @Test
    void deveRespeitarLimiteDeTrintaHorasNaSemana() {
        Estagio estagio = criarEstagioAtivo(ModalidadeEstagio.OBRIGATORIO, INICIO_SEMESTRE, FIM_SEMESTRE);
        registrarSeisHorasPorDia(estagio, SEGUNDA_FEIRA, 5);

        assertTrue(ValidadorCargaHoraria.respeitaLimiteSemanal(aluno, List.of(estagio)));
    }

    @Test
    void deveRejeitarTrintaESeisHorasNaMesmaSemana() {
        Estagio estagio = criarEstagioAtivo(ModalidadeEstagio.OBRIGATORIO, INICIO_SEMESTRE, FIM_SEMESTRE);
        registrarSeisHorasPorDia(estagio, SEGUNDA_FEIRA, 6);

        assertFalse(ValidadorCargaHoraria.respeitaLimiteSemanal(aluno, List.of(estagio)));
    }

    @Test
    void deveSomarHorasDeEstagiosComPeriodosSobrepostos() {
        Estagio obrigatorio = criarEstagioAtivo(ModalidadeEstagio.OBRIGATORIO, INICIO_SEMESTRE, FIM_SEMESTRE);
        Estagio naoObrigatorio = criarEstagioAtivo(ModalidadeEstagio.NAO_OBRIGATORIO, INICIO_SEMESTRE, FIM_SEMESTRE);
        registrarHoras(obrigatorio, SEGUNDA_FEIRA, 4);
        registrarHoras(naoObrigatorio, SEGUNDA_FEIRA, 3);

        assertTrue(obrigatorio.respeitaLimiteDiario());
        assertTrue(naoObrigatorio.respeitaLimiteDiario());
        assertFalse(ValidadorCargaHoraria.respeitaLimiteDiario(aluno, List.of(obrigatorio, naoObrigatorio)));
    }

    @Test
    void naoDeveSomarEstagiosSemSobreposicaoDePeriodo() {
        LocalDate segundaFeiraDaVirada = LocalDate.of(2026, 6, 29);
        Estagio primeiroSemestre = criarEstagioAtivo(ModalidadeEstagio.NAO_OBRIGATORIO,
                INICIO_SEMESTRE, LocalDate.of(2026, 6, 30));
        Estagio segundoSemestre = criarEstagioAtivo(ModalidadeEstagio.NAO_OBRIGATORIO,
                LocalDate.of(2026, 7, 1), LocalDate.of(2026, 8, 31));
        registrarSeisHorasPorDia(primeiroSemestre, segundaFeiraDaVirada, 2);
        registrarSeisHorasPorDia(segundoSemestre, segundaFeiraDaVirada.plusDays(2), 4);
        List<Estagio> estagios = List.of(primeiroSemestre, segundoSemestre);

        assertTrue(ValidadorCargaHoraria.respeitaLimiteDiario(aluno, estagios));
        assertTrue(ValidadorCargaHoraria.respeitaLimiteSemanal(aluno, estagios));
    }

    @Test
    void devePermitirDoisEstagiosNaoObrigatoriosSimultaneosDentroDosLimites() {
        Estagio primeiro = criarEstagioAtivo(ModalidadeEstagio.NAO_OBRIGATORIO, INICIO_SEMESTRE, FIM_SEMESTRE);
        Estagio segundo = criarEstagioAtivo(ModalidadeEstagio.NAO_OBRIGATORIO, INICIO_SEMESTRE, FIM_SEMESTRE);
        registrarHoras(primeiro, SEGUNDA_FEIRA, 3);
        registrarHoras(segundo, SEGUNDA_FEIRA, 3);

        assertTrue(ValidadorCargaHoraria.respeitaRegrasDeSimultaneidade(aluno, List.of(primeiro, segundo)));
    }

    @Test
    void deveRejeitarTresEstagiosNaoObrigatoriosSimultaneos() {
        Estagio primeiro = criarEstagioAtivo(ModalidadeEstagio.NAO_OBRIGATORIO, INICIO_SEMESTRE, FIM_SEMESTRE);
        Estagio segundo = criarEstagioAtivo(ModalidadeEstagio.NAO_OBRIGATORIO, INICIO_SEMESTRE, FIM_SEMESTRE);
        Estagio terceiro = criarEstagioAtivo(ModalidadeEstagio.NAO_OBRIGATORIO,
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 5, 31));

        assertFalse(ValidadorCargaHoraria.respeitaLimiteEstagiosNaoObrigatorios(
                aluno, List.of(primeiro, segundo, terceiro)));
    }

    @Test
    void devePermitirTresEstagiosNaoObrigatoriosQueNuncaEstaoAtivosAoMesmoTempo() {
        Estagio estagioA = criarEstagioAtivo(ModalidadeEstagio.NAO_OBRIGATORIO,
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 6, 30));
        Estagio estagioB = criarEstagioAtivo(ModalidadeEstagio.NAO_OBRIGATORIO,
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 8, 31));
        Estagio estagioC = criarEstagioAtivo(ModalidadeEstagio.NAO_OBRIGATORIO,
                LocalDate.of(2026, 7, 1), LocalDate.of(2026, 10, 31));

        assertTrue(ValidadorCargaHoraria.respeitaLimiteEstagiosNaoObrigatorios(
                aluno, List.of(estagioA, estagioB, estagioC)));
    }

    private Estagio criarEstagioAtivo(ModalidadeEstagio modalidade, LocalDate dataInicio, LocalDate dataFimPrevista) {
        Estagio estagio = new Estagio(aluno, concedente, modalidade, OrigemEstagio.CONTATO_DIRETO, AmbitoEstagio.EXTERNO);
        estagio.setDataInicio(dataInicio);
        estagio.setDataFimPrevista(dataFimPrevista);
        estagio.setConvenio(concedente.getConvenios().get(0));

        TermoCompromisso termo = new TermoCompromisso(estagio, dataInicio);
        termo.setRemunerado(true);
        termo.setValorRemuneracao(new BigDecimal("1000.00"));
        termo.setValorAuxilioTransporte(new BigDecimal("200.00"));
        estagio.setTermoCompromisso(termo);
        estagio.adicionarPlanoAtividades(new PlanoAtividades(estagio, "Desenvolvimento de Software"));
        estagio.adicionarApolice(new ApoliceSeguro("AP-TESTE", "Seguradora Teste"));

        assertTrue(estagio.iniciar(), "O estágio de teste deveria iniciar com a documentação completa");
        return estagio;
    }

    private void registrarHoras(Estagio estagio, LocalDate data, int horas) {
        estagio.adicionarRegistroFrequencia(new RegistroFrequencia(data, horas, "Atividade de teste", estagio));
    }

    private void registrarSeisHorasPorDia(Estagio estagio, LocalDate primeiroDia, int quantidadeDeDias) {
        for (int dia = 0; dia < quantidadeDeDias; dia++) {
            registrarHoras(estagio, primeiroDia.plusDays(dia), 6);
        }
    }
}
