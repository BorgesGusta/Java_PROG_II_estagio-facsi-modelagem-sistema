package br.edu.unifesspa.facsi.estagio;

import br.edu.unifesspa.facsi.estagio.enums.AmbitoEstagio;
import br.edu.unifesspa.facsi.estagio.enums.ModalidadeEstagio;
import br.edu.unifesspa.facsi.estagio.enums.OrigemEstagio;
import br.edu.unifesspa.facsi.estagio.model.Aluno;
import br.edu.unifesspa.facsi.estagio.model.ApoliceSeguro;
import br.edu.unifesspa.facsi.estagio.model.AvaliacaoEstagio;
import br.edu.unifesspa.facsi.estagio.model.Candidatura;
import br.edu.unifesspa.facsi.estagio.model.Concedente;
import br.edu.unifesspa.facsi.estagio.model.Convenio;
import br.edu.unifesspa.facsi.estagio.model.Estagio;
import br.edu.unifesspa.facsi.estagio.model.PlanoAtividades;
import br.edu.unifesspa.facsi.estagio.model.Professor;
import br.edu.unifesspa.facsi.estagio.model.Profissional;
import br.edu.unifesspa.facsi.estagio.model.RegistroFrequencia;
import br.edu.unifesspa.facsi.estagio.model.RelatorioFinal;
import br.edu.unifesspa.facsi.estagio.model.TermoCompromisso;
import br.edu.unifesspa.facsi.estagio.model.Vaga;
import br.edu.unifesspa.facsi.estagio.regras.ValidadorCargaHoraria;
import br.edu.unifesspa.facsi.estagio.regras.ValidadorEstagio;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        Estagio estagio = new Estagio(aluno, concedente, ModalidadeEstagio.OBRIGATORIO,
                OrigemEstagio.VAGA_PUBLICADA, AmbitoEstagio.EXTERNO);
        estagio.setId(1L);
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
        estagio.iniciar();

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

        // Relatório final (Sprint 02)
        RelatorioFinal relatorio = new RelatorioFinal(
                estagio, "Desenvolvimento de módulo de cadastro em Java", LocalDate.of(2026, 9, 5));
        relatorio.setId(1L);
        relatorio.setObjetivos("Aplicar Orientação a Objetos no desenvolvimento de um sistema real");
        relatorio.setAtividadesRealizadas("Configuração do ambiente, estudo da arquitetura e correção de bugs");
        relatorio.setConclusao("O estágio permitiu consolidar os conhecimentos de Programação II");
        estagio.setRelatorioFinal(relatorio);

        // Avaliação do estágio (Sprint 02)
        AvaliacaoEstagio avaliacao = new AvaliacaoEstagio(estagio, LocalDate.of(2026, 9, 15));
        avaliacao.setId(1L);
        avaliacao.setParecerSupervisor("Estagiário proativo e com boa evolução técnica");
        avaliacao.setParecerOrientador("Relatório bem estruturado e coerente com o plano de atividades");
        avaliacao.setResultado("APROVADO");
        estagio.setAvaliacaoEstagio(avaliacao);

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
        System.out.println("Relatório final entregue em: " + estagio.getRelatorioFinal().getDataEntrega());
        System.out.println("Tema do relatório: " + estagio.getRelatorioFinal().getTemaObjeto());
        System.out.println("Resultado da avaliação: " + estagio.getAvaliacaoEstagio().getResultado());
        System.out.println("Parecer do supervisor: " + estagio.getAvaliacaoEstagio().getParecerSupervisor());
        System.out.println("Status do estágio (sem encerramento automático): " + estagio.getStatus());

        System.out.println();
        System.out.println("=== Cenário válido ===");
        LocalDate inicioSemestre = LocalDate.of(2026, 3, 1);
        LocalDate fimSemestre = LocalDate.of(2026, 6, 30);
        Estagio estagioValido = criarEstagioAtivo(aluno, concedente, ModalidadeEstagio.OBRIGATORIO,
                inicioSemestre, fimSemestre);
        registrarHoras(estagioValido, LocalDate.of(2026, 3, 9), 4);
        registrarHoras(estagioValido, LocalDate.of(2026, 3, 10), 5);
        imprimirLimites(estagioValido);

        System.out.println();
        System.out.println("=== Cenário inválido diário (7h em 2026-03-09) ===");
        Estagio estagioExcessoDiario = criarEstagioAtivo(aluno, concedente, ModalidadeEstagio.OBRIGATORIO,
                inicioSemestre, fimSemestre);
        registrarHoras(estagioExcessoDiario, LocalDate.of(2026, 3, 9), 4);
        registrarHoras(estagioExcessoDiario, LocalDate.of(2026, 3, 9), 3);
        imprimirLimites(estagioExcessoDiario);

        System.out.println();
        System.out.println("=== Cenário inválido semanal (6h por dia de segunda a sábado) ===");
        Estagio estagioExcessoSemanal = criarEstagioAtivo(aluno, concedente, ModalidadeEstagio.OBRIGATORIO,
                inicioSemestre, fimSemestre);
        LocalDate segundaFeira = LocalDate.of(2026, 3, 9);
        for (int dia = 0; dia < 6; dia++) {
            registrarHoras(estagioExcessoSemanal, segundaFeira.plusDays(dia), 6);
        }
        System.out.println("Horas na semana: " + estagioExcessoSemanal.calcularHorasNaSemana(segundaFeira));
        imprimirLimites(estagioExcessoSemanal);

        System.out.println();
        System.out.println("=== Estágios simultâneos do mesmo aluno ===");
        Estagio estagioNaoObrigatorio = criarEstagioAtivo(aluno, concedente, ModalidadeEstagio.NAO_OBRIGATORIO,
                inicioSemestre, fimSemestre);
        registrarHoras(estagioNaoObrigatorio, LocalDate.of(2026, 3, 5), 6);
        registrarHoras(estagioNaoObrigatorio, LocalDate.of(2026, 3, 6), 6);
        List<Estagio> estagiosDoAluno = new ArrayList<>(List.of(estagio, estagioNaoObrigatorio));
        imprimirLimitesSimultaneos(aluno, estagiosDoAluno);

        System.out.println("-- Após registrar 3h em 2026-03-04 no estágio não obrigatório --");
        registrarHoras(estagioNaoObrigatorio, LocalDate.of(2026, 3, 4), 3);
        imprimirLimitesSimultaneos(aluno, estagiosDoAluno);

        System.out.println();
        System.out.println("=== Limite de estágios não obrigatórios simultâneos ===");
        Estagio segundoNaoObrigatorio = criarEstagioAtivo(aluno, concedente, ModalidadeEstagio.NAO_OBRIGATORIO,
                inicioSemestre, fimSemestre);
        Estagio terceiroNaoObrigatorio = criarEstagioEmFormalizacao(aluno, concedente, ModalidadeEstagio.NAO_OBRIGATORIO,
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 5, 31));
        estagiosDoAluno.add(segundoNaoObrigatorio);
        estagiosDoAluno.add(terceiroNaoObrigatorio);
        System.out.println("Dois ativos e um em formalização: "
                + ValidadorCargaHoraria.respeitaLimiteEstagiosNaoObrigatorios(aluno, estagiosDoAluno));

        terceiroNaoObrigatorio.iniciar();
        System.out.println("Três ativos: "
                + ValidadorCargaHoraria.respeitaLimiteEstagiosNaoObrigatorios(aluno, estagiosDoAluno));

        System.out.println();
        System.out.println("=== Estágios ativos sem sobreposição de período ===");
        Estagio estagioPrimeiroSemestre = criarEstagioAtivo(aluno, concedente, ModalidadeEstagio.NAO_OBRIGATORIO,
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 6, 30));
        Estagio estagioSegundoSemestre = criarEstagioAtivo(aluno, concedente, ModalidadeEstagio.NAO_OBRIGATORIO,
                LocalDate.of(2026, 7, 1), LocalDate.of(2026, 8, 31));
        registrarHoras(estagioPrimeiroSemestre, LocalDate.of(2026, 3, 9), 6);
        registrarHoras(estagioSegundoSemestre, LocalDate.of(2026, 7, 6), 6);
        List<Estagio> estagiosEmPeriodosDiferentes = List.of(estagioPrimeiroSemestre, estagioSegundoSemestre);
        System.out.println("Períodos sobrepostos: "
                + estagioPrimeiroSemestre.possuiPeriodoSobreposto(estagioSegundoSemestre));
        imprimirLimitesSimultaneos(aluno, estagiosEmPeriodosDiferentes);

        System.out.println();
        System.out.println("=== Três não obrigatórios que nunca estão ativos ao mesmo tempo ===");
        Estagio estagioA = criarEstagioAtivo(aluno, concedente, ModalidadeEstagio.NAO_OBRIGATORIO,
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 6, 30));
        Estagio estagioB = criarEstagioAtivo(aluno, concedente, ModalidadeEstagio.NAO_OBRIGATORIO,
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 8, 31));
        Estagio estagioC = criarEstagioAtivo(aluno, concedente, ModalidadeEstagio.NAO_OBRIGATORIO,
                LocalDate.of(2026, 7, 1), LocalDate.of(2026, 10, 31));
        System.out.println("A e B sobrepostos: " + estagioA.possuiPeriodoSobreposto(estagioB));
        System.out.println("B e C sobrepostos: " + estagioB.possuiPeriodoSobreposto(estagioC));
        System.out.println("A e C sobrepostos: " + estagioA.possuiPeriodoSobreposto(estagioC));
        System.out.println("Limite de não obrigatórios válido: "
                + ValidadorCargaHoraria.respeitaLimiteEstagiosNaoObrigatorios(aluno, List.of(estagioA, estagioB, estagioC)));

        System.out.println();
        System.out.println("=== Tentativa inválida de início (sem documentos) ===");
        Estagio estagioSemDocumentos = new Estagio(aluno, concedente, ModalidadeEstagio.OBRIGATORIO,
                OrigemEstagio.CONTATO_DIRETO, AmbitoEstagio.EXTERNO);
        System.out.println("Pode iniciar: " + ValidadorEstagio.podeIniciar(estagioSemDocumentos));
        estagioSemDocumentos.iniciar();
        System.out.println("Status: " + estagioSemDocumentos.getStatus());

        System.out.println();
        System.out.println("=== Tentativa válida de início ===");
        Estagio estagioFormalizado = criarEstagioEmFormalizacao(aluno, concedente, ModalidadeEstagio.OBRIGATORIO,
                inicioSemestre, fimSemestre);
        System.out.println("Pode iniciar: " + ValidadorEstagio.podeIniciar(estagioFormalizado));
        estagioFormalizado.iniciar();
        System.out.println("Status: " + estagioFormalizado.getStatus());

        System.out.println();
        System.out.println("=== Encerramento inválido (obrigatório com menos de 170h) ===");
        System.out.println("Horas realizadas: " + estagio.calcularHorasRealizadas());
        System.out.println("Pode encerrar: " + ValidadorEstagio.podeEncerrar(estagio));
        estagio.encerrar(LocalDate.of(2026, 8, 31));
        System.out.println("Status: " + estagio.getStatus());
        System.out.println("Data fim real: " + estagio.getDataFimReal());

        System.out.println();
        System.out.println("=== Encerramento válido ===");
        Estagio estagioConcluido = criarEstagioAtivo(aluno, concedente, ModalidadeEstagio.OBRIGATORIO,
                inicioSemestre, fimSemestre);
        LocalDate dia = inicioSemestre;
        while (estagioConcluido.calcularHorasRealizadas() < ValidadorEstagio.CARGA_HORARIA_MINIMA_OBRIGATORIO) {
            if (dia.getDayOfWeek() != DayOfWeek.SATURDAY && dia.getDayOfWeek() != DayOfWeek.SUNDAY) {
                registrarHoras(estagioConcluido, dia, 6);
            }
            dia = dia.plusDays(1);
        }
        List<RegistroFrequencia> registrosConcluidos = estagioConcluido.getRegistrosFrequencia();
        LocalDate ultimoDiaDeAtividade = registrosConcluidos.get(registrosConcluidos.size() - 1).getData();
        estagioConcluido.setRelatorioFinal(new RelatorioFinal(
                estagioConcluido, "Manutenção de sistemas web", ultimoDiaDeAtividade.plusDays(5)));
        AvaliacaoEstagio avaliacaoAprovada = new AvaliacaoEstagio(estagioConcluido, ultimoDiaDeAtividade.plusDays(10));
        avaliacaoAprovada.setResultado(ValidadorEstagio.RESULTADO_APROVADO);
        estagioConcluido.setAvaliacaoEstagio(avaliacaoAprovada);
        System.out.println("Horas realizadas: " + estagioConcluido.calcularHorasRealizadas());
        System.out.println("Limites diário e semanal respeitados: "
                + (estagioConcluido.respeitaLimiteDiario() && estagioConcluido.respeitaLimiteSemanal()));
        System.out.println("Pode encerrar: " + ValidadorEstagio.podeEncerrar(estagioConcluido));
        estagioConcluido.encerrar(ultimoDiaDeAtividade);
        System.out.println("Status: " + estagioConcluido.getStatus());
        System.out.println("Data fim real: " + estagioConcluido.getDataFimReal());

        System.out.println();
        System.out.println("=== Estágio EXTERNO sem convênio ===");
        Estagio estagioExternoSemConvenio = criarEstagioEmFormalizacao(aluno, concedente,
                ModalidadeEstagio.OBRIGATORIO, inicioSemestre, fimSemestre);
        estagioExternoSemConvenio.setConvenio(null);
        System.out.println("Exige convênio: " + ValidadorEstagio.exigeConvenio(estagioExternoSemConvenio));
        System.out.println("Pode iniciar: " + ValidadorEstagio.podeIniciar(estagioExternoSemConvenio));

        System.out.println();
        System.out.println("=== Estágio INTERNO sem convênio ===");
        Estagio estagioInterno = criarEstagioEmFormalizacao(aluno, concedente,
                ModalidadeEstagio.OBRIGATORIO, inicioSemestre, fimSemestre);
        estagioInterno.setConvenio(null);
        estagioInterno.setAmbito(AmbitoEstagio.INTERNO);
        System.out.println("Exige convênio: " + ValidadorEstagio.exigeConvenio(estagioInterno));
        System.out.println("Pode iniciar: " + ValidadorEstagio.podeIniciar(estagioInterno));
        estagioInterno.iniciar();
        System.out.println("Status: " + estagioInterno.getStatus());

        System.out.println();
        System.out.println("=== Não obrigatório sem bolsa ===");
        Estagio estagioSemBolsa = criarEstagioEmFormalizacao(aluno, concedente,
                ModalidadeEstagio.NAO_OBRIGATORIO, inicioSemestre, fimSemestre);
        estagioSemBolsa.getTermoCompromisso().setRemunerado(false);
        estagioSemBolsa.getTermoCompromisso().setValorRemuneracao(null);
        System.out.println("Pode iniciar: " + ValidadorEstagio.podeIniciar(estagioSemBolsa));

        System.out.println();
        System.out.println("=== Não obrigatório sem auxílio-transporte ===");
        Estagio estagioSemAuxilioTransporte = criarEstagioEmFormalizacao(aluno, concedente,
                ModalidadeEstagio.NAO_OBRIGATORIO, inicioSemestre, fimSemestre);
        estagioSemAuxilioTransporte.getTermoCompromisso().setValorAuxilioTransporte(BigDecimal.ZERO);
        System.out.println("Pode iniciar: " + ValidadorEstagio.podeIniciar(estagioSemAuxilioTransporte));

        System.out.println();
        System.out.println("=== Não obrigatório com bolsa e auxílio-transporte ===");
        Estagio estagioComContrapartida = criarEstagioEmFormalizacao(aluno, concedente,
                ModalidadeEstagio.NAO_OBRIGATORIO, inicioSemestre, fimSemestre);
        System.out.println("Pode iniciar: " + ValidadorEstagio.podeIniciar(estagioComContrapartida));

        System.out.println();
        System.out.println("=== Estágio sem âmbito definido ===");
        Estagio estagioSemAmbito = criarEstagioEmFormalizacao(aluno, concedente,
                ModalidadeEstagio.OBRIGATORIO, inicioSemestre, fimSemestre);
        estagioSemAmbito.setAmbito(null);
        System.out.println("Pode iniciar: " + ValidadorEstagio.podeIniciar(estagioSemAmbito));
    }

    private static Estagio criarEstagioEmFormalizacao(
            Aluno aluno,
            Concedente concedente,
            ModalidadeEstagio modalidade,
            LocalDate dataInicio,
            LocalDate dataFimPrevista
    ) {
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

        ApoliceSeguro apolice = new ApoliceSeguro("AP-000001", "Seguradora Exemplo");
        apolice.adicionarEstagio(estagio);
        estagio.adicionarApolice(apolice);
        return estagio;
    }

    private static Estagio criarEstagioAtivo(
            Aluno aluno,
            Concedente concedente,
            ModalidadeEstagio modalidade,
            LocalDate dataInicio,
            LocalDate dataFimPrevista
    ) {
        Estagio estagio = criarEstagioEmFormalizacao(aluno, concedente, modalidade, dataInicio, dataFimPrevista);
        estagio.iniciar();
        return estagio;
    }

    private static void registrarHoras(Estagio estagio, LocalDate data, int horas) {
        estagio.adicionarRegistroFrequencia(new RegistroFrequencia(data, horas, "Atividade de exemplo", estagio));
    }

    private static void imprimirLimites(Estagio estagio) {
        System.out.println("Limite diário válido: " + estagio.respeitaLimiteDiario());
        System.out.println("Limite semanal válido: " + estagio.respeitaLimiteSemanal());
    }

    private static void imprimirLimitesSimultaneos(Aluno aluno, List<Estagio> estagios) {
        System.out.println("Limite diário válido (soma dos estágios): "
                + ValidadorCargaHoraria.respeitaLimiteDiario(aluno, estagios));
        System.out.println("Limite semanal válido (soma dos estágios): "
                + ValidadorCargaHoraria.respeitaLimiteSemanal(aluno, estagios));
    }
}
