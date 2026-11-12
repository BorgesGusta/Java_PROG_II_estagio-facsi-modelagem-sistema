package br.edu.unifesspa.facsi.estagio;

import br.edu.unifesspa.facsi.estagio.enums.StatusEstagio;
import br.edu.unifesspa.facsi.estagio.model.Estagio;
import br.edu.unifesspa.facsi.estagio.model.RegistroFrequencia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
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
            System.out.println("4 - Consultar horas realizadas");
            System.out.println("5 - Encerrar estágio");
            System.out.println("0 - Voltar");
            System.out.println();

            opcao = lerInteiro(scanner, "Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    exibirEstagio(estagio);
                    pausar(scanner);
                    break;
                case 2:
                    iniciarEstagio(estagio);
                    pausar(scanner);
                    break;
                case 3:
                    registrarFrequencia(scanner, estagio);
                    pausar(scanner);
                    break;
                case 4:
                    consultarHoras(estagio);
                    pausar(scanner);
                    break;
                case 5:
                    encerrarEstagio(scanner, estagio);
                    pausar(scanner);
                    break;
                case 0:
                    break;
                default:
                    System.out.println();
                    System.out.println("Opção inválida.");
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

        if (estagio.getStatus() != StatusEstagio.ATIVO) {
            System.out.println("O estágio precisa estar ATIVO para registrar frequência.");
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

    private static void consultarHoras(Estagio estagio) {
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("        RESUMO DE FREQUÊNCIA");
        System.out.println("----------------------------------------");
        System.out.println("Aluno: " + estagio.getAluno().getNome());
        System.out.println("Registros: " + estagio.getRegistrosFrequencia().size());
        System.out.println("Horas realizadas: " + estagio.calcularHorasRealizadas());
        System.out.println("Limite diário válido: " + estagio.respeitaLimiteDiario());
        System.out.println("Limite semanal válido: " + estagio.respeitaLimiteSemanal());
        System.out.println("----------------------------------------");
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
        } else {
            System.out.println("O estágio ainda não atende aos requisitos de encerramento.");
            System.out.println();
            System.out.println("Horas realizadas: " + estagio.calcularHorasRealizadas());
            System.out.println("Relatório final: " + (estagio.getRelatorioFinal() != null ? "OK" : "PENDENTE"));
            System.out.println("Avaliação: " + (estagio.getAvaliacaoEstagio() != null ? "OK" : "PENDENTE"));
        }

        System.out.println();
        System.out.println("Status atual: " + estagio.getStatus());
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

    private static String formatarData(LocalDate data) {
        if (data == null) {
            return "-";
        }
        return data.format(FORMATO_DATA);
    }
}
