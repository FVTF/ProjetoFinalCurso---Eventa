package com.example.model;

import java.io.Serializable;

public class ServicoFornecedor implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idServicoFornecedor;
    private int idServico;        // FK para tabela Servico
    private int idFornecedor;     // FK para tabela Fornecedor
    private double preco;
    private String descricaoServico; // Para exibir na lista
    private String imagemUrl;
    private Integer idEspaco;

    public ServicoFornecedor() {}

    public int getIdServicoFornecedor() { return idServicoFornecedor; }
    public void setIdServicoFornecedor(int idServicoFornecedor) { this.idServicoFornecedor = idServicoFornecedor; }

    public int getIdServico() { return idServico; }
    public void setIdServico(int idServico) { this.idServico = idServico; }

    public int getIdFornecedor() { return idFornecedor; }
    public void setIdFornecedor(int idFornecedor) { this.idFornecedor = idFornecedor; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public String getDescricaoServico() { return descricaoServico; }
    public void setDescricaoServico(String descricaoServico) { this.descricaoServico = descricaoServico; }
    
    public String getImagemUrl() {return imagemUrl;}
    public void setImagemUrl(String imagemUrl) {this.imagemUrl = imagemUrl;}
 
    public Integer getIdEspaco() {
        return idEspaco;
    }

    public void setIdEspaco(Integer idEspaco) {
        this.idEspaco = idEspaco;
    }
}
