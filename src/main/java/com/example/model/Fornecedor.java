package com.example.model;

import java.io.Serializable;

public class Fornecedor implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idFornecedor;
    private int idUser;
    private int idRamo;
    private String ramoDescricao;
    private String codPostal; // NOVO

    public Fornecedor() { }

    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdRamo() {
        return idRamo;
    }

    public void setIdRamo(int idRamo) {
        this.idRamo = idRamo;
    }

    public String getRamoDescricao() {
        return ramoDescricao;
    }

    public void setRamoDescricao(String ramoDescricao) {
        this.ramoDescricao = ramoDescricao;
    }

    // NOVO getter/setter para código postal
    public String getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }

    @Override
    public String toString() {
        return "Fornecedor{" +
               "idFornecedor=" + idFornecedor +
               ", idUser=" + idUser +
               ", idRamo=" + idRamo +
               ", ramoDescricao='" + ramoDescricao + '\'' +
               ", codPostal='" + codPostal + '\'' +
               '}';
    }
}
