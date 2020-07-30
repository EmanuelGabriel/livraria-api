package br.com.emanuelgabriel.model;

import java.io.Serializable;

public class Cliente implements Serializable {

    private Long codigo;
    private String nome;
    private String cpf;

    public Cliente() {
    }

    public Cliente(Long codigo, String nome, String cpf) {
        this.codigo = codigo;
        this.nome = nome;
        this.cpf = cpf;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
