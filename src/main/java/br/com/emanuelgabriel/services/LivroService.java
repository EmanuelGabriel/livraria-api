package br.com.emanuelgabriel.services;

import br.com.emanuelgabriel.model.Livro;

import java.util.List;
import java.util.Optional;

public interface LivroService {

    Livro salvar(Livro livro);

    List<Livro> findAll();

    Optional<Livro> getByCodigo(Long codigo);

    Livro atualizar(Long codigo, Livro livro);

    Livro update(Livro livro);

    void remover(Livro livro);

}
