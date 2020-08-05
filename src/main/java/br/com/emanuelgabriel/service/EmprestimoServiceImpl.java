package br.com.emanuelgabriel.service;

import br.com.emanuelgabriel.model.Emprestimo;
import br.com.emanuelgabriel.repository.EmprestimoRepository;
import org.springframework.stereotype.Service;

@Service
public class EmprestimoServiceImpl implements EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;

    public EmprestimoServiceImpl(EmprestimoRepository emprestimoRepository) {
        this.emprestimoRepository = emprestimoRepository;
    }


    @Override
    public Emprestimo salvar(Emprestimo emprestimo) {
        return null;
    }
}
