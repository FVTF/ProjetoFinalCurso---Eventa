package com.example.model;

import java.sql.Timestamp;

public class Utilizador {
    private int idUser;
    private String nome;
    private String email;
    private String pwd;
    private Timestamp dataRegisto;
    private boolean ativo;
    private String telefone;
    private String morada;
    private String codPostal;

    public Utilizador() { }

    public int getIdUser() {
        return idUser;
    }
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Timestamp getDataRegisto() {
        return dataRegisto;
    }
    public void setDataRegisto(Timestamp dataRegisto) {
        this.dataRegisto = dataRegisto;
    }

    public boolean isAtivo() {
        return ativo;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getMorada() {
        return morada;
    }
    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getCodPostal() {
        return codPostal;
    }
    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }
}

