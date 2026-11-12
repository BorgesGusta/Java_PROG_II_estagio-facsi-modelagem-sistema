package br.edu.unifesspa.facsi.estagio;

public class EntradaEncerradaException extends RuntimeException {

    public EntradaEncerradaException() {
        super("A entrada de dados foi encerrada.");
    }
}
