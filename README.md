# Sistema de Gestão de Estágios — FACSI

Projeto acadêmico desenvolvido para a disciplina de **Programação II**, com foco na modelagem e implementação, em Java, do domínio de gestão de estágios da FACSI/Unifesspa.

A versão atual implementa o fluxo principal do estágio em memória, regras de negócio, validações, menu interativo em terminal e testes automatizados. A prioridade desta etapa é consolidar corretamente o domínio antes de adicionar persistência, API ou interface web.

## Estado atual

A versão funcional em Java puro contempla:

- modelagem orientada a objetos do domínio de estágios;
- cadastro em memória de aluno, concedente, convênio, vaga e candidatura;
- formalização do estágio com termo, plano de atividades e seguro;
- estágio interno e externo;
- estágio obrigatório e não obrigatório;
- supervisor e professor orientador;
- controle de frequência;
- cálculo de carga horária diária, semanal e total;
- validação de estágios simultâneos;
- relatório final e avaliação;
- validação de início e encerramento;
- menu interativo no terminal;
- cenários de demonstração;
- testes automatizados com JUnit 5.

O fluxo principal do estágio é:

```text
EM_FORMALIZACAO
        |
        | iniciar()
        v
      ATIVO
        |
        | frequência
        | relatório final
        | avaliação
        v
    encerrar()
        |
        v
    ENCERRADO
```

O enum `StatusEstagio` também prevê o estado `CANCELADO`.

## Tecnologias

- Java 17
- Maven
- JUnit Jupiter 5.11.4
- aplicação de terminal, sem framework web nesta etapa

## Organização do projeto

```text
src/main/java/br/edu/unifesspa/facsi/estagio/
├── Main.java
├── Menu.java
├── MenuEstagio.java
├── DadosExemplo.java
├── EntradaEncerradaException.java
├── enums/
│   ├── AmbitoEstagio.java
│   ├── ModalidadeEstagio.java
│   ├── OrigemEstagio.java
│   └── StatusEstagio.java
├── model/
│   ├── Pessoa.java
│   ├── Aluno.java
│   ├── Professor.java
│   ├── Profissional.java
│   ├── Concedente.java
│   ├── Convenio.java
│   ├── Vaga.java
│   ├── Candidatura.java
│   ├── Estagio.java
│   ├── TermoCompromisso.java
│   ├── PlanoAtividades.java
│   ├── ApoliceSeguro.java
│   ├── RegistroFrequencia.java
│   ├── RelatorioFinal.java
│   └── AvaliacaoEstagio.java
└── regras/
    ├── ValidadorEstagio.java
    └── ValidadorCargaHoraria.java
```

A separação de pacotes é feita por responsabilidade:

- `model`: entidades e conceitos do domínio;
- `enums`: conjuntos fechados de valores utilizados pelo domínio;
- `regras`: validações e regras de negócio;
- `Menu` e `MenuEstagio`: interação com o usuário no terminal;
- `DadosExemplo`: montagem de cenários para estudo e demonstração.

## Modelo de pessoas

`Pessoa` é uma classe abstrata utilizada como base para:

```text
Pessoa
├── Aluno
├── Professor
└── Profissional
```

No estágio:

- o aluno é o discente que realiza o estágio;
- o supervisor é modelado como `Pessoa`;
- o orientador é modelado como `Professor`.

## Classe central: Estagio

`Estagio` concentra o estado e os comportamentos diretamente ligados ao estágio.

Entre suas responsabilidades estão:

- iniciar e encerrar o estágio;
- armazenar período, modalidade, origem, âmbito e status;
- relacionar aluno, concedente, documentos e responsáveis;
- registrar frequência;
- calcular horas realizadas;
- calcular horas por data e semana;
- verificar limites de carga horária;
- verificar sobreposição de períodos.

O status inicial é definido pelo próprio construtor como `EM_FORMALIZACAO`. O código não expõe um setter público para alterar diretamente o status, preservando as transições controladas pelo domínio.

## Regras para início

O método `Estagio.iniciar()` utiliza `ValidadorEstagio.podeIniciar(...)`.

Para iniciar, o estágio precisa atender às condições modeladas, entre elas:

- estar em `EM_FORMALIZACAO`;
- possuir âmbito definido;
- possuir período válido;
- possuir termo de compromisso;
- possuir ao menos um plano de atividades;
- possuir apólice vigente na data de início;
- possuir convênio vigente quando o estágio for externo;
- possuir bolsa e auxílio-transporte quando for não obrigatório.

Quando todas as regras são atendidas, o status passa para `ATIVO`.

## Frequência e carga horária

O sistema registra atividades somente para estágio ativo e dentro do período válido.

Os limites utilizados no modelo são:

