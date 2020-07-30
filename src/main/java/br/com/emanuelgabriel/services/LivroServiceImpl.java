package br.com.emanuelgabriel.services;

import br.com.emanuelgabriel.exception.RegraNegocioException;
import br.com.emanuelgabriel.model.Livro;
import br.com.emanuelgabriel.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
