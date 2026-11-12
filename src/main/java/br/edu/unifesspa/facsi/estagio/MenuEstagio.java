package br.edu.unifesspa.facsi.estagio;

import br.edu.unifesspa.facsi.estagio.enums.StatusEstagio;
import br.edu.unifesspa.facsi.estagio.model.AvaliacaoEstagio;
import br.edu.unifesspa.facsi.estagio.model.Estagio;
import br.edu.unifesspa.facsi.estagio.model.RegistroFrequencia;
import br.edu.unifesspa.facsi.estagio.model.RelatorioFinal;
import br.edu.unifesspa.facsi.estagio.regras.ValidadorEstagio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Scanner;

public class MenuEstagio {

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private MenuEstagio() {
    }

    public static void executar(Scanner scanner, Estagio estagio) {
        int opcao;
        do {
            System.out.println();
            System.out.println("========================================");
            System.out.println("               ESTÁGIOS");
            System.out.println("========================================");
            System.out.println();
            System.out.println("1 - Ver estágio");
            System.out.println("2 - Iniciar estágio");
            System.out.println("3 - Registrar frequência");
            System.out.println("4 - Ver registros de frequência");
            System.out.println("5 - Consultar carga horária");
            System.out.println("6 - Registrar relatório final");
            System.out.println("7 - Registrar avaliação");
            System.out.println("8 - Ver pendências");
            System.out.println("9 - Encerrar estágio");
            System.out.println("10 - Preencher carga horária de demonstração");
            System.out.println("0 - Voltar");
            System.out.println();

            opcao = lerInteiro(scanner, "Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    exibirEstagio(estagio);
                    break;
                case 2:
                    iniciarEstagio(estagio);
                    break;
                case 3:
                    registrarFrequencia(scanner, estagio);
                    break;
                case 4:
                    exibirRegistrosFrequencia(estagio);
                    break;
                case 5:
                    consultarCargaHoraria(estagio);
                    break;
                case 6:
                    registrarRelatorioFinal(scanner, estagio);
                    break;
                case 7:
                    registrarAvaliacao(scanner, estagio);
                    break;
                case 8:
                    exibirPendencias(estagio);
                    break;
                case 9:
                    encerrarEstagio(scanner, estagio);
                    break;
                case 10:
                    preencherCargaHorariaDemonstracao(estagio);
                    break;
                case 0:
                    break;
                default:
                    System.out.println();
                    System.out.println("Opção inválida.");
            }
            if (opcao >= 1 && opcao <= 10) {
                pausar(scanner);
            }
        } while (opcao != 0);
    }

    private static void exibirEstagio(Estagio estagio) {
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("ESTÁGIO #" + estagio.getId());
        System.out.println("----------------------------------------");
        System.out.println("Aluno: " + estagio.getAluno().getNome());
        System.out.println("Matrícula: " + estagio.getAluno().getMatricula());
        System.out.println("Concedente: " + estagio.getConcedente().getNome());
        System.out.println("Modalidade: " + estagio.getModalidade());
        System.out.println("Âmbito: " + estagio.getAmbito());
        System.out.println("Status: " + estagio.getStatus());
        System.out.println("Período: " + formatarData(estagio.getDataInicio())
                + " a " + formatarData(estagio.getDataFimPrevista()));
        System.out.println("Horas realizadas: " + estagio.calcularHorasRealizadas());
        System.out.println("Registros de frequência: " + estagio.getRegistrosFrequencia().size());
        System.out.println("Relatório final: " + (estagio.getRelatorioFinal() != null ? "REGISTRADO" : "PENDENTE"));
        System.out.println("Avaliação: " + (estagio.getAvaliacaoEstagio() != null
                ? estagio.getAvaliacaoEstagio().getResultado() : "PENDENTE"));
        if (estagio.getDataFimReal() != null) {
            System.out.println("Data de encerramento: " + formatarData(estagio.getDataFimReal()));
        }
        System.out.println("----------------------------------------");
    }

