package br.edu.unifesspa.facsi.estagio;

import java.util.Scanner;

public class Menu {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            exibirMenu();

            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            System.out.println();

            switch (opcao) {

                case 1:
                    System.out.println("Executando demonstração...\n");
                    Main.main(new String[]{});
                    break;

                case 2:
                    exibirInformacoes();
                    break;

                case 3:
                    System.out.println("Área de testes das regras.");
                    System.out.println("Depois vamos colocar os cenários aqui.");
                    break;

                case 0:
                    System.out.println("Encerrando o sistema...");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }

            if (opcao != 0) {
                System.out.println();
                System.out.println("Pressione ENTER para voltar ao menu...");
                scanner.nextLine();
            }

        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirMenu() {

        System.out.println();
        System.out.println("========================================");
        System.out.println("     SISTEMA DE ESTÁGIOS — FACSI");
        System.out.println("========================================");
        System.out.println();
        System.out.println("1 - Executar demonstração do sistema");
        System.out.println("2 - Ver informações do projeto");
        System.out.println("3 - Testar regras de estágio");
        System.out.println("0 - Sair");
        System.out.println();
    }

    private static void exibirInformacoes() {

        System.out.println("Sistema de Gestão de Estágios — FACSI");
        System.out.println();
        System.out.println("Projeto desenvolvido em Java.");
        System.out.println("Modela alunos, concedentes, vagas,");
        System.out.println("estágios, documentos e regras de negócio.");
    }
}