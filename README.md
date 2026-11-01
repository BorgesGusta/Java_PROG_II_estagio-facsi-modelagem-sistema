# Estágio FACSI — Implementação Java

Repositório de implementação do sistema de gestão de estágios da FACSI para a disciplina de Programação II.

## Estado atual

A modelagem central da Sprint 01 já possui UML candidata a final e base normativa/documental suficiente para iniciar a implementação incremental do domínio.

## Etapa atual — Bloco 1

Implementado em Java puro:

- classe abstrata `Pessoa`;
- classe `Aluno`;
- classe `Professor`;
- herança entre Pessoa, Aluno e Professor;
- encapsulamento com atributos privados;
- construtores;
- getters e setters;
- enums `ModalidadeEstagio`, `OrigemEstagio` e `StatusEstagio`;
- `Main` simples para criação e inspeção de objetos.

## Estrutura

```
src/main/java/br/edu/unifesspa/facsi/estagio/
├── Main.java
├── enums/
│   ├── ModalidadeEstagio.java
│   ├── OrigemEstagio.java
│   └── StatusEstagio.java
└── model/
    ├── Pessoa.java
    ├── Aluno.java
    └── Professor.java
```

## Build

Projeto configurado com Maven e Java 17.

Para compilar:

```bash
mvn compile
```

## Fluxo de implementação

1. modelagem e UML;
2. Pessoas + enums;
3. Concedente + Convênio;
4. Vaga + Candidatura;
5. Estágio;
6. Termo + Plano + Seguro;
7. regras de negócio;
8. testes;
9. somente depois avaliar Spring, JPA e persistência.

## Repositório de modelagem

A documentação, regras, decisões e UML são mantidas separadamente em:

`Obsidian_PROG_II_Estagio-FACSI---Modelagem-e-Sistema`
