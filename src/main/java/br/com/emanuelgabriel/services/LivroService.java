package br.com.emanuelgabriel.services;

import br.com.emanuelgabriel.model.Livro;

import java.util.List;

public interface LivroService {

    Livro salvar(Livro livro);

    List<Livro> findAll();

}
