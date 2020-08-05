package br.com.emanuelgabriel.service;

import br.com.emanuelgabriel.exception.RecursoNaoEncontradoException;
import br.com.emanuelgabriel.exception.RegraNegocioException;
import br.com.emanuelgabriel.model.Livro;
import br.com.emanuelgabriel.repository.LivroRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.StringMatcher;
import static org.springframework.data.domain.ExampleMatcher.matching;

@Service
public class LivroServiceImpl implements LivroService {

    private final LivroRepository livroRepository;

    public LivroServiceImpl(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Override
    public Livro salvar(Livro livro) {
        if (this.livroRepository.existsByIsbn(livro.getIsbn())) {
            throw new RegraNegocioException("ISBN já cadastrado");
        }
        return this.livroRepository.save(livro);
    }

    @Override
    public Page<Livro> findAll(Livro livroFilter, Pageable pageable) {
        Example<Livro> example = Example
                .of(livroFilter, matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(StringMatcher.CONTAINING)
                );

        return this.livroRepository.findAll(example, pageable);
    }

    @Override
    public Optional<Livro> getByCodigo(Long codigo) {
        return this.livroRepository.findById(codigo);
    }

    @Override
    public Optional<Livro> findByIsbn(String isbn) {
        return this.livroRepository.findByIsbn(isbn);
    }

    @Override
    public Livro atualizar(Long codigo, Livro livro) {
        return this.livroRepository.findById(codigo)
                .map(book -> {
                    book.setAutor(livro.getAutor());
                    book.setTitulo(livro.getTitulo());
                    return this.livroRepository.save(book);
                }).orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado"));
    }

    @Override
    public Livro update(Livro livro) {
        if (livro == null || livro.getCodigo() == null) {
            throw new IllegalArgumentException("Este livro não pode ser removido");
        }
        return this.livroRepository.save(livro);
    }

    @Override
    public void remover(Livro livro) {
        if (livro == null || livro.getCodigo() == null) {
            throw new IllegalArgumentException("Este livro não pode ser removido");
        }
        this.livroRepository.delete(livro);
    }


}
