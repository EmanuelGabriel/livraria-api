package br.com.emanuelgabriel.api.exceptions;

import br.com.emanuelgabriel.exception.RegraNegocioException;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class ApiErrors {

    private List<String> errors;

    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(erro -> this.errors.add(erro.getDefaultMessage()));
    }

    public ApiErrors(ResponseStatusException responseStatusException) {
        this.errors = Arrays.asList(responseStatusException.getReason());
    }

    public ApiErrors(RegraNegocioException ex) {
        this.errors = Arrays.asList(ex.getMessage());
    }

}
