package br.com.cursoandroid.brabrank.model.entity;

import java.util.Date;

public class Lancamento {

    public Integer codigo;
    public String tipo, descricao;
    public Float valor;
    public Date dataLancamento;
    public Categoria categoria;

    public Lancamento(Integer codigo, String tipo, String descricao, Float valor, Date dataLancamento, Categoria categoria) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.descricao = descricao;
        this.valor = valor;
        this.dataLancamento = dataLancamento;
        this.categoria = categoria;
    }

    public Lancamento() {
    }
}
