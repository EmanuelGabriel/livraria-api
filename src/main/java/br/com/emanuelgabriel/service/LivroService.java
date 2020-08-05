package br.com.emanuelgabriel.service;

import br.com.emanuelgabriel.model.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LivroService {

    Livro salvar(Livro livro);

    Page<Livro> findAll(Livro livroFilter, Pageable pageable);

    Optional<Livro> getByCodigo(Long codigo);

    Optional<Livro> findByIsbn(String isbn);

    Livro atualizar(Long codigo, Livro livro);

    Livro update(Livro livro);

    void remover(Livro livro);

}