    private static void iniciarEstagio(Estagio estagio) {
        System.out.println();

        if (estagio.getStatus() == StatusEstagio.ATIVO) {
            System.out.println("O estágio já está ATIVO.");
            return;
        }
        if (estagio.getStatus() == StatusEstagio.ENCERRADO) {
            System.out.println("O estágio já está ENCERRADO.");
            return;
        }

        if (estagio.iniciar()) {
            System.out.println("Estágio iniciado com sucesso.");
        } else {
            System.out.println("Não foi possível iniciar o estágio.");
            System.out.println("Verifique período, termo, plano, seguro, convênio e demais requisitos.");
        }
        System.out.println("Status atual: " + estagio.getStatus());
    }

    private static void registrarFrequencia(Scanner scanner, Estagio estagio) {
        System.out.println();

        if (!estaAtivo(estagio, "registrar frequência")) {
            return;
        }

        LocalDate data = lerData(scanner, "Data da atividade (DD/MM/AAAA): ");

        int horas = lerInteiro(scanner, "Quantidade de horas: ");
        while (horas <= 0) {
            System.out.println("A quantidade de horas deve ser maior que zero.");
            horas = lerInteiro(scanner, "Quantidade de horas: ");
        }

        String descricao = lerTextoObrigatorio(scanner, "Descrição da atividade: ");

        boolean registrado = estagio.adicionarRegistroFrequencia(new RegistroFrequencia(data, horas, descricao, estagio));

        System.out.println();
        if (!registrado) {
            System.out.println("Frequência não registrada.");
            System.out.println("A data deve estar dentro do período do estágio: "
                    + formatarData(estagio.getDataInicio()) + " a " + formatarData(estagio.getDataFimPrevista()) + ".");
            return;
        }

        System.out.println("Frequência registrada.");
        System.out.println("Horas registradas nesta data: " + estagio.calcularHorasNaData(data));
        System.out.println("Total de horas realizadas: " + estagio.calcularHorasRealizadas());
        System.out.println("Limite diário válido: " + estagio.respeitaLimiteDiario());
        System.out.println("Limite semanal válido: " + estagio.respeitaLimiteSemanal());

        if (estagio.calcularHorasNaData(data) > Estagio.LIMITE_HORAS_DIARIAS) {
            System.out.println("ATENÇÃO: limite diário de " + Estagio.LIMITE_HORAS_DIARIAS + " horas excedido.");
        }
        if (estagio.calcularHorasNaSemana(data) > Estagio.LIMITE_HORAS_SEMANAIS) {
            System.out.println("ATENÇÃO: limite semanal de " + Estagio.LIMITE_HORAS_SEMANAIS + " horas excedido.");
        }
    }

    private static void exibirRegistrosFrequencia(Estagio estagio) {
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("        REGISTROS DE FREQUÊNCIA");
        System.out.println("----------------------------------------");

        List<RegistroFrequencia> registros = estagio.getRegistrosFrequencia();
        if (registros.isEmpty()) {
            System.out.println("Nenhum registro de frequência.");
            return;
        }

        for (RegistroFrequencia registro : registros) {
            System.out.println();
            System.out.println(formatarData(registro.getData()) + " - " + registro.getQuantidadeHoras() + "h");
            System.out.println(registro.getDescricaoAtividade());
        }
        System.out.println();
        System.out.println("Total de horas: " + estagio.calcularHorasRealizadas() + "h");
    }

    private static void consultarCargaHoraria(Estagio estagio) {
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("            CARGA HORÁRIA");
        System.out.println("----------------------------------------");
        System.out.println("Horas realizadas: " + estagio.calcularHorasRealizadas() + "h");

        if (ValidadorEstagio.exigeCargaHorariaMinima(estagio)) {
            System.out.println("Carga mínima obrigatória: " + ValidadorEstagio.CARGA_HORARIA_MINIMA_OBRIGATORIO + "h");
            int faltantes = ValidadorEstagio.calcularHorasFaltantes(estagio);
            if (faltantes > 0) {
                System.out.println("Faltam: " + faltantes + "h");
            } else {
                System.out.println("Carga mínima atingida.");
            }
        } else {
            System.out.println("Estágio " + estagio.getModalidade() + ": sem carga mínima automática.");
        }

        System.out.println("Limite diário (" + Estagio.LIMITE_HORAS_DIARIAS + "h) respeitado: "
                + estagio.respeitaLimiteDiario());
        System.out.println("Limite semanal (" + Estagio.LIMITE_HORAS_SEMANAIS + "h) respeitado: "
                + estagio.respeitaLimiteSemanal());
    }

