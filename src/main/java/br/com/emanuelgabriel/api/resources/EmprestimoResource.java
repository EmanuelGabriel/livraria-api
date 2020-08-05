package br.com.emanuelgabriel.api.resources;

import br.com.emanuelgabriel.api.exceptions.ApiErrors;
import br.com.emanuelgabriel.dtos.EmprestimoDto;
import br.com.emanuelgabriel.model.Emprestimo;
import br.com.emanuelgabriel.model.Livro;
import br.com.emanuelgabriel.service.EmprestimoService;
import br.com.emanuelgabriel.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/v1/emprestimos", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class EmprestimoResource {

    private final EmprestimoService emprestimoService;
    private final LivroService livroService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long criar(@RequestBody EmprestimoDto emprestimoDto) {
        Livro livro = this.livroService.findByIsbn(emprestimoDto.getIsbn())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Livro não encontrado para o ISBN informado"));
        Emprestimo emprestimo = Emprestimo.builder().livro(livro).cliente(emprestimoDto.getCliente()).dataEmprestimo(LocalDate.now()).build();
        emprestimo = this.emprestimoService.salvar(emprestimo);
        return emprestimo.getCodigo();
    }


}
