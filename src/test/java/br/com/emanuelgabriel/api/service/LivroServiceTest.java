package br.com.emanuelgabriel.api.service;

import br.com.emanuelgabriel.exception.RegraNegocioException;
import br.com.emanuelgabriel.model.Livro;
import br.com.emanuelgabriel.repository.LivroRepository;
import br.com.emanuelgabriel.services.LivroService;
import br.com.emanuelgabriel.services.LivroServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LivroServiceTest {

    LivroService livroService;

    @MockBean
    LivroRepository livroRepository;

    @BeforeEach
    public void setUp() {
        this.livroService = new LivroServiceImpl(this.livroRepository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void salvarLivroTest() {

        // cenário
        Livro livro = criarNovoLivro();

        Mockito.when(this.livroRepository.save(livro))
                .thenReturn(Livro.builder().codigo(1L).titulo("123").autor("Fulano").isbn("27387").build());

        // execução
        Livro salvarLivro = this.livroService.salvar(livro);

        // verifiação
        Assertions.assertThat(salvarLivro.getCodigo()).isNotNull(); // não seja nulo
        Assertions.assertThat(salvarLivro.getTitulo()).isEqualTo("123");
        Assertions.assertThat(salvarLivro.getAutor()).isEqualTo("Fulano");
        Assertions.assertThat(salvarLivro.getIsbn()).isEqualTo("27387");

    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar salvar um livro com ISBN duplicado ou existente")
    public void lancarErroAoSalvarISBNExistente() {

        // cenário
        Livro livro = criarNovoLivro();
        Mockito.when(this.livroRepository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        // execução
        Throwable exception = Assertions.catchThrowable(() -> this.livroService.salvar(livro));

        // verificações
        Assertions.assertThat(exception)
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ISBN já cadastrado");

        Mockito.verify(this.livroRepository, Mockito.never()).save(livro);

    }


    private Livro criarNovoLivro() {
        return Livro.builder().codigo(1L).titulo("123").autor("Fulano").isbn("27387").build();
    }
}
