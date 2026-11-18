package br.edu.unifesspa.facsi.estagio.controller;

import br.edu.unifesspa.facsi.estagio.DadosExemplo;
import br.edu.unifesspa.facsi.estagio.dto.EstagioResumoResponse;
import br.edu.unifesspa.facsi.estagio.model.Estagio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estagios")
public class EstagioController {

    @GetMapping("/exemplo")
    public EstagioResumoResponse exemplo() {
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
