package br.edu.unifesspa.facsi.estagio.model;

import br.edu.unifesspa.facsi.estagio.enums.ModalidadeEstagio;
import br.edu.unifesspa.facsi.estagio.enums.OrigemEstagio;
import br.edu.unifesspa.facsi.estagio.enums.StatusEstagio;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Estagio {

    public static final int LIMITE_HORAS_DIARIAS = 6;
    public static final int LIMITE_HORAS_SEMANAIS = 30;

    private Long id;
    private ModalidadeEstagio modalidade;
    private OrigemEstagio origem;
    private LocalDate dataInicio;
    private LocalDate dataFimPrevista;
    private LocalDate dataFimReal;
    private Integer cargaHorariaDiaria;
    private Integer cargaHorariaSemanal;
    private Integer cargaHorariaTotalPrevista;
    private StatusEstagio status;

    // Relacionamentos obrigatórios
    private Aluno aluno;
    private Concedente concedente;

    // Relacionamentos opcionais
    private Convenio convenio;
    private Vaga vaga;
    private Candidatura candidatura;
    private TermoCompromisso termoCompromisso;
    private RelatorioFinal relatorioFinal;
    private AvaliacaoEstagio avaliacaoEstagio;

    // Papéis exercidos por pessoas no estágio
    private Pessoa supervisor;
    private Professor orientador;

    private List<PlanoAtividades> planosAtividades = new ArrayList<>();
    private List<ApoliceSeguro> apolices = new ArrayList<>();
    private List<RegistroFrequencia> registrosFrequencia = new ArrayList<>();

    public Estagio(Aluno aluno, Concedente concedente, ModalidadeEstagio modalidade, OrigemEstagio origem) {
        this.aluno = aluno;
        this.concedente = concedente;
        this.modalidade = modalidade;
        this.origem = origem;
    }

    public void adicionarPlanoAtividades(PlanoAtividades plano) {
        this.planosAtividades.add(plano);
    }

    public void adicionarApolice(ApoliceSeguro apolice) {
        this.apolices.add(apolice);
    }

    public void adicionarRegistroFrequencia(RegistroFrequencia registro) {
        this.registrosFrequencia.add(registro);
    }

    public int calcularHorasRealizadas() {
        int total = 0;
        for (RegistroFrequencia registro : registrosFrequencia) {
            if (registro.getQuantidadeHoras() != null) {
                total += registro.getQuantidadeHoras();
            }
        }
        return total;
    }

    public int calcularHorasNaData(LocalDate data) {
        int total = 0;
        for (RegistroFrequencia registro : registrosFrequencia) {
            if (registro.getData().equals(data) && registro.getQuantidadeHoras() != null) {
                total += registro.getQuantidadeHoras();
            }
        }
        return total;
    }

    public int calcularHorasNaSemana(LocalDate dataReferencia) {
        int total = 0;
        for (RegistroFrequencia registro : registrosFrequencia) {
            if (estaoNaMesmaSemana(registro.getData(), dataReferencia) && registro.getQuantidadeHoras() != null) {
                total += registro.getQuantidadeHoras();
            }
        }
        return total;
    }

    public boolean respeitaLimiteDiario() {
        for (RegistroFrequencia registro : registrosFrequencia) {
            if (calcularHorasNaData(registro.getData()) > LIMITE_HORAS_DIARIAS) {
                return false;
            }
        }
        return true;
    }

    public boolean respeitaLimiteSemanal() {
        for (RegistroFrequencia registro : registrosFrequencia) {
            if (calcularHorasNaSemana(registro.getData()) > LIMITE_HORAS_SEMANAIS) {
                return false;
            }
        }
        return true;
    }

    // Semana considerada de segunda a domingo (padrão ISO-8601).
    public static boolean estaoNaMesmaSemana(LocalDate data, LocalDate outraData) {
        return data.with(DayOfWeek.MONDAY).equals(outraData.with(DayOfWeek.MONDAY));
    }

    // Sem data definida, o período é tratado como aberto para que as regras de simultaneidade não sejam contornadas.
    public LocalDate getInicioPeriodo() {
        return dataInicio != null ? dataInicio : LocalDate.MIN;
    }

    public LocalDate getFimPeriodo() {
        if (dataFimReal != null) {
            return dataFimReal;
        }
        return dataFimPrevista != null ? dataFimPrevista : LocalDate.MAX;
    }

    public boolean estaVigenteEm(LocalDate data) {
        return !data.isBefore(getInicioPeriodo()) && !data.isAfter(getFimPeriodo());
    }

    public boolean possuiPeriodoSobreposto(Estagio outro) {
        return !getInicioPeriodo().isAfter(outro.getFimPeriodo())
                && !outro.getInicioPeriodo().isAfter(getFimPeriodo());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ModalidadeEstagio getModalidade() {
        return modalidade;
    }

    public void setModalidade(ModalidadeEstagio modalidade) {
        this.modalidade = modalidade;
    }

    public OrigemEstagio getOrigem() {
        return origem;
    }

    public void setOrigem(OrigemEstagio origem) {
        this.origem = origem;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFimPrevista() {
        return dataFimPrevista;
    }

    public void setDataFimPrevista(LocalDate dataFimPrevista) {
        this.dataFimPrevista = dataFimPrevista;
    }

    public LocalDate getDataFimReal() {
        return dataFimReal;
    }

    public void setDataFimReal(LocalDate dataFimReal) {
        this.dataFimReal = dataFimReal;
    }

    public Integer getCargaHorariaDiaria() {
        return cargaHorariaDiaria;
    }

    public void setCargaHorariaDiaria(Integer cargaHorariaDiaria) {
        this.cargaHorariaDiaria = cargaHorariaDiaria;
    }

    public Integer getCargaHorariaSemanal() {
        return cargaHorariaSemanal;
    }

    public void setCargaHorariaSemanal(Integer cargaHorariaSemanal) {
        this.cargaHorariaSemanal = cargaHorariaSemanal;
    }

    public Integer getCargaHorariaTotalPrevista() {
        return cargaHorariaTotalPrevista;
    }

    public void setCargaHorariaTotalPrevista(Integer cargaHorariaTotalPrevista) {
        this.cargaHorariaTotalPrevista = cargaHorariaTotalPrevista;
    }

    public StatusEstagio getStatus() {
        return status;
    }

    public void setStatus(StatusEstagio status) {
        this.status = status;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Concedente getConcedente() {
        return concedente;
    }

    public void setConcedente(Concedente concedente) {
        this.concedente = concedente;
    }

    public Convenio getConvenio() {
        return convenio;
    }

    public void setConvenio(Convenio convenio) {
        this.convenio = convenio;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
    }

    public Candidatura getCandidatura() {
        return candidatura;
    }

    public void setCandidatura(Candidatura candidatura) {
        this.candidatura = candidatura;
    }

    public TermoCompromisso getTermoCompromisso() {
        return termoCompromisso;
    }

    public void setTermoCompromisso(TermoCompromisso termoCompromisso) {
        this.termoCompromisso = termoCompromisso;
    }

    public RelatorioFinal getRelatorioFinal() {
        return relatorioFinal;
    }

    public void setRelatorioFinal(RelatorioFinal relatorioFinal) {
        this.relatorioFinal = relatorioFinal;
    }

    public AvaliacaoEstagio getAvaliacaoEstagio() {
        return avaliacaoEstagio;
    }

    public void setAvaliacaoEstagio(AvaliacaoEstagio avaliacaoEstagio) {
        this.avaliacaoEstagio = avaliacaoEstagio;
    }

    public Pessoa getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Pessoa supervisor) {
        this.supervisor = supervisor;
    }

    public Professor getOrientador() {
        return orientador;
    }

    public void setOrientador(Professor orientador) {
        this.orientador = orientador;
    }

    public List<PlanoAtividades> getPlanosAtividades() {
        return planosAtividades;
    }

    public void setPlanosAtividades(List<PlanoAtividades> planosAtividades) {
        this.planosAtividades = planosAtividades;
    }

    public List<ApoliceSeguro> getApolices() {
        return apolices;
    }

    public void setApolices(List<ApoliceSeguro> apolices) {
        this.apolices = apolices;
    }

    public List<RegistroFrequencia> getRegistrosFrequencia() {
        return registrosFrequencia;
    }
}
