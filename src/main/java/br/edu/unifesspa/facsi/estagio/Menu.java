package br.edu.unifesspa.facsi.estagio;

import br.edu.unifesspa.facsi.estagio.model.Estagio;

import java.util.Scanner;

public class Menu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Estagio estagio = DadosExemplo.criarEstagioFormalizado();

        int opcao;
        do {
            System.out.println();
            System.out.println("========================================");
            System.out.println("     SISTEMA DE ESTÁGIOS — FACSI");
            System.out.println("========================================");
            System.out.println();
            System.out.println("1 - Estágios");
            System.out.println("2 - Executar demonstração completa");
            System.out.println("3 - Informações do projeto");
            System.out.println("0 - Sair");
            System.out.println();

            opcao = MenuEstagio.lerInteiro(scanner, "Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    MenuEstagio.executar(scanner, estagio);
                    break;
                case 2:
                    System.out.println();
                    Main.main(new String[]{});
                    MenuEstagio.pausar(scanner);
                    break;
                case 3:
                    exibirInformacoes();
                    MenuEstagio.pausar(scanner);
                    break;
                case 0:
                    System.out.println();
                    System.out.println("Encerrando o sistema...");
                    break;
                default:
                    System.out.println();
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirInformacoes() {
        System.out.println();
        System.out.println("Sistema de Gestão de Estágios — FACSI");
        System.out.println();
        System.out.println("Projeto acadêmico desenvolvido em Java.");
        System.out.println("Modela alunos, concedentes, vagas, candidaturas e estágios.");
        System.out.println("Também aplica regras de formalização, frequência, carga horária e encerramento.");
    }
}
