package br.com.emanuelgabriel.repository;

import br.com.emanuelgabriel.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
}
