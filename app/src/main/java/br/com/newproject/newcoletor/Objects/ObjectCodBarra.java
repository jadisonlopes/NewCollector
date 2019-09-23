package br.com.newproject.newcoletor.Objects;

import java.util.ArrayList;

public class ObjectCodBarra extends ArrayList<ObjectCodBarra> {
    private String nome;
    private Float qtde;

    public ObjectCodBarra() {
    }

    public ObjectCodBarra(String nome, Float qtde) {
        this.nome = nome;
        this.qtde = qtde;
    }

    public String getNome() {
        return nome;
    }

    public Float getQtde() {
        return qtde;
    }
}
