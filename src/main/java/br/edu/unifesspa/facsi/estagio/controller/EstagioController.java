package br.edu.unifesspa.facsi.estagio.controller;

import br.edu.unifesspa.facsi.estagio.dto.EstagioResumoResponse;
import br.edu.unifesspa.facsi.estagio.service.EstagioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estagios")
public class EstagioController {

    private final EstagioService estagioService;

    public EstagioController(EstagioService estagioService) {
        this.estagioService = estagioService;
    }

    @GetMapping("/exemplo")
    public EstagioResumoResponse exemplo() {
        return estagioService.buscarExemplo();
    }
}
