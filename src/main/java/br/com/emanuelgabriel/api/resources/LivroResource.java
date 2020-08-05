package br.com.emanuelgabriel.api.resources;

import br.com.emanuelgabriel.api.exceptions.ApiErrors;
import br.com.emanuelgabriel.dtos.LivroDTO;
import br.com.emanuelgabriel.exception.RegraNegocioException;
import br.com.emanuelgabriel.model.Livro;
import br.com.emanuelgabriel.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/v1/livros", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class LivroResource {

    private final LivroService livroService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivroDTO criar(@Valid @RequestBody LivroDTO livroDTO) {

        Livro livro = this.modelMapper.map(livroDTO, Livro.class);
        livro = this.livroService.salvar(livro);
        return this.modelMapper.map(livro, LivroDTO.class);
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<LivroDTO> findAll(LivroDTO livroDTO, Pageable pageable) {
        Livro livro = this.modelMapper.map(livroDTO, Livro.class);
        Page<Livro> resultado = this.livroService.findAll(livro, pageable);
        List<LivroDTO> listaDto = resultado.getContent()
                .stream()
                .map(entity -> this.modelMapper.map(entity, LivroDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<LivroDTO>(listaDto, pageable, resultado.getTotalElements());
    }

    @GetMapping(value = "{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public LivroDTO getByCodigo(@PathVariable Long codigo) {
        return this.livroService
                .getByCodigo(codigo).map(livro -> this.modelMapper.map(livro, LivroDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @PutMapping(value = "{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public LivroDTO atualizar(@PathVariable Long codigo, @Valid @RequestBody Livro livro) {
        Livro updateLivro = this.livroService.getByCodigo(codigo).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        this.livroService.atualizar(codigo, livro);
        return this.modelMapper.map(livro, LivroDTO.class);
    }

    @DeleteMapping(value = "{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long codigo) {
        Livro livro = this.livroService.getByCodigo(codigo).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        this.livroService.remover(livro);
    }


}
