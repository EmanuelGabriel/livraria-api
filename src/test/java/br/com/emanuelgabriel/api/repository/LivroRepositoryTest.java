package br.com.emanuelgabriel.api.repository;

import br.com.emanuelgabriel.model.Livro;
import br.com.emanuelgabriel.repository.LivroRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
        Assertions.assertThat(existe).isTrue();
    }


    @Test
    @DisplayName("Deve retornar falso quando não existir um livro na base com ISBN informado")
    public void retornarFalsoSeNaoExistirIsbn() {

        // cenário
        String isbn = "123";

        // execução
        boolean existe = this.livroRepository.existsByIsbn(isbn);

        // verificação
        Assertions.assertThat(existe).isFalse();
    }


}
