package br.edu.unifesspa.facsi.estagio.regras;

import br.edu.unifesspa.facsi.estagio.enums.AmbitoEstagio;
import br.edu.unifesspa.facsi.estagio.enums.ModalidadeEstagio;
import br.edu.unifesspa.facsi.estagio.enums.OrigemEstagio;
import br.edu.unifesspa.facsi.estagio.enums.StatusEstagio;
import br.edu.unifesspa.facsi.estagio.model.Aluno;
import br.edu.unifesspa.facsi.estagio.model.ApoliceSeguro;
import br.edu.unifesspa.facsi.estagio.model.AvaliacaoEstagio;
import br.edu.unifesspa.facsi.estagio.model.Concedente;
import br.edu.unifesspa.facsi.estagio.model.Convenio;
import br.edu.unifesspa.facsi.estagio.model.Estagio;
import br.edu.unifesspa.facsi.estagio.model.PlanoAtividades;
import br.edu.unifesspa.facsi.estagio.model.RegistroFrequencia;
import br.edu.unifesspa.facsi.estagio.model.RelatorioFinal;
import br.edu.unifesspa.facsi.estagio.model.TermoCompromisso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidadorEstagioTest {

    private static final LocalDate DATA_INICIO = LocalDate.of(2026, 3, 2);
    private static final LocalDate DATA_FIM_PREVISTA = LocalDate.of(2026, 6, 30);

    private Aluno aluno;
    private Concedente concedente;
    private Convenio convenio;

    @BeforeEach
    void prepararCenario() {
        aluno = new Aluno(1L, "Aluno Teste", "000.000.000-00", "aluno@teste.com", "(94) 99999-0000",
                "2026000000", "Sistemas de Informação", "ATIVO");
        concedente = new Concedente("Empresa Teste", "00.000.000/0001-00");
        convenio = new Convenio("CONV-TESTE", concedente);
        convenio.setDataInicio(LocalDate.of(2026, 1, 1));
        convenio.setDataFim(LocalDate.of(2030, 12, 31));
        concedente.adicionarConvenio(convenio);
    }

    @Test
    void naoDevePermitirIniciarEstagioSemDocumentos() {
        Estagio estagio = criarEstagio(ModalidadeEstagio.OBRIGATORIO, AmbitoEstagio.EXTERNO);
        estagio.setConvenio(convenio);

        assertFalse(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void naoDevePermitirIniciarEstagioExternoSemConvenio() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.OBRIGATORIO, AmbitoEstagio.EXTERNO);
        estagio.setConvenio(null);

        assertFalse(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void devePermitirIniciarEstagioInternoSemConvenio() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.OBRIGATORIO, AmbitoEstagio.INTERNO);
        estagio.setConvenio(null);

        assertTrue(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void naoDevePermitirIniciarEstagioSemAmbitoDefinido() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.OBRIGATORIO, null);

        assertFalse(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void naoDevePermitirIniciarNaoObrigatorioSemBolsa() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.NAO_OBRIGATORIO, AmbitoEstagio.EXTERNO);
        estagio.getTermoCompromisso().setRemunerado(false);
        estagio.getTermoCompromisso().setValorRemuneracao(null);

        assertFalse(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void naoDevePermitirIniciarNaoObrigatorioSemAuxilioTransporte() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.NAO_OBRIGATORIO, AmbitoEstagio.EXTERNO);
        estagio.getTermoCompromisso().setValorAuxilioTransporte(BigDecimal.ZERO);

        assertFalse(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void devePermitirIniciarNaoObrigatorioComBolsaEAuxilioTransporte() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.NAO_OBRIGATORIO, AmbitoEstagio.EXTERNO);

        assertTrue(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void iniciarDeveMudarStatusParaAtivoQuandoRequisitosForemCumpridos() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.OBRIGATORIO, AmbitoEstagio.EXTERNO);
        assertEquals(StatusEstagio.EM_FORMALIZACAO, estagio.getStatus());

        assertTrue(estagio.iniciar());
        assertEquals(StatusEstagio.ATIVO, estagio.getStatus());
    }

    @Test
    void iniciarDeveManterEmFormalizacaoQuandoRequisitosNaoForemCumpridos() {
        Estagio estagio = criarEstagio(ModalidadeEstagio.OBRIGATORIO, AmbitoEstagio.EXTERNO);

        assertFalse(estagio.iniciar());
        assertEquals(StatusEstagio.EM_FORMALIZACAO, estagio.getStatus());
    }

    @Test
    void naoDevePermitirEncerrarObrigatorioComMenosDeCentoESetentaHoras() {
        Estagio estagio = criarEstagioAtivoObrigatorio();
        registrarHoras(estagio, DATA_INICIO, 6);
        anexarRelatorioFinal(estagio);
        anexarAvaliacao(estagio, ValidadorEstagio.RESULTADO_APROVADO);

        assertTrue(estagio.calcularHorasRealizadas() < ValidadorEstagio.CARGA_HORARIA_MINIMA_OBRIGATORIO);
        assertFalse(ValidadorEstagio.podeEncerrar(estagio));
    }

    @Test
    void naoDevePermitirEncerrarSemRelatorioFinal() {
        Estagio estagio = criarEstagioAtivoObrigatorio();
        registrarSeisHorasPorDiaUtilAteCargaMinima(estagio);
        anexarAvaliacao(estagio, ValidadorEstagio.RESULTADO_APROVADO);

        assertFalse(ValidadorEstagio.podeEncerrar(estagio));
    }

    @Test
    void naoDevePermitirEncerrarSemAvaliacao() {
        Estagio estagio = criarEstagioAtivoObrigatorio();
        registrarSeisHorasPorDiaUtilAteCargaMinima(estagio);
        anexarRelatorioFinal(estagio);

        assertFalse(ValidadorEstagio.podeEncerrar(estagio));
    }

    @Test
    void naoDevePermitirEncerrarComAvaliacaoNaoAprovada() {
        Estagio estagio = criarEstagioAtivoObrigatorio();
        registrarSeisHorasPorDiaUtilAteCargaMinima(estagio);
        anexarRelatorioFinal(estagio);
        anexarAvaliacao(estagio, "REPROVADO");

        assertFalse(ValidadorEstagio.podeEncerrar(estagio));
    }

    @Test
    void devePermitirEncerrarObrigatorioComRequisitosCumpridos() {
        Estagio estagio = criarEstagioProntoParaEncerrar();

        assertTrue(ValidadorEstagio.podeEncerrar(estagio));
    }

    @Test
    void encerrarDeveMudarStatusERegistrarDataFimReal() {
        Estagio estagio = criarEstagioProntoParaEncerrar();
        LocalDate dataFimReal = ultimoDiaDeAtividade(estagio);

        assertTrue(estagio.encerrar(dataFimReal));
        assertEquals(StatusEstagio.ENCERRADO, estagio.getStatus());
        assertEquals(dataFimReal, estagio.getDataFimReal());
    }

    @Test
    void encerrarComDataNulaNaoDeveAlterarStatusNemDataFimReal() {
        Estagio estagio = criarEstagioProntoParaEncerrar();

        assertFalse(estagio.encerrar(null));
        assertEquals(StatusEstagio.ATIVO, estagio.getStatus());
        assertNull(estagio.getDataFimReal());
    }

    @Test
    void encerrarSemCumprirRegrasNaoDeveAlterarStatusNemDataFimReal() {
        Estagio estagio = criarEstagioAtivoObrigatorio();
        registrarHoras(estagio, DATA_INICIO, 6);

        assertFalse(estagio.encerrar(LocalDate.of(2026, 6, 30)));
        assertEquals(StatusEstagio.ATIVO, estagio.getStatus());
        assertNull(estagio.getDataFimReal());
    }

    @Test
    void naoDevePermitirIniciarSemDataDeInicio() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.OBRIGATORIO, AmbitoEstagio.EXTERNO);
        estagio.setDataInicio(null);

        assertFalse(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void naoDevePermitirIniciarSemDataDeFimPrevista() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.OBRIGATORIO, AmbitoEstagio.EXTERNO);
        estagio.setDataFimPrevista(null);

        assertFalse(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void naoDevePermitirIniciarComFimPrevistoAnteriorAoInicio() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.OBRIGATORIO, AmbitoEstagio.EXTERNO);
        estagio.setDataFimPrevista(DATA_INICIO.minusDays(1));

        assertFalse(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void devePermitirIniciarComPeriodoValido() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.OBRIGATORIO, AmbitoEstagio.EXTERNO);
        estagio.setDataFimPrevista(DATA_INICIO);

        assertTrue(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void devePermitirIniciarEstagioExternoComConvenioVigente() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.OBRIGATORIO, AmbitoEstagio.EXTERNO);

        assertTrue(convenio.estaVigenteEm(DATA_INICIO));
        assertTrue(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void naoDevePermitirIniciarEstagioExternoComConvenioVencido() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.OBRIGATORIO, AmbitoEstagio.EXTERNO);
        convenio.setDataInicio(LocalDate.of(2021, 1, 1));
        convenio.setDataFim(DATA_INICIO.minusDays(1));

        assertFalse(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void naoDevePermitirIniciarEstagioExternoComConvenioQueAindaNaoComecou() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.OBRIGATORIO, AmbitoEstagio.EXTERNO);
        convenio.setDataInicio(DATA_INICIO.plusDays(1));

        assertFalse(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void naoDevePermitirIniciarSemApolice() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.OBRIGATORIO, AmbitoEstagio.EXTERNO);
        estagio.getApolices().clear();

        assertFalse(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void naoDevePermitirIniciarComApoliceVencida() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.OBRIGATORIO, AmbitoEstagio.EXTERNO);
        estagio.getApolices().clear();
        estagio.adicionarApolice(criarApolice(LocalDate.of(2025, 1, 1), DATA_INICIO.minusDays(1)));

        assertFalse(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void naoDevePermitirIniciarComApoliceQueComecaDepoisDoEstagio() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.OBRIGATORIO, AmbitoEstagio.EXTERNO);
        estagio.getApolices().clear();
        estagio.adicionarApolice(criarApolice(DATA_INICIO.plusDays(1), LocalDate.of(2026, 12, 31)));

        assertFalse(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void naoDevePermitirIniciarComApoliceSemDatas() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.OBRIGATORIO, AmbitoEstagio.EXTERNO);
        estagio.getApolices().clear();
        estagio.adicionarApolice(new ApoliceSeguro("AP-SEM-DATAS", "Seguradora Teste"));

        assertFalse(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void devePermitirIniciarQuandoAoMenosUmaApoliceEstiverVigente() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.OBRIGATORIO, AmbitoEstagio.EXTERNO);
        estagio.getApolices().clear();
        estagio.adicionarApolice(criarApolice(LocalDate.of(2025, 1, 1), DATA_INICIO.minusDays(1)));
        estagio.adicionarApolice(criarApolice(DATA_INICIO, LocalDate.of(2026, 12, 31)));

        assertTrue(ValidadorEstagio.podeIniciar(estagio));
    }

    @Test
    void encerrarComDataAnteriorAoInicioNaoDeveAlterarStatusNemDataFimReal() {
        Estagio estagio = criarEstagioProntoParaEncerrar();

        assertFalse(estagio.encerrar(DATA_INICIO.minusDays(1)));
        assertEquals(StatusEstagio.ATIVO, estagio.getStatus());
        assertNull(estagio.getDataFimReal());
    }

    @Test
    void encerrarComDataAnteriorAoUltimoRegistroNaoDeveAlterarStatusNemDataFimReal() {
        Estagio estagio = criarEstagioProntoParaEncerrar();

        assertFalse(estagio.encerrar(ultimoDiaDeAtividade(estagio).minusDays(1)));
        assertEquals(StatusEstagio.ATIVO, estagio.getStatus());
        assertNull(estagio.getDataFimReal());
    }

    @Test
    void devePermitirEncerrarNaDataDoUltimoRegistro() {
        Estagio estagio = criarEstagioProntoParaEncerrar();
        LocalDate dataDoUltimoRegistro = ultimoDiaDeAtividade(estagio);

        assertTrue(estagio.encerrar(dataDoUltimoRegistro));
        assertEquals(dataDoUltimoRegistro, estagio.getDataFimReal());
    }

    @Test
    void devePermitirEncerrarDepoisDoUltimoRegistro() {
        Estagio estagio = criarEstagioProntoParaEncerrar();
        LocalDate dataPosterior = ultimoDiaDeAtividade(estagio).plusDays(3);

        assertTrue(estagio.encerrar(dataPosterior));
        assertEquals(StatusEstagio.ENCERRADO, estagio.getStatus());
        assertEquals(dataPosterior, estagio.getDataFimReal());
    }

    private Estagio criarEstagio(ModalidadeEstagio modalidade, AmbitoEstagio ambito) {
        Estagio estagio = new Estagio(aluno, concedente, modalidade, OrigemEstagio.CONTATO_DIRETO, ambito);
        estagio.setDataInicio(DATA_INICIO);
        estagio.setDataFimPrevista(DATA_FIM_PREVISTA);
        return estagio;
    }

    private Estagio criarEstagioComDocumentos(ModalidadeEstagio modalidade, AmbitoEstagio ambito) {
        Estagio estagio = criarEstagio(modalidade, ambito);
        estagio.setConvenio(convenio);

        TermoCompromisso termo = new TermoCompromisso(estagio, DATA_INICIO);
        termo.setRemunerado(true);
        termo.setValorRemuneracao(new BigDecimal("1000.00"));
        termo.setValorAuxilioTransporte(new BigDecimal("200.00"));
        estagio.setTermoCompromisso(termo);
        estagio.adicionarPlanoAtividades(new PlanoAtividades(estagio, "Desenvolvimento de Software"));
        estagio.adicionarApolice(criarApolice(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31)));
        return estagio;
    }

    private ApoliceSeguro criarApolice(LocalDate dataInicio, LocalDate dataFim) {
        ApoliceSeguro apolice = new ApoliceSeguro("AP-TESTE", "Seguradora Teste");
        apolice.setDataInicio(dataInicio);
        apolice.setDataFim(dataFim);
        return apolice;
    }

    private Estagio criarEstagioAtivoObrigatorio() {
        Estagio estagio = criarEstagioComDocumentos(ModalidadeEstagio.OBRIGATORIO, AmbitoEstagio.EXTERNO);
        assertTrue(estagio.iniciar(), "O estágio de teste deveria iniciar com a documentação completa");
        return estagio;
    }

    private Estagio criarEstagioProntoParaEncerrar() {
        Estagio estagio = criarEstagioAtivoObrigatorio();
        registrarSeisHorasPorDiaUtilAteCargaMinima(estagio);
        anexarRelatorioFinal(estagio);
        anexarAvaliacao(estagio, ValidadorEstagio.RESULTADO_APROVADO);
        return estagio;
    }

    private void registrarHoras(Estagio estagio, LocalDate data, int horas) {
        estagio.adicionarRegistroFrequencia(new RegistroFrequencia(data, horas, "Atividade de teste", estagio));
    }

    private void registrarSeisHorasPorDiaUtilAteCargaMinima(Estagio estagio) {
        LocalDate dia = DATA_INICIO;
        while (estagio.calcularHorasRealizadas() < ValidadorEstagio.CARGA_HORARIA_MINIMA_OBRIGATORIO) {
            if (dia.getDayOfWeek() != DayOfWeek.SATURDAY && dia.getDayOfWeek() != DayOfWeek.SUNDAY) {
                registrarHoras(estagio, dia, 6);
            }
            dia = dia.plusDays(1);
        }
    }

    private LocalDate ultimoDiaDeAtividade(Estagio estagio) {
        List<RegistroFrequencia> registros = estagio.getRegistrosFrequencia();
        return registros.get(registros.size() - 1).getData();
    }

    private void anexarRelatorioFinal(Estagio estagio) {
        estagio.setRelatorioFinal(new RelatorioFinal(estagio, "Manutenção de sistemas web", DATA_FIM_PREVISTA));
    }

    private void anexarAvaliacao(Estagio estagio, String resultado) {
        AvaliacaoEstagio avaliacao = new AvaliacaoEstagio(estagio, DATA_FIM_PREVISTA);
        avaliacao.setResultado(resultado);
        estagio.setAvaliacaoEstagio(avaliacao);
    }
}
