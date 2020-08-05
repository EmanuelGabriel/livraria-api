package br.com.emanuelgabriel.api.repository;

import br.com.emanuelgabriel.model.Livro;
import br.com.emanuelgabriel.repository.LivroRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LivroRepositoryTest {


    @Autowired
    TestEntityManager entityManager;

    @Autowired
    LivroRepository livroRepository;


    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com ISBN informado")
    public void retornarVerdadeiroSeExistirIsbn() {

        // cenário
        String isbn = "123";
        Livro livro = Livro.builder().titulo("Aventuras").autor("Fulano").isbn(isbn).build();

        this.entityManager.persist(livro);

        // execução

        boolean existe = this.livroRepository.existsByIsbn(isbn);

        // verificação
        assertThat(existe).isTrue();
    }


    @Test
    @DisplayName("Deve retornar falso quando não existir um livro na base com ISBN informado")
    public void retornarFalsoSeNaoExistirIsbn() {

        // cenário
        String isbn = "123";

        // execução
        boolean existe = this.livroRepository.existsByIsbn(isbn);

        // verificação
        assertThat(existe).isFalse();
    }


    @Test
    @DisplayName("Deve obter um livro por seu código")
    public void obterLivroPorCodigo() {

        // cenário
        Livro livro = criarNovoLivro("123");
        this.entityManager.persist(livro);

        // execução
        Optional<Livro> buscarPorCodigo = this.livroRepository.findById(livro.getCodigo());

        // verificação ou verificações
        assertThat(buscarPorCodigo.isPresent()).isTrue();

    }

    @Test
    @DisplayName("Deve remover um livro")
    public void removerLivroTest() {

        // cenário
        Livro livro = criarNovoLivro("123");
        this.entityManager.persist(livro);

        Livro buscarLivro = this.entityManager.find(Livro.class, livro.getCodigo());
        this.livroRepository.delete(buscarLivro);

        // verificação ou verificações=
        Livro removerLivro = this.entityManager.find(Livro.class, livro.getCodigo());
        assertThat(removerLivro).isNull();

    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void salvarLivroTest() {

        Livro livro = criarNovoLivro("123");

        Livro criarLivro = this.livroRepository.save(livro);

        assertThat(criarLivro.getCodigo()).isNotNull();

    }


    private Livro criarNovoLivro(String isbn) {
        return Livro.builder().titulo("Aventuras").autor("Fulano").isbn(isbn).build();
    }

}
