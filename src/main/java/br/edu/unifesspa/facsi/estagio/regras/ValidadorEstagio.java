package br.edu.unifesspa.facsi.estagio.regras;

import br.edu.unifesspa.facsi.estagio.enums.AmbitoEstagio;
import br.edu.unifesspa.facsi.estagio.enums.ModalidadeEstagio;
import br.edu.unifesspa.facsi.estagio.enums.StatusEstagio;
import br.edu.unifesspa.facsi.estagio.model.Estagio;
import br.edu.unifesspa.facsi.estagio.model.TermoCompromisso;

import java.math.BigDecimal;

public class ValidadorEstagio {

    // Carga horária mínima definida pelo Regulamento FACSI.
    public static final int CARGA_HORARIA_MINIMA_OBRIGATORIO = 170;
    public static final String RESULTADO_APROVADO = "APROVADO";

    private ValidadorEstagio() {
    }

    public static boolean podeIniciar(Estagio estagio) {
        return estagio.getStatus() == StatusEstagio.EM_FORMALIZACAO
                && estagio.getAmbito() != null
                && estagio.getTermoCompromisso() != null
                && !estagio.getPlanosAtividades().isEmpty()
                && !estagio.getApolices().isEmpty()
                && possuiConvenioQuandoExigido(estagio)
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

    private static boolean possuiConvenioQuandoExigido(Estagio estagio) {
        return !exigeConvenio(estagio) || estagio.getConvenio() != null;
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
