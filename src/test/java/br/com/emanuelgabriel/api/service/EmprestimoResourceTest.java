package br.com.emanuelgabriel.api.service;


import br.com.emanuelgabriel.model.Emprestimo;
import br.com.emanuelgabriel.model.Livro;
import br.com.emanuelgabriel.repository.EmprestimoRepository;
import br.com.emanuelgabriel.service.EmprestimoService;
import br.com.emanuelgabriel.service.EmprestimoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class EmprestimoResourceTest {

    @MockBean
    EmprestimoRepository emprestimoRepository;

    EmprestimoService emprestimoService;

    @BeforeEach
    public void setUp() {
        this.emprestimoService = new EmprestimoServiceImpl(emprestimoRepository);
    }


    @Test
    @DisplayName("Deve salvar um empréstimo")
    public void criarEmprestimoTest() {

        Livro livro = Livro.builder().codigo(1L).build();
        String cliente = "Fulano";

        Emprestimo salvandoEmprestimo = Emprestimo.builder()
                .livro(livro)
                .cliente(cliente)
                .dataEmprestimo(LocalDate.now())
                .build();

        Emprestimo emprestimoSalvo = Emprestimo.builder()
                .codigo(1L)
                .dataEmprestimo(LocalDate.now())
                .cliente(cliente)
                .livro(livro)
                .build();

        when(this.emprestimoRepository.save(salvandoEmprestimo)).thenReturn(emprestimoSalvo);

        Emprestimo emprest = this.emprestimoService.salvar(salvandoEmprestimo);

        // verificação ou verificações
        assertThat(emprest.getCodigo()).isEqualTo(emprestimoSalvo.getCodigo());
        assertThat(emprest.getLivro().getCodigo()).isEqualTo(emprestimoSalvo.getLivro().getCodigo());
        assertThat(emprest.getCliente()).isEqualTo(emprestimoSalvo.getCliente());
        assertThat(emprest.getDataEmprestimo()).isEqualTo(emprestimoSalvo.getDataEmprestimo());


    }
}
