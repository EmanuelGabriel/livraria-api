package br.com.emanuelgabriel.api.resources;

import br.com.emanuelgabriel.model.Cliente;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/clientes")
public class MeuResource {


    @PostMapping(MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Cliente> criar(@RequestBody Cliente cliente) {
        Cliente cl1 = new Cliente(1L, "Francisco Castro", "827.237.287-23");
        return new ResponseEntity<>(cl1, HttpStatus.CREATED);
    }


    @GetMapping(value = "{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cliente> obterCliente(@PathVariable Long codigo) {
        Cliente cliente = new Cliente();
        cliente.setCodigo(1L);
        cliente.setNome("Jonas da Silva Moreira");
        cliente.setCpf("273.283.277-99");
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    @PutMapping(value = "{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Cliente atualizar(@PathVariable Long codigo, @RequestBody Cliente cliente) {
        return cliente;
    }


    @DeleteMapping(value = "{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long codigo) {

    }

}
