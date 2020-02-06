package br.com.cursoandroid.brabrank.model.entity;

public class Categoria {

    public Integer id;
    public String nome, tipo;

    @Override
    public String toString() {
        return nome;
    }

    public Categoria(Integer id, String nome, String tipo) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
    }

    public Categoria() {
    }
}
