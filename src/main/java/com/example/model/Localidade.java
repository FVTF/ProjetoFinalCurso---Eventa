package com.example.model;

import java.io.Serializable;

public class Localidade implements Serializable {
    private static final long serialVersionUID = 1L;

    private String codPostal;
    private String nome;
    private int codDistrito;

    public Localidade() { }

    public Localidade(String codPostal, String nome, int codDistrito) {
        this.codPostal  = codPostal;
        this.nome       = nome;
        this.codDistrito = codDistrito;
    }

    public String getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCodDistrito() {
        return codDistrito;
    }

    public void setCodDistrito(int codDistrito) {
        this.codDistrito = codDistrito;
    }

    @Override
    public String toString() {
        return "Localidade{codPostal='" + codPostal + "', nome='" + nome + "', codDistrito=" + codDistrito + "}";
    }
}

