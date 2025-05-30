package com.example.model;

import java.io.Serializable;

public class Espaco implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idEspaco;
    private String descricao;
    private int idFornecedor;
    private double preco;
    private String morada;
    private String codPostal;
    private double latitude;
    private double longitude;

    public Espaco() {}

    // Getters and setters
    public int getIdEspaco() { return idEspaco; }
    public void setIdEspaco(int idEspaco) { this.idEspaco = idEspaco; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getIdFornecedor() { return idFornecedor; }
    public void setIdFornecedor(int idFornecedor) { this.idFornecedor = idFornecedor; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public String getMorada() { return morada; }
    public void setMorada(String morada) { this.morada = morada; }

    public String getCodPostal() { return codPostal; }
    public void setCodPostal(String codPostal) { this.codPostal = codPostal; }
    
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}
