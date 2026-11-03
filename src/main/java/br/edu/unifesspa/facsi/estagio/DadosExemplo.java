package br.edu.unifesspa.facsi.estagio;

import br.edu.unifesspa.facsi.estagio.enums.AmbitoEstagio;
import br.edu.unifesspa.facsi.estagio.enums.ModalidadeEstagio;
import br.edu.unifesspa.facsi.estagio.enums.OrigemEstagio;
import br.edu.unifesspa.facsi.estagio.model.Aluno;
import br.edu.unifesspa.facsi.estagio.model.ApoliceSeguro;
import br.edu.unifesspa.facsi.estagio.model.Concedente;
import br.edu.unifesspa.facsi.estagio.model.Convenio;
import br.edu.unifesspa.facsi.estagio.model.Estagio;
import br.edu.unifesspa.facsi.estagio.model.PlanoAtividades;
import br.edu.unifesspa.facsi.estagio.model.TermoCompromisso;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DadosExemplo {

    private DadosExemplo() {
    }

    public static Estagio criarEstagioFormalizado() {

        Aluno aluno = new Aluno(
                1L,
                "Aluno Exemplo",
                "000.000.000-00",
                "aluno@unifesspa.edu.br",
                "(94) 99999-0000",
                "2026000000",
                "Sistemas de Informação",
                "ATIVO"
        );

        Concedente concedente =
                new Concedente("Empresa Exemplo Ltda", "00.000.000/0001-00");

        concedente.setId(1L);
        concedente.setTipo("EMPRESA_PRIVADA");

        Convenio convenio =
                new Convenio("CONV-2026/001", concedente);

        convenio.setId(1L);
        convenio.setDataInicio(LocalDate.of(2026, 1, 1));
        convenio.setDataFim(LocalDate.of(2030, 12, 31));
        convenio.setStatus("VIGENTE");

        concedente.adicionarConvenio(convenio);

        Estagio estagio = new Estagio(
                aluno,
                concedente,
                ModalidadeEstagio.OBRIGATORIO,
                OrigemEstagio.CONTATO_DIRETO,
                AmbitoEstagio.EXTERNO
        );

        estagio.setId(1L);
        estagio.setDataInicio(LocalDate.of(2026, 3, 1));
        estagio.setDataFimPrevista(LocalDate.of(2026, 8, 31));
        estagio.setCargaHorariaDiaria(6);
        estagio.setCargaHorariaSemanal(30);
        estagio.setCargaHorariaTotalPrevista(170);
        estagio.setConvenio(convenio);

        TermoCompromisso termo =
                new TermoCompromisso(estagio, LocalDate.of(2026, 2, 15));

        termo.setId(1L);
        termo.setRemunerado(true);
        termo.setValorRemuneracao(new BigDecimal("1000.00"));
        termo.setValorAuxilioTransporte(new BigDecimal("200.00"));
        termo.setStatus("ASSINADO");

        estagio.setTermoCompromisso(termo);

        PlanoAtividades plano =
                new PlanoAtividades(estagio, "Desenvolvimento de Software");

        plano.setId(1L);
        plano.setObjetivoGeral(
                "Aplicar conceitos de Orientação a Objetos"
        );
        plano.setStatus("APROVADO");

        estagio.adicionarPlanoAtividades(plano);

        ApoliceSeguro apolice =
                new ApoliceSeguro("AP-123456", "Seguradora Exemplo");

        apolice.setId(1L);
        apolice.setDataInicio(LocalDate.of(2026, 1, 1));
        apolice.setDataFim(LocalDate.of(2026, 12, 31));

        apolice.adicionarEstagio(estagio);
        estagio.adicionarApolice(apolice);

        return estagio;
    }
}