- máximo de 6 horas por dia;
- máximo de 30 horas por semana;
- mínimo de 170 horas para encerramento de estágio obrigatório.

Uma frequência que excede o limite pode ser mantida como registro para permitir a identificação da irregularidade. Nesse caso, o sistema sinaliza o problema e o encerramento fica bloqueado enquanto houver violação diária ou semanal.

## Estágios simultâneos

`ValidadorCargaHoraria` verifica regras envolvendo mais de um estágio do mesmo aluno, incluindo:

- soma das horas diárias em estágios simultâneos;
- soma das horas semanais;
- sobreposição de períodos;
- limite de dois estágios não obrigatórios simultâneos.

## Regras para encerramento

O estágio só pode ser encerrado quando:

- estiver `ATIVO`;
- possuir registros de frequência;
- possuir relatório final;
- possuir avaliação;
- o resultado da avaliação for `APROVADO`;
- a carga mínima de 170 horas for cumprida quando o estágio for obrigatório;
- o limite diário de 6 horas estiver respeitado;
- o limite semanal de 30 horas estiver respeitado;
- a data real de encerramento for coerente com o início e com o último registro de frequência.

O método `ValidadorEstagio.listarPendenciasEncerramento(...)` informa os requisitos que ainda impedem o encerramento.

## Menu interativo

Depois da compilação, execute:

```bash
java -cp target/classes br.edu.unifesspa.facsi.estagio.Menu
```

O menu principal disponibiliza:

```text
1 - Estágios
2 - Executar demonstração completa
3 - Informações do projeto
0 - Sair
```

O submenu de estágios permite visualizar o estágio, iniciar, registrar e consultar frequência, consultar carga horária, registrar relatório e avaliação, verificar pendências e encerrar o estágio.

A opção de preenchimento automático de carga horária existe apenas para facilitar demonstrações e testes manuais do projeto.

## Build

Para compilar:

```bash
mvn compile
```

Para limpar, compilar e executar todos os testes:

```bash
mvn clean test
```

## Testes automatizados

A suíte atual possui **73 testes automatizados**, distribuídos em:

| Classe de teste | Testes |
| --- | ---: |
| `ValidadorEstagioTest` | 42 |
| `ValidadorCargaHorariaTest` | 9 |
| `EstagioTest` | 15 |
| `DadosExemploTest` | 4 |
| `MenuEstagioTest` | 2 |
| `MenuTest` | 1 |
| **Total** | **73** |

Os testes cobrem, entre outros cenários:

- início válido e inválido;
- convênio e apólice;
- estágio interno e externo;
- contrapartida do estágio não obrigatório;
- registro de frequência;
- períodos e sobreposição;
- limites diário e semanal;
- simultaneidade;
- carga mínima;
- relatório e avaliação;
- data de encerramento;
- bloqueio de encerramento com carga horária irregular.

Última validação desta versão: **73 testes executados, 0 falhas e 0 erros**, incluindo teste de integração do fluxo de terminal.

## Modelagem e UML

A modelagem, decisões e documentação detalhada são mantidas em repositório separado:

[Obsidian_PROG_II_Estagio-FACSI---Modelagem-e-Sistema](https://github.com/BorgesGusta/Obsidian_PROG_II_Estagio-FACSI---Modelagem-e-Sistema)

A documentação UML deve ser mantida sincronizada com o código à medida que o domínio evolui.

## Próxima fase: Spring e persistência

A implementação atual foi mantida propositalmente em Java puro para validar primeiro orientação a objetos, domínio e regras de negócio.

Depois do fechamento desta versão, a evolução planejada pode incluir:

1. Spring Boot;
2. organização em camadas de aplicação;
3. persistência com Spring Data JPA;
4. banco de dados;
5. serviços para os casos de uso;
6. API REST;
7. tratamento de erros HTTP;
8. testes de integração;
9. eventual interface web.

A adoção de Spring deve reaproveitar o domínio e as regras existentes, evitando reescrever a lógica que já foi validada.

## Situação da versão atual

Nesta etapa, o núcleo funcional em Java puro é considerado estável para refinamento. As próximas atividades antes da evolução para Spring são:

- revisão final entre código e UML;
- pequenos refinamentos de usabilidade;
- consolidação da versão estável;
- revisão e estudo dos conceitos de Programação II presentes no código.

## Conceitos de Programação II presentes no projeto

O projeto permite estudar na prática:

- classes e objetos;
- atributos e métodos;
- construtores;
- encapsulamento;
- herança;
- classe abstrata;
- associações;
- polimorfismo;
- enums;
- Collections;
- métodos e constantes `static`;
- exceções;
- separação de responsabilidades;
- regras de negócio;
- testes automatizados com JUnit.
