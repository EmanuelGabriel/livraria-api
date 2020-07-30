package br.com.emanuelgabriel.api.modelmapper;

import br.com.emanuelgabriel.dtos.LivroDTO;
import br.com.emanuelgabriel.dtos.LivroInputDTO;
import br.com.emanuelgabriel.model.Livro;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LivroModelMapper {

    private final ModelMapper modelMapper;

    public LivroDTO toModel(Livro livro) {
        return this.modelMapper.map(livro, LivroDTO.class);
    }

    public Livro toDto(LivroDTO livroDTO) {
        return this.modelMapper.map(livroDTO, Livro.class);
    }

    public Livro toDto(LivroInputDTO livroInputDTO) {
        return this.modelMapper.map(livroInputDTO, Livro.class);
    }

    public List<LivroDTO> toCollectionModel(List<Livro> livros) {
        return livros.stream().map(this::toModel).collect(Collectors.toList());
    }

}
