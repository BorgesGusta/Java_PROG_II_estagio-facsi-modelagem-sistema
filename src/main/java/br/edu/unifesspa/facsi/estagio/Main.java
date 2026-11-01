package br.edu.unifesspa.facsi.estagio;

import br.edu.unifesspa.facsi.estagio.enums.ModalidadeEstagio;
import br.edu.unifesspa.facsi.estagio.enums.OrigemEstagio;
import br.edu.unifesspa.facsi.estagio.enums.StatusEstagio;
import br.edu.unifesspa.facsi.estagio.model.Aluno;
import br.edu.unifesspa.facsi.estagio.model.ApoliceSeguro;
import br.edu.unifesspa.facsi.estagio.model.Candidatura;
import br.edu.unifesspa.facsi.estagio.model.Concedente;
import br.edu.unifesspa.facsi.estagio.model.Convenio;
import br.edu.unifesspa.facsi.estagio.model.Estagio;
import br.edu.unifesspa.facsi.estagio.model.PlanoAtividades;
import br.edu.unifesspa.facsi.estagio.model.Professor;
import br.edu.unifesspa.facsi.estagio.model.Profissional;
import br.edu.unifesspa.facsi.estagio.model.RegistroFrequencia;
import br.edu.unifesspa.facsi.estagio.model.TermoCompromisso;
import br.edu.unifesspa.facsi.estagio.model.Vaga;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        // 1. Aluno
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

        // 2. Professor (será o orientador)
        Professor professor = new Professor(
                2L,
                "Professor Exemplo",
                "111.111.111-11",
                "professor@unifesspa.edu.br",
                "(94) 99999-1111",
                "1234567",
                "FACSI"
        );

        // Profissional da concedente que exercerá o papel de supervisor
        Profissional supervisor = new Profissional(
                3L,
                "Supervisor Exemplo",
                "222.222.222-22",
                "supervisor@empresaexemplo.com.br",
                "(94) 99999-2222",
                "Analista de Sistemas",
                "Coordenador de Desenvolvimento",
                "REG-0001"
        );

        // 3. Concedente
        Concedente concedente = new Concedente("Empresa Exemplo Ltda", "00.000.000/0001-00");
        concedente.setId(1L);
        concedente.setTipo("EMPRESA_PRIVADA");

        // 4. Convênio
        Convenio convenio = new Convenio("CONV-2026/001", concedente);
        convenio.setId(1L);
        convenio.setDataInicio(LocalDate.of(2026, 1, 1));
        convenio.setDataFim(LocalDate.of(2030, 12, 31));
        convenio.setStatus("VIGENTE");
        concedente.adicionarConvenio(convenio);

        // 5. Vaga
        Vaga vaga = new Vaga("Estágio em Desenvolvimento Java", concedente);
        vaga.setId(1L);
        vaga.setModalidade(ModalidadeEstagio.OBRIGATORIO);
        vaga.setCargaHorariaDiaria(6);
        vaga.setCargaHorariaSemanal(30);
        vaga.setValorBolsaOfertado(new BigDecimal("1000.00"));
        vaga.setValorAuxilioTransporteOfertado(new BigDecimal("200.00"));
        vaga.setStatus("ABERTA");

        // 6. Candidatura
        Candidatura candidatura = new Candidatura(aluno, vaga, LocalDate.of(2026, 2, 1));
        candidatura.setId(1L);
        candidatura.setStatus("APROVADA");
        candidatura.setDataDecisao(LocalDate.of(2026, 2, 10));

        // 7. Estágio
        Estagio estagio = new Estagio(aluno, concedente, ModalidadeEstagio.OBRIGATORIO, OrigemEstagio.VAGA_PUBLICADA);
        estagio.setId(1L);
        estagio.setStatus(StatusEstagio.EM_FORMALIZACAO);
        estagio.setDataInicio(LocalDate.of(2026, 3, 1));
        estagio.setDataFimPrevista(LocalDate.of(2026, 8, 31));
        estagio.setCargaHorariaDiaria(6);
        estagio.setCargaHorariaSemanal(30);
        estagio.setCargaHorariaTotalPrevista(300);
        estagio.setConvenio(convenio);
        estagio.setVaga(vaga);
        estagio.setCandidatura(candidatura);

        // 8. Supervisor e Orientador
        estagio.setSupervisor(supervisor);
        estagio.setOrientador(professor);

        // 9. Termo de Compromisso
        TermoCompromisso termo = new TermoCompromisso(estagio, LocalDate.of(2026, 2, 15));
        termo.setId(1L);
        termo.setRemunerado(true);
        termo.setValorRemuneracao(new BigDecimal("1000.00"));
        termo.setValorAuxilioTransporte(new BigDecimal("200.00"));
        termo.setStatus("ASSINADO");
        estagio.setTermoCompromisso(termo);

        // 10. Plano de Atividades
        PlanoAtividades plano = new PlanoAtividades(estagio, "Desenvolvimento de Software");
        plano.setId(1L);
        plano.setObjetivoGeral("Aplicar conceitos de Orientação a Objetos em projeto real");
        plano.setStatus("APROVADO");
        estagio.adicionarPlanoAtividades(plano);

        // 11. Apólice de Seguro
        ApoliceSeguro apolice = new ApoliceSeguro("AP-123456", "Seguradora Exemplo");
        apolice.setId(1L);
        apolice.setDataInicio(LocalDate.of(2026, 3, 1));
        apolice.setDataFim(LocalDate.of(2026, 12, 31));
        apolice.adicionarEstagio(estagio);
        estagio.adicionarApolice(apolice);

        // Registros de frequência (Sprint 02)
        RegistroFrequencia registro1 = new RegistroFrequencia(
                LocalDate.of(2026, 3, 2), 6, "Configuração do ambiente de desenvolvimento", estagio);
        registro1.setId(1L);
        RegistroFrequencia registro2 = new RegistroFrequencia(
                LocalDate.of(2026, 3, 3), 6, "Estudo da arquitetura do sistema da empresa", estagio);
        registro2.setId(2L);
        RegistroFrequencia registro3 = new RegistroFrequencia(
                LocalDate.of(2026, 3, 4), 4, "Correção de bugs no módulo de cadastro", estagio);
        registro3.setId(3L);
        estagio.adicionarRegistroFrequencia(registro1);
        estagio.adicionarRegistroFrequencia(registro2);
        estagio.adicionarRegistroFrequencia(registro3);

        // 12. Demonstração
        System.out.println("=== Estágio #" + estagio.getId() + " ===");
        System.out.println("Aluno: " + estagio.getAluno().getNome() + " (" + estagio.getAluno().getMatricula() + ")");
        System.out.println("Concedente: " + estagio.getConcedente().getNome());
        System.out.println("Convênios da concedente: " + concedente.getConvenios().size());
        System.out.println("Convênio: " + estagio.getConvenio().getNumeroReferencia());
        System.out.println("Vaga: " + estagio.getVaga().getTitulo() + " - " + estagio.getVaga().getConcedente().getNome());
        System.out.println("Candidatura: " + estagio.getCandidatura().getStatus()
                + " em " + estagio.getCandidatura().getDataCandidatura());
        System.out.println("Modalidade: " + estagio.getModalidade());
        System.out.println("Origem: " + estagio.getOrigem());
        System.out.println("Status: " + estagio.getStatus());
        System.out.println("Período: " + estagio.getDataInicio() + " a " + estagio.getDataFimPrevista());
        System.out.println("Supervisor: " + estagio.getSupervisor().getNome() + " (" + supervisor.getCargo() + ")");
        System.out.println("Orientador: " + estagio.getOrientador().getNome());
        System.out.println("Termo remunerado: " + estagio.getTermoCompromisso().isRemunerado()
                + " (R$ " + estagio.getTermoCompromisso().getValorRemuneracao() + ")");
        System.out.println("Planos de atividades: " + estagio.getPlanosAtividades().size()
                + " - " + estagio.getPlanosAtividades().get(0).getAreaAtuacao());
        System.out.println("Apólices: " + estagio.getApolices().size()
                + " - " + estagio.getApolices().get(0).getNumero()
                + " (cobre " + apolice.getEstagios().size() + " estágio(s))");
        System.out.println("Registros de frequência: " + estagio.getRegistrosFrequencia().size());
        System.out.println("Horas realizadas: " + estagio.calcularHorasRealizadas());
        System.out.println("Primeira atividade: " + estagio.getRegistrosFrequencia().get(0).getData()
                + " - " + estagio.getRegistrosFrequencia().get(0).getDescricaoAtividade());
    }
}
