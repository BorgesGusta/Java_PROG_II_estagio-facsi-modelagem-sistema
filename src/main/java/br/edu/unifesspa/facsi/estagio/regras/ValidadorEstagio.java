package br.edu.unifesspa.facsi.estagio.regras;

import br.edu.unifesspa.facsi.estagio.enums.AmbitoEstagio;
import br.edu.unifesspa.facsi.estagio.enums.ModalidadeEstagio;
import br.edu.unifesspa.facsi.estagio.enums.StatusEstagio;
import br.edu.unifesspa.facsi.estagio.model.ApoliceSeguro;
import br.edu.unifesspa.facsi.estagio.model.Convenio;
import br.edu.unifesspa.facsi.estagio.model.Estagio;
import br.edu.unifesspa.facsi.estagio.model.TermoCompromisso;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ValidadorEstagio {

    // Carga horária mínima definida pelo Regulamento FACSI.
    public static final int CARGA_HORARIA_MINIMA_OBRIGATORIO = 170;
    public static final String RESULTADO_APROVADO = "APROVADO";

    private ValidadorEstagio() {
    }

    public static boolean podeIniciar(Estagio estagio) {
        return estagio.getStatus() == StatusEstagio.EM_FORMALIZACAO
                && estagio.getAmbito() != null
                && possuiPeriodoValido(estagio)
                && estagio.getTermoCompromisso() != null
                && !estagio.getPlanosAtividades().isEmpty()
                && possuiApoliceVigenteNoInicio(estagio)
                && possuiConvenioVigenteQuandoExigido(estagio)
                && possuiContrapartidaQuandoNaoObrigatorio(estagio);
    }

    public static boolean podeEncerrar(Estagio estagio) {
        return estagio.getStatus() == StatusEstagio.ATIVO
                && estagio.getRelatorioFinal() != null
                && estagio.getAvaliacaoEstagio() != null
                && !estagio.getRegistrosFrequencia().isEmpty()
                && RESULTADO_APROVADO.equals(estagio.getAvaliacaoEstagio().getResultado())
                && cumpreCargaHorariaMinima(estagio);
    }

    public static List<String> listarPendenciasEncerramento(Estagio estagio) {
        List<String> pendencias = new ArrayList<>();
        if (estagio.getStatus() != StatusEstagio.ATIVO) {
            pendencias.add("Status: precisa estar ATIVO (atual: " + estagio.getStatus() + ")");
        }
        if (estagio.getRegistrosFrequencia().isEmpty()) {
            pendencias.add("Registros de frequência: nenhum registro");
        }
        if (!cumpreCargaHorariaMinima(estagio)) {
            pendencias.add("Carga horária: " + estagio.calcularHorasRealizadas() + "/"
                    + CARGA_HORARIA_MINIMA_OBRIGATORIO + "h");
        }
        if (estagio.getRelatorioFinal() == null) {
            pendencias.add("Relatório final: PENDENTE");
        }
        if (estagio.getAvaliacaoEstagio() == null) {
            pendencias.add("Avaliação: PENDENTE");
            pendencias.add("Resultado aprovado: PENDENTE");
        } else if (!RESULTADO_APROVADO.equals(estagio.getAvaliacaoEstagio().getResultado())) {
            pendencias.add("Resultado aprovado: PENDENTE (resultado atual: "
                    + estagio.getAvaliacaoEstagio().getResultado() + ")");
        }
        return pendencias;
    }

    public static boolean exigeCargaHorariaMinima(Estagio estagio) {
        return estagio.getModalidade() == ModalidadeEstagio.OBRIGATORIO;
    }

    public static int calcularHorasFaltantes(Estagio estagio) {
        if (!exigeCargaHorariaMinima(estagio)) {
            return 0;
        }
        return Math.max(0, CARGA_HORARIA_MINIMA_OBRIGATORIO - estagio.calcularHorasRealizadas());
    }

    public static boolean exigeConvenio(Estagio estagio) {
        return estagio.getAmbito() == AmbitoEstagio.EXTERNO;
    }

    public static boolean possuiContrapartidaFinanceira(TermoCompromisso termo) {
        if (termo == null) {
            return false;
        }
        boolean possuiBolsa = termo.isRemunerado() && ehPositivo(termo.getValorRemuneracao());
        return possuiBolsa && ehPositivo(termo.getValorAuxilioTransporte());
    }

    private static boolean possuiPeriodoValido(Estagio estagio) {
        return estagio.getDataInicio() != null
                && estagio.getDataFimPrevista() != null
                && !estagio.getDataFimPrevista().isBefore(estagio.getDataInicio());
    }

    private static boolean possuiApoliceVigenteNoInicio(Estagio estagio) {
        for (ApoliceSeguro apolice : estagio.getApolices()) {
            if (apolice.estaVigenteEm(estagio.getDataInicio())) {
                return true;
            }
        }
        return false;
    }

    private static boolean possuiConvenioVigenteQuandoExigido(Estagio estagio) {
        if (!exigeConvenio(estagio)) {
            return true;
        }
        Convenio convenio = estagio.getConvenio();
        return convenio != null && convenio.estaVigenteEm(estagio.getDataInicio());
    }

    private static boolean possuiContrapartidaQuandoNaoObrigatorio(Estagio estagio) {
        return estagio.getModalidade() != ModalidadeEstagio.NAO_OBRIGATORIO
                || possuiContrapartidaFinanceira(estagio.getTermoCompromisso());
    }

    private static boolean cumpreCargaHorariaMinima(Estagio estagio) {
        return estagio.getModalidade() != ModalidadeEstagio.OBRIGATORIO
                || estagio.calcularHorasRealizadas() >= CARGA_HORARIA_MINIMA_OBRIGATORIO;
    }

    private static boolean ehPositivo(BigDecimal valor) {
        return valor != null && valor.signum() > 0;
    }
}
