package br.com.emanuelgabriel.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LivroDTO {

    private Long codigo;

    @NotBlank(message = "O campo TÍTULO não pode ser vazio")
    private String titulo;

    @NotBlank(message = "O campo AUTOR não pode ser vazio")
    private String autor;

    @NotBlank(message = "O campo ISBN não pode ser vazio")
    private String isbn;


}
