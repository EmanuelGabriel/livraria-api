package br.com.emanuelgabriel.api.resources;

import br.com.emanuelgabriel.api.exceptions.ApiErrors;
import br.com.emanuelgabriel.dtos.LivroDTO;
import br.com.emanuelgabriel.exception.RegraNegocioException;
import br.com.emanuelgabriel.model.Livro;
import br.com.emanuelgabriel.services.LivroService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Livro>> findAll() {
        List<Livro> livros = this.livroService.findAll();
        return livros != null ? ResponseEntity.ok().body(livros) : ResponseEntity.notFound().build();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErrors(bindingResult);
    }

    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(RegraNegocioException ex) {
        return new ApiErrors(ex);
    }

}
