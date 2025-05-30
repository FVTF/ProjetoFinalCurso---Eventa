package com.example.model;

import java.io.Serializable;

public class Servico implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idServico;
    private String descricao;
    private int idRamo; // NOVO

    public Servico() {}

    public int getIdServico() { return idServico; }
    public void setIdServico(int idServico) { this.idServico = idServico; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getIdRamo() { return idRamo; }
    public void setIdRamo(int idRamo) { this.idRamo = idRamo; }
}
