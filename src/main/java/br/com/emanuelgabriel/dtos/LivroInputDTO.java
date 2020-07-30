package br.com.emanuelgabriel.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LivroInputDTO {

    private String titulo;
    private String autor;
    private String isbn;


}
