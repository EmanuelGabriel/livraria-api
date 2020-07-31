package br.com.emanuelgabriel.api.resource;

import br.com.emanuelgabriel.dtos.LivroDTO;
import br.com.emanuelgabriel.exception.RegraNegocioException;
import br.com.emanuelgabriel.model.Livro;
import br.com.emanuelgabriel.services.LivroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
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

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class LivroResourceTest {

    static String URL_LIVRO_API = "/v1/livros";

    @Autowired
    MockMvc mvc;

    @MockBean
    LivroService livroService;


    @Test
    @DisplayName("Deve criar um livro com sucesso")
    public void criarLivroTest() throws Exception {

        LivroDTO livroDTO = LivroDTO.builder().codigo(1L).titulo("Morro do Gritador").autor("Pedro Alves Cabral").isbn("2384283").build();
        Livro livro = Livro.builder().codigo(1L).titulo("Morro do Gritador").autor("Pedro Alves Cabral").isbn("2384283").build();

        BDDMockito.given(this.livroService.salvar(any(Livro.class))).willReturn(livro);
        String json = new ObjectMapper().writeValueAsString(livroDTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(URL_LIVRO_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        this.mvc
                .perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("codigo").isNotEmpty())
                .andExpect(jsonPath("titulo").value(livroDTO.getTitulo()))
                .andExpect(jsonPath("autor").value(livroDTO.getAutor()))
                .andExpect(jsonPath("isbn").value(livroDTO.getIsbn()));


    }

    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficiente para criação do livro")
    public void criarLivroInvalidoTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new LivroDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(URL_LIVRO_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        this.mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(3)));
    }


    @Test
    @DisplayName("Deve lançar erro ao tentar cadastrar um livro com ISBN já utilizado por outro")
    public void criarLivroComIsbnDuplicado() throws Exception {

        LivroDTO livroDTO = criarNovoLivro();

        String json = new ObjectMapper().writeValueAsString(livroDTO);
        String mensagemErro = "ISBN já cadastrado";
        BDDMockito.given(this.livroService.salvar(any(Livro.class))).willThrow(new RegraNegocioException(mensagemErro));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(URL_LIVRO_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        this.mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(mensagemErro));
    }


    @Test
    @DisplayName("Deve obter informação de um livro por seu código")
    public void getLivroPorCodigo() throws Exception {
        // cenário (given)
        Long codigoLivro = 1L;

        Livro livro = Livro.builder().codigo(codigoLivro).titulo("Morro do Gritador").autor("Pedro Alves Cabral").isbn("2384283").build();
        LivroDTO dto = criarNovoLivro();

        BDDMockito.given(this.livroService.getByCodigo(codigoLivro)).willReturn(Optional.of(livro));

        // execução (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(URL_LIVRO_API.concat("/" + codigoLivro))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("codigo").value(codigoLivro))
                .andExpect(jsonPath("titulo").value(dto.getTitulo()))
                .andExpect(jsonPath("autor").value(dto.getAutor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()));


    }

    @Test
    @DisplayName("Deve retornar um recurso não encontrado quando o livro não existir")
    public void livroNaoEncontradoTest() throws Exception {


        BDDMockito.given(this.livroService.getByCodigo(anyLong())).willReturn(Optional.empty());

        // execução (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(URL_LIVRO_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        this.mvc
                .perform(request)
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Deve remover um livro por seu código")
    public void removerLivroTest() throws Exception {

        BDDMockito.given(this.livroService.getByCodigo(anyLong())).willReturn(Optional.of(Livro.builder().codigo(1L).build()));

        // execução (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(URL_LIVRO_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar recurso não encontrado caso não exista o código do livro")
    public void removerNaoEncontradoOuInexistenteLivroTest() throws Exception {

        BDDMockito.given(this.livroService.getByCodigo(anyLong())).willReturn(Optional.empty());

        // execução (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(URL_LIVRO_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateLivroTest() throws Exception {

        // cenário (given)
        Long codigoLivro = 1L;
        String json = new ObjectMapper().writeValueAsString(criarNovoLivro());

        Livro atualizandoLivro = Livro.builder()
                .codigo(1L)
                .titulo("Novo Título")
                .autor("Fernando Henrique Moreira")
                .isbn("09876").build();

        BDDMockito.given(this.livroService.getByCodigo(codigoLivro))
                .willReturn(Optional.of(atualizandoLivro));

        Livro livroAtualizado = Livro.builder()
                .codigo(codigoLivro)
                .titulo("Morro do Gritador")
                .autor("Pedro Alves Cabral")
                .isbn("2384283").build();

        BDDMockito.given(this.livroService.atualizar(codigoLivro, atualizandoLivro)).willReturn(livroAtualizado);

        // execução (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(URL_LIVRO_API.concat("/" + 1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc
                .perform(request)
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("codigo").value(codigoLivro))
                .andExpect(jsonPath("titulo").value(criarNovoLivro().getTitulo()))
                .andExpect(jsonPath("autor").value(criarNovoLivro().getAutor()))
                .andExpect(jsonPath("isbn").value("2384283"));

    }

    @Test
    @DisplayName("Deve retornar 404 (not found) ao tentar atualizar livro de código inexistente")
    public void updateInexistenteLivroTest() throws Exception {

        // cenário (given)
        String json = new ObjectMapper().writeValueAsString(criarNovoLivro());

        BDDMockito.given(this.livroService.getByCodigo(anyLong()))
                .willReturn(Optional.empty());

        // execução (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(URL_LIVRO_API.concat("/" + 1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc
                .perform(request)
                .andExpect(status().isNotFound());

    }

    private LivroDTO criarNovoLivro() {
        return LivroDTO.builder().codigo(1L).titulo("Morro do Gritador").autor("Pedro Alves Cabral").isbn("2384283").build();
    }
}
