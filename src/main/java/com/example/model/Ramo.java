package com.example.model;

import java.io.Serializable;

public class Ramo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idRamo;
    private String descricao;

    public Ramo() { }

    public Ramo(int idRamo, String descricao) {
        this.idRamo = idRamo;
        this.descricao = descricao;
    }

    public int getIdRamo() {
        return idRamo;
    }

    public void setIdRamo(int idRamo) {
        this.idRamo = idRamo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Ramo{idRamo=" + idRamo + ", descricao='" + descricao + "'}";
    }
}
