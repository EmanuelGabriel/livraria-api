package br.com.emanuelgabriel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Emprestimo implements Serializable {

    private Long codigo;
    private String cliente;
    private Livro livro;
    private LocalDate dataEmprestimo;
    private Boolean retorno;

}
