package br.com.emanuelgabriel.api.resource;


import br.com.emanuelgabriel.api.resources.EmprestimoResource;
import br.com.emanuelgabriel.dtos.EmprestimoDto;
import br.com.emanuelgabriel.exception.RegraNegocioException;
import br.com.emanuelgabriel.model.Emprestimo;
import br.com.emanuelgabriel.model.Livro;
import br.com.emanuelgabriel.service.EmprestimoService;
import br.com.emanuelgabriel.service.LivroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {EmprestimoResource.class})
@AutoConfigureMockMvc
public class EmprestimoResourceTest {


    static String URL_EMPRESTIMO_API = "/v1/emprestimos";

    @Autowired
    MockMvc mvc;

    @MockBean
    private LivroService livroService;

    @MockBean
    private EmprestimoService emprestimoService;

    @Test
    @DisplayName("Deve criar um empréstimo de um livro")
    public void criarEmprestimoTest() throws Exception {

        // cenário
        String isbnLivro = "123";

        EmprestimoDto dto = EmprestimoDto.builder().isbn(isbnLivro).cliente("Fulano").build();

        String json = new ObjectMapper().writeValueAsString(dto);

        Livro livro = Livro.builder().codigo(1L).isbn(isbnLivro).build();

        BDDMockito.given(this.livroService.findByIsbn(isbnLivro))
                .willReturn(Optional.of(livro));

        Emprestimo emprestimo = Emprestimo.builder().codigo(1L).cliente("Fulano").livro(livro).dataEmprestimo(LocalDate.now()).build();

        BDDMockito.given(this.emprestimoService.salvar(Mockito.any(Emprestimo.class))).willReturn(emprestimo);

        // execução
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(URL_EMPRESTIMO_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        // verificação ou verificações
        this.mvc
                .perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));


    }

    @Test
    @DisplayName("Deve retornar erro ao tentar fazer empréstimo de um livro inexistente")
    public void emprestimoIsbnInvalidoTest() throws Exception {

        EmprestimoDto dto = EmprestimoDto.builder().isbn("123").cliente("Fulano").build();

        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(this.livroService.findByIsbn("123")).willReturn(Optional.empty());

        // execução
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(URL_EMPRESTIMO_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        // verificação ou verificações
        this.mvc
                .perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Livro não encontrado para o ISBN informado"));

    }

    @Test
    @DisplayName("Deve retornar erro ao tentar fazer empréstimo de um livro que já está emprestado")
    public void emprestimoLivroErrorCriarTest() throws Exception {

        EmprestimoDto dto = EmprestimoDto.builder().isbn("123").cliente("Fulano").build();

        String json = new ObjectMapper().writeValueAsString(dto);

        Livro livro = Livro.builder().codigo(1L).isbn("123").build();

        BDDMockito.given(this.livroService.findByIsbn("123"))
                .willReturn(Optional.of(livro));

        BDDMockito.given(this.emprestimoService.salvar(Mockito.any(Emprestimo.class)))
                .willThrow(new RegraNegocioException("Livro já emprestado"));

        // execução
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(URL_EMPRESTIMO_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        // verificação ou verificações
        this.mvc
                .perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Livro já emprestado"));
    }


}
