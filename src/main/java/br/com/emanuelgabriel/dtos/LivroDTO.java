package br.com.emanuelgabriel.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LivroDTO {

    private Long codigo;

    @NotEmpty(message = "O campo TÍTULO não pode ser vazio")
    private String titulo;

    @NotEmpty(message = "O campo AUTOR não pode ser vazio")
    private String autor;

    @NotEmpty(message = "O campo ISBN não pode ser vazio")
    private String isbn;


}
