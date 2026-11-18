package br.edu.unifesspa.facsi.estagio.service;

import br.edu.unifesspa.facsi.estagio.DadosExemplo;
import br.edu.unifesspa.facsi.estagio.dto.EstagioResumoResponse;
import br.edu.unifesspa.facsi.estagio.model.Estagio;
import org.springframework.stereotype.Service;

@Service
public class EstagioService {

    public EstagioResumoResponse buscarExemplo() {
        Estagio estagio = DadosExemplo.criarEstagioFormalizado();

        return new EstagioResumoResponse(
                estagio.getId(),
                estagio.getAluno().getNome(),
                estagio.getConcedente().getNome(),
                estagio.getModalidade().name(),
                estagio.getOrigem().name(),
                estagio.getAmbito().name(),
                estagio.getStatus().name()
        );
    }
}
