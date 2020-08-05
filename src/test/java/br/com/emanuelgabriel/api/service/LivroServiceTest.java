package br.com.emanuelgabriel.api.service;

import br.com.emanuelgabriel.exception.RegraNegocioException;
import br.com.emanuelgabriel.model.Livro;
import br.com.emanuelgabriel.repository.LivroRepository;
import br.com.emanuelgabriel.service.LivroService;
import br.com.emanuelgabriel.service.LivroServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    @DisplayName("Deve remover um livro")
    public void removerLivroTest() {
        Livro livro = Livro.builder().codigo(1L).build();

        // execução
        assertDoesNotThrow(() -> this.livroService.remover(livro));

        // verficações
        Mockito.verify(this.livroRepository, Mockito.times(1)).delete(livro);

    }

    @Test
    @DisplayName("Deve lançar um erro ao tentar remover um livro inexistente")
    public void removerLivroInexistenteTest() {

        // cenário
        Livro livro = new Livro();

        // execução
        assertThrows(IllegalArgumentException.class, () -> this.livroService.remover(livro));

        // verificação
        Mockito.verify(this.livroRepository, Mockito.never()).delete(livro);


    }

    @Test
    @DisplayName("Deve lançar um erro ao tentar atualizar um livro inexistente")
    public void updateInexistenteLivroTest() {

        // cenário
        Livro livro = new Livro();

        // execução
        assertThrows(IllegalArgumentException.class, () -> this.livroService.update(livro));

        // verificação
        Mockito.verify(this.livroRepository, Mockito.never()).save(livro);


    }


    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateLivroTest() {

        // cenário
        Long codigoLivro = 1L;
        Livro livroAtualizando = Livro.builder().codigo(codigoLivro).build();

        // execução
        Livro livroAtualizado = criarNovoLivro();
        livroAtualizado.setCodigo(codigoLivro);

        // verificações
        Mockito.when(this.livroRepository.save(livroAtualizando)).thenReturn(livroAtualizado);

        // execução
        Livro livro = this.livroService.update(livroAtualizando);

        // verificações
        assertThat(livro.getCodigo()).isEqualTo(livroAtualizado.getCodigo());
        assertThat(livro.getTitulo()).isEqualTo(livroAtualizado.getTitulo());
        assertThat(livro.getIsbn()).isEqualTo(livroAtualizado.getIsbn());
        assertThat(livro.getAutor()).isEqualTo(livroAtualizado.getAutor());

    }

    @Test
    @DisplayName("Deve filtrar livros por título e autor com paginação")
    public void findLivroTest() {

        // cenário
        Livro livro = criarNovoLivro();
        livro.setCodigo(1L);

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Livro> lista = Arrays.asList(livro);

        Page<Livro> page = new PageImpl<>(lista, pageRequest, 1);

        Mockito.when(this.livroRepository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(page);

        // execução
        Page<Livro> resultado = this.livroService.findAll(livro, pageRequest);

        // vericação ou verificações
        assertThat(resultado.getTotalElements()).isEqualTo(1);
        assertThat(resultado.getContent()).isEqualTo(lista);
        assertThat(resultado.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(resultado.getPageable().getPageSize()).isEqualTo(10);

    }


    private Livro criarNovoLivro() {
        return Livro.builder().titulo("123").autor("Fulano").isbn("27387").build();
    }
}
