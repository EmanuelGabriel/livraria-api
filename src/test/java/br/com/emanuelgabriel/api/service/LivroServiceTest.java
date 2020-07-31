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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(salvarLivro.getCodigo()).isNotNull(); // não seja nulo
        assertThat(salvarLivro.getTitulo()).isEqualTo("123");
        assertThat(salvarLivro.getAutor()).isEqualTo("Fulano");
        assertThat(salvarLivro.getIsbn()).isEqualTo("27387");

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
        assertThat(exception)
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ISBN já cadastrado");

        Mockito.verify(this.livroRepository, Mockito.never()).save(livro);

    }


    @Test
    @DisplayName("Deve obter um livro por código")
    public void obterLivroPorCodigo() {

        // Cenário
        Long codigoLivro = 1L;

        Livro livro = criarNovoLivro();
        livro.setCodigo(codigoLivro);
        Mockito.when(this.livroRepository.findById(codigoLivro)).thenReturn(Optional.of(livro));

        // execução
        Optional<Livro> buscarLivroPorCodigo = this.livroService.getByCodigo(codigoLivro);

        // verificações
        assertThat(buscarLivroPorCodigo.isPresent()).isTrue();
        assertThat(buscarLivroPorCodigo.get().getCodigo()).isEqualTo(codigoLivro);
        assertThat(buscarLivroPorCodigo.get().getTitulo()).isEqualTo(livro.getTitulo());
        assertThat(buscarLivroPorCodigo.get().getAutor()).isEqualTo(livro.getAutor());
        assertThat(buscarLivroPorCodigo.get().getIsbn()).isEqualTo(livro.getIsbn());


    }

    @Test
    @DisplayName("Deve retornar vazio ao obter um livro por código inexistente")
    public void livroNaoEncontradoPorCodigo() {

        // Cenário
        Long codigoLivro = 1L;

        Mockito.when(this.livroRepository.findById(codigoLivro)).thenReturn(Optional.empty());

        // execução
        Optional<Livro> buscarLivroPorCodigo = this.livroService.getByCodigo(codigoLivro);

        // verificação
        assertThat(buscarLivroPorCodigo.isPresent()).isFalse();

    }


    private Livro criarNovoLivro() {
        return Livro.builder().titulo("123").autor("Fulano").isbn("27387").build();
    }
}
