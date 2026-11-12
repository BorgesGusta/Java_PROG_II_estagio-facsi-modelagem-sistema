package br.edu.unifesspa.facsi.estagio;

import br.edu.unifesspa.facsi.estagio.enums.StatusEstagio;
import br.edu.unifesspa.facsi.estagio.model.AvaliacaoEstagio;
import br.edu.unifesspa.facsi.estagio.model.Estagio;
import br.edu.unifesspa.facsi.estagio.model.RegistroFrequencia;
import br.edu.unifesspa.facsi.estagio.model.RelatorioFinal;
import br.edu.unifesspa.facsi.estagio.regras.ValidadorEstagio;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DadosExemploTest {

    @Test
    void preenchimentoDeDemonstracaoDeveAtingirCargaMinimaRespeitandoLimites() {
        Estagio estagio = criarEstagioAtivo();

        int adicionados = DadosExemplo.preencherCargaHorariaMinima(estagio);

        assertEquals(ValidadorEstagio.CARGA_HORARIA_MINIMA_OBRIGATORIO, estagio.calcularHorasRealizadas());
        assertEquals(adicionados, estagio.getRegistrosFrequencia().size());
        assertTrue(estagio.respeitaLimiteDiario());
        assertTrue(estagio.respeitaLimiteSemanal());
        for (RegistroFrequencia registro : estagio.getRegistrosFrequencia()) {
            DayOfWeek diaDaSemana = registro.getData().getDayOfWeek();
            assertFalse(diaDaSemana == DayOfWeek.SATURDAY || diaDaSemana == DayOfWeek.SUNDAY);
            assertTrue(estagio.estaVigenteEm(registro.getData()));
            assertTrue(registro.getQuantidadeHoras() <= Estagio.LIMITE_HORAS_DIARIAS);
        }
    }

    @Test
    void preenchimentoDeDemonstracaoDeveComplementarRegistrosExistentesSemExcederLimites() {
        Estagio estagio = criarEstagioAtivo();
        LocalDate segundaFeira = LocalDate.of(2026, 3, 2);
        estagio.adicionarRegistroFrequencia(new RegistroFrequencia(segundaFeira, 4, "Registro manual", estagio));

        DadosExemplo.preencherCargaHorariaMinima(estagio);

        assertEquals(Estagio.LIMITE_HORAS_DIARIAS, estagio.calcularHorasNaData(segundaFeira));
        assertEquals(ValidadorEstagio.CARGA_HORARIA_MINIMA_OBRIGATORIO, estagio.calcularHorasRealizadas());
        assertTrue(estagio.respeitaLimiteDiario());
        assertTrue(estagio.respeitaLimiteSemanal());
    }

    @Test
    void preenchimentoDeDemonstracaoNaoDeveRegistrarEmEstagioNaoIniciado() {
        Estagio estagio = DadosExemplo.criarEstagioFormalizado();

        assertEquals(0, DadosExemplo.preencherCargaHorariaMinima(estagio));
        assertTrue(estagio.getRegistrosFrequencia().isEmpty());
    }

    @Test
    void deveCompletarCicloDoFormalizadoAoEncerrado() {
        Estagio estagio = DadosExemplo.criarEstagioFormalizado();
        assertEquals(StatusEstagio.EM_FORMALIZACAO, estagio.getStatus());

        assertTrue(estagio.iniciar());
        estagio.adicionarRegistroFrequencia(new RegistroFrequencia(LocalDate.of(2026, 3, 2), 6, "Atividade", estagio));

        assertFalse(estagio.encerrar(LocalDate.of(2026, 3, 2)));
        assertEquals(StatusEstagio.ATIVO, estagio.getStatus());
        assertFalse(ValidadorEstagio.listarPendenciasEncerramento(estagio).isEmpty());

        DadosExemplo.preencherCargaHorariaMinima(estagio);
        LocalDate ultimoDia = ultimaDataRegistrada(estagio);
        estagio.setRelatorioFinal(new RelatorioFinal(estagio, "Desenvolvimento Java", ultimoDia.plusDays(1)));
        AvaliacaoEstagio avaliacao = new AvaliacaoEstagio(estagio, ultimoDia.plusDays(2));
        avaliacao.setResultado(ValidadorEstagio.RESULTADO_APROVADO);
        estagio.setAvaliacaoEstagio(avaliacao);

        assertTrue(ValidadorEstagio.listarPendenciasEncerramento(estagio).isEmpty());
        assertTrue(estagio.encerrar(ultimoDia.plusDays(3)));
        assertEquals(StatusEstagio.ENCERRADO, estagio.getStatus());
        assertEquals(ultimoDia.plusDays(3), estagio.getDataFimReal());
    }

    private Estagio criarEstagioAtivo() {
        Estagio estagio = DadosExemplo.criarEstagioFormalizado();
        assertTrue(estagio.iniciar(), "O estágio de exemplo deveria iniciar");
        return estagio;
    }

    private LocalDate ultimaDataRegistrada(Estagio estagio) {
        List<RegistroFrequencia> registros = estagio.getRegistrosFrequencia();
        return registros.get(registros.size() - 1).getData();
    }
}
