package br.com.cursoandroid.brabrank.model.entity;

public class Usuario {

    public String nome, cpf, email, senha, sexo;
    public Integer id;

    public Usuario(String nome, String cpf, String email, String senha, String sexo, Integer id) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.sexo = sexo;
        this.id = id;
    }

    public Usuario() {
    }
}
