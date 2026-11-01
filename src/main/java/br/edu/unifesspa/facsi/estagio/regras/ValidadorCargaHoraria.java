package br.edu.unifesspa.facsi.estagio.regras;

import br.edu.unifesspa.facsi.estagio.enums.ModalidadeEstagio;
import br.edu.unifesspa.facsi.estagio.enums.StatusEstagio;
import br.edu.unifesspa.facsi.estagio.model.Aluno;
import br.edu.unifesspa.facsi.estagio.model.Estagio;
import br.edu.unifesspa.facsi.estagio.model.RegistroFrequencia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ValidadorCargaHoraria {

    public static final int LIMITE_ESTAGIOS_NAO_OBRIGATORIOS_SIMULTANEOS = 2;

    private ValidadorCargaHoraria() {
    }

    public static boolean respeitaLimiteDiario(Aluno aluno, List<Estagio> estagios) {
        List<Estagio> estagiosAtivos = filtrarEstagiosAtivosDoAluno(aluno, estagios);
        for (Estagio estagio : estagiosAtivos) {
            List<Estagio> simultaneos = filtrarSimultaneos(estagio, estagiosAtivos);
            for (RegistroFrequencia registro : estagio.getRegistrosFrequencia()) {
                if (somarHorasNaData(simultaneos, registro.getData()) > Estagio.LIMITE_HORAS_DIARIAS) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean respeitaLimiteSemanal(Aluno aluno, List<Estagio> estagios) {
        List<Estagio> estagiosAtivos = filtrarEstagiosAtivosDoAluno(aluno, estagios);
        for (Estagio estagio : estagiosAtivos) {
            List<Estagio> simultaneos = filtrarSimultaneos(estagio, estagiosAtivos);
            for (RegistroFrequencia registro : estagio.getRegistrosFrequencia()) {
                if (somarHorasNaSemana(simultaneos, registro.getData()) > Estagio.LIMITE_HORAS_SEMANAIS) {
                    return false;
                }
            }
        }
        return true;
    }

    // O maior número de estágios vigentes ao mesmo tempo sempre ocorre no início de algum deles,
    // por isso basta contar quantos estão vigentes em cada data de início.
    public static boolean respeitaLimiteEstagiosNaoObrigatorios(Aluno aluno, List<Estagio> estagios) {
        List<Estagio> naoObrigatorios = new ArrayList<>();
        for (Estagio estagio : filtrarEstagiosAtivosDoAluno(aluno, estagios)) {
            if (estagio.getModalidade() == ModalidadeEstagio.NAO_OBRIGATORIO) {
                naoObrigatorios.add(estagio);
            }
        }

        for (Estagio estagio : naoObrigatorios) {
            int vigentesNoInicio = 0;
            for (Estagio outro : naoObrigatorios) {
                if (outro.estaVigenteEm(estagio.getInicioPeriodo())) {
                    vigentesNoInicio++;
                }
            }
            if (vigentesNoInicio > LIMITE_ESTAGIOS_NAO_OBRIGATORIOS_SIMULTANEOS) {
                return false;
            }
        }
        return true;
    }

    public static boolean respeitaRegrasDeSimultaneidade(Aluno aluno, List<Estagio> estagios) {
        return respeitaLimiteDiario(aluno, estagios)
                && respeitaLimiteSemanal(aluno, estagios)
                && respeitaLimiteEstagiosNaoObrigatorios(aluno, estagios);
    }

    private static List<Estagio> filtrarEstagiosAtivosDoAluno(Aluno aluno, List<Estagio> estagios) {
        List<Estagio> estagiosAtivos = new ArrayList<>();
        for (Estagio estagio : estagios) {
            if (estagio.getAluno() == aluno && estagio.getStatus() == StatusEstagio.ATIVO) {
                estagiosAtivos.add(estagio);
            }
        }
        return estagiosAtivos;
    }

    private static List<Estagio> filtrarSimultaneos(Estagio referencia, List<Estagio> estagios) {
        List<Estagio> simultaneos = new ArrayList<>();
        for (Estagio estagio : estagios) {
            if (estagio.possuiPeriodoSobreposto(referencia)) {
                simultaneos.add(estagio);
            }
        }
        return simultaneos;
    }

    private static int somarHorasNaData(List<Estagio> estagios, LocalDate data) {
        int total = 0;
        for (Estagio estagio : estagios) {
            total += estagio.calcularHorasNaData(data);
        }
        return total;
    }

    private static int somarHorasNaSemana(List<Estagio> estagios, LocalDate dataReferencia) {
        int total = 0;
        for (Estagio estagio : estagios) {
            total += estagio.calcularHorasNaSemana(dataReferencia);
        }
        return total;
    }
}
