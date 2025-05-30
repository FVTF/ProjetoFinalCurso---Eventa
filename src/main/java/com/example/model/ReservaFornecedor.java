package com.example.model;

import java.io.Serializable;
import java.util.Date;

public class ReservaFornecedor implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idClienteServico;
    private String nomeCliente;
    private String evento;
    private String descricaoServico;
    private String descricaoEspaco;
    private double precoServico;
    private Date dataEvento;

    public int getIdClienteServico() { return idClienteServico; }
    public void setIdClienteServico(int idClienteServico) { this.idClienteServico = idClienteServico; }

    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

    public String getEvento() { return evento; }
    public void setEvento(String evento) { this.evento = evento; }

    public String getDescricaoServico() { return descricaoServico; }
    public void setDescricaoServico(String descricaoServico) { this.descricaoServico = descricaoServico; }

    public String getDescricaoEspaco() { return descricaoEspaco; }
    public void setDescricaoEspaco(String descricaoEspaco) { this.descricaoEspaco = descricaoEspaco; }

    public double getPrecoServico() { return precoServico; }
    public void setPrecoServico(double precoServico) { this.precoServico = precoServico; }

    public Date getDataEvento() { return dataEvento; }
    public void setDataEvento(Date dataEvento) { this.dataEvento = dataEvento; }
}