    private static void registrarRelatorioFinal(Scanner scanner, Estagio estagio) {
        System.out.println();

        if (!estaAtivo(estagio, "registrar o relatório final")) {
            return;
        }
        if (estagio.getRelatorioFinal() != null
                && !lerConfirmacao(scanner, "Já existe um relatório final. Deseja substituí-lo? (S/N): ")) {
            System.out.println("Relatório mantido.");
            return;
        }

        LocalDate dataEntrega = lerDataAPartirDoInicio(scanner, estagio, "Data de entrega (DD/MM/AAAA): ");
        String tema = lerTextoObrigatorio(scanner, "Tema/objeto: ");

        RelatorioFinal relatorio = new RelatorioFinal(estagio, tema, dataEntrega);
        relatorio.setJustificativa(lerTextoOpcional(scanner, "Justificativa (opcional): "));
        relatorio.setObjetivos(lerTextoOpcional(scanner, "Objetivos (opcional): "));
        relatorio.setMetodologia(lerTextoOpcional(scanner, "Metodologia (opcional): "));
        relatorio.setAtividadesRealizadas(lerTextoOpcional(scanner, "Atividades realizadas (opcional): "));
        relatorio.setDificuldadesEncontradas(lerTextoOpcional(scanner, "Dificuldades encontradas (opcional): "));
        relatorio.setConclusao(lerTextoOpcional(scanner, "Conclusão (opcional): "));
        estagio.setRelatorioFinal(relatorio);

        System.out.println();
        System.out.println("Relatório final registrado em " + formatarData(dataEntrega) + ".");
    }

    private static void registrarAvaliacao(Scanner scanner, Estagio estagio) {
        System.out.println();

        if (!estaAtivo(estagio, "registrar a avaliação")) {
            return;
        }
        if (estagio.getRelatorioFinal() == null) {
            System.out.println("É necessário registrar o relatório final antes da avaliação.");
            return;
        }
        if (estagio.getAvaliacaoEstagio() != null
                && !lerConfirmacao(scanner, "Já existe uma avaliação. Deseja substituí-la? (S/N): ")) {
            System.out.println("Avaliação mantida.");
            return;
        }

        LocalDate dataAvaliacao = lerDataAPartirDoInicio(scanner, estagio, "Data da avaliação (DD/MM/AAAA): ");

        AvaliacaoEstagio avaliacao = new AvaliacaoEstagio(estagio, dataAvaliacao);
        avaliacao.setParecerSupervisor(lerTextoOpcional(scanner, "Parecer do supervisor (opcional): "));
        avaliacao.setParecerOrientador(lerTextoOpcional(scanner, "Parecer do orientador (opcional): "));
        String resultado = lerTextoObrigatorio(scanner,
                "Resultado (" + ValidadorEstagio.RESULTADO_APROVADO + " para aprovar): ");
        avaliacao.setResultado(resultado.toUpperCase());
        avaliacao.setObservacao(lerTextoOpcional(scanner, "Observação (opcional): "));
        estagio.setAvaliacaoEstagio(avaliacao);

        System.out.println();
        System.out.println("Avaliação registrada com resultado " + avaliacao.getResultado() + ".");
    }

    private static void exibirPendencias(Estagio estagio) {
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("    PENDÊNCIAS PARA ENCERRAMENTO");
        System.out.println("----------------------------------------");
        imprimirPendencias(estagio);
    }

    private static void imprimirPendencias(Estagio estagio) {
        List<String> pendencias = ValidadorEstagio.listarPendenciasEncerramento(estagio);
        if (pendencias.isEmpty()) {
            System.out.println("Nenhuma pendência. O estágio pode ser encerrado.");
            return;
        }
        for (String pendencia : pendencias) {
            System.out.println(pendencia);
        }
    }

