package br.edu.unifesspa.facsi.estagio.service;

import br.edu.unifesspa.facsi.estagio.dto.AlunoRequest;
import br.edu.unifesspa.facsi.estagio.dto.AlunoResponse;
import br.edu.unifesspa.facsi.estagio.model.Aluno;
import br.edu.unifesspa.facsi.estagio.repository.AlunoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public AlunoResponse cadastrar(AlunoRequest request) {
        Aluno aluno = new Aluno(
                null,
                request.nome(),
                request.cpf(),
                request.email(),
                request.telefone(),
                request.matricula(),
                request.curso(),
                request.situacaoAcademica()
        );

        Aluno alunoSalvo = alunoRepository.save(aluno);

        return converterParaResponse(alunoSalvo);
    }

    public List<AlunoResponse> listarTodos() {
        return alunoRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    private AlunoResponse converterParaResponse(Aluno aluno) {
        return new AlunoResponse(
                aluno.getId(),
                aluno.getNome(),
                aluno.getCpf(),
                aluno.getEmail(),
                aluno.getTelefone(),
                aluno.getMatricula(),
                aluno.getCurso(),
                aluno.getSituacaoAcademica()
        );
    }
}
