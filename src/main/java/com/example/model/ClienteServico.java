package com.example.model;

import java.io.Serializable;
import java.util.Date;

public class ClienteServico implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idClienteServico;
    private Integer idServicoFornecedor;   // Pode ser null
    private int idCliente;
    private Date data;
    private int idEvento;
    private Integer idEspaco;              // Pode ser null
    private double precoServico;
    private double precoEspaco;
    private String estado;
    private int idOrcamento; // Orçamento global a que esta reserva pertence

    // Auxiliares para Dashboard
    private String nomeCliente;
    private String descricaoServico;

    // === GETTERS E SETTERS ===

    public int getIdClienteServico() { return idClienteServico; }
    public void setIdClienteServico(int idClienteServico) { this.idClienteServico = idClienteServico; }

    public Integer getIdServicoFornecedor() { return idServicoFornecedor; }
    public void setIdServicoFornecedor(Integer idServicoFornecedor) { this.idServicoFornecedor = idServicoFornecedor; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public int getIdEvento() { return idEvento; }
    public void setIdEvento(int idEvento) { this.idEvento = idEvento; }

    public Integer getIdEspaco() { return idEspaco; }
    public void setIdEspaco(Integer idEspaco) { this.idEspaco = idEspaco; }

    public double getPrecoServico() { return precoServico; }
    public void setPrecoServico(double precoServico) { this.precoServico = precoServico; }

    public double getPrecoEspaco() { return precoEspaco; }
    public void setPrecoEspaco(double precoEspaco) { this.precoEspaco = precoEspaco; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getIdOrcamento() { return idOrcamento; }
    public void setIdOrcamento(int idOrcamento) { this.idOrcamento = idOrcamento; }

    // --- AUXILIARES PARA DASHBOARD ---
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

    public String getDescricaoServico() { return descricaoServico; }
    public void setDescricaoServico(String descricaoServico) { this.descricaoServico = descricaoServico; }
}
