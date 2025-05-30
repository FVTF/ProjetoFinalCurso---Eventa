package com.example.model;

public class OrcamentoServico {
    private int idOrcamento;
    private int idServico;
    private int idEspaco;
    private double precoServico;
    private double precoEspaco;
    private String nomeServico; 
    private int idFornecedor;

    public int getIdOrcamento() {
        return idOrcamento;
    }
    public void setIdOrcamento(int idOrcamento) { this.idOrcamento = idOrcamento; }

    public int getIdServico() {
        return idServico;
    }
    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public int getIdEspaco() {
        return idEspaco;
    }
    public void setIdEspaco(int idEspaco) {
        this.idEspaco = idEspaco;
    }

    public double getPrecoServico() {
        return precoServico;
    }
    public void setPrecoServico(double precoServico) {
        this.precoServico = precoServico;
    }

    public double getPrecoEspaco() {
        return precoEspaco;
    }
    public void setPrecoEspaco(double precoEspaco) {
        this.precoEspaco = precoEspaco;
    }

    public String getNomeServico() {
        return nomeServico;
    }
    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }

    public int getIdFornecedor() { return idFornecedor; }
    public void setIdFornecedor(int idFornecedor) { this.idFornecedor = idFornecedor; }
}
