package br.com.emanuelgabriel.services;

import br.com.emanuelgabriel.exception.RecursoNaoEncontradoException;
import br.com.emanuelgabriel.exception.RegraNegocioException;
import br.com.emanuelgabriel.model.Livro;
import br.com.emanuelgabriel.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroServiceImpl implements LivroService {

    private LivroRepository livroRepository;

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
    public List<Livro> findAll() {
        return this.livroRepository.findAll();
    }

    @Override
    public Optional<Livro> getByCodigo(Long codigo) {
        return this.livroRepository.findById(codigo);
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
