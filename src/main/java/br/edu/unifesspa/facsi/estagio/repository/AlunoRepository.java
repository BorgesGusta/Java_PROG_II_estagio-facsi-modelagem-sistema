package br.edu.unifesspa.facsi.estagio.repository;

import br.edu.unifesspa.facsi.estagio.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}