    private static void encerrarEstagio(Scanner scanner, Estagio estagio) {
        System.out.println();

        if (estagio.getStatus() != StatusEstagio.ATIVO) {
            System.out.println("Somente um estágio ATIVO pode ser encerrado.");
            System.out.println("Status atual: " + estagio.getStatus());
            return;
        }

        LocalDate data = lerData(scanner, "Data de encerramento (DD/MM/AAAA): ");

        System.out.println();
        if (estagio.encerrar(data)) {
            System.out.println("Estágio encerrado com sucesso.");
            System.out.println("Data de encerramento: " + formatarData(estagio.getDataFimReal()));
        } else if (ValidadorEstagio.podeEncerrar(estagio)) {
            System.out.println("Data de encerramento inválida.");
            System.out.println("Ela não pode ser anterior ao início do estágio nem ao último registro de frequência.");
        } else {
            System.out.println("O estágio ainda não atende aos requisitos de encerramento:");
            imprimirPendencias(estagio);
        }

        System.out.println();
        System.out.println("Status atual: " + estagio.getStatus());
    }

    private static void preencherCargaHorariaDemonstracao(Estagio estagio) {
        System.out.println();
        System.out.println("[DEMONSTRAÇÃO] Preenchimento automático de frequência");

        if (!estaAtivo(estagio, "preencher a carga horária")) {
            return;
        }
        if (!ValidadorEstagio.exigeCargaHorariaMinima(estagio)) {
            System.out.println("Este estágio não possui carga mínima automática.");
            return;
        }

        int adicionados = DadosExemplo.preencherCargaHorariaMinima(estagio);

        System.out.println("Registros adicionados: " + adicionados);
        System.out.println("Horas realizadas: " + estagio.calcularHorasRealizadas() + "h");
        if (ValidadorEstagio.calcularHorasFaltantes(estagio) > 0) {
            System.out.println("Não há dias úteis suficientes no período. Faltam: "
                    + ValidadorEstagio.calcularHorasFaltantes(estagio) + "h");
        } else {
            System.out.println("Carga mínima atingida.");
        }
    }

    private static boolean estaAtivo(Estagio estagio, String acao) {
        if (estagio.getStatus() == StatusEstagio.ATIVO) {
            return true;
        }
        System.out.println("O estágio precisa estar ATIVO para " + acao + ".");
        System.out.println("Status atual: " + estagio.getStatus());
        return false;
    }

    static String lerLinha(Scanner scanner) {
        if (!scanner.hasNextLine()) {
            throw new EntradaEncerradaException();
        }
        return scanner.nextLine();
    }

    static int lerInteiro(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = lerLinha(scanner).trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número inteiro.");
            }
        }
    }

    static void pausar(Scanner scanner) {
        System.out.println();
        System.out.println("Pressione ENTER para continuar...");
        lerLinha(scanner);
    }

    private static String lerTextoObrigatorio(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = lerLinha(scanner).trim();
            if (!entrada.isEmpty()) {
                return entrada;
            }
            System.out.println("O texto não pode ficar vazio.");
        }
    }

    private static String lerTextoOpcional(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        String entrada = lerLinha(scanner).trim();
        return entrada.isEmpty() ? null : entrada;
    }

    private static boolean lerConfirmacao(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = lerLinha(scanner).trim().toUpperCase();
            if (entrada.equals("S")) {
                return true;
            }
            if (entrada.equals("N")) {
                return false;
            }
            System.out.println("Responda S ou N.");
        }
    }

    private static LocalDate lerData(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = lerLinha(scanner).trim();
            try {
                return LocalDate.parse(entrada, FORMATO_DATA);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Use o formato DD/MM/AAAA.");
            }
        }
    }

    private static LocalDate lerDataAPartirDoInicio(Scanner scanner, Estagio estagio, String mensagem) {
        LocalDate data = lerData(scanner, mensagem);
        while (data.isBefore(estagio.getDataInicio())) {
            System.out.println("A data não pode ser anterior ao início do estágio ("
                    + formatarData(estagio.getDataInicio()) + ").");
            data = lerData(scanner, mensagem);
        }
        return data;
    }

    private static String formatarData(LocalDate data) {
        if (data == null) {
            return "-";
        }
        return data.format(FORMATO_DATA);
    }
}
