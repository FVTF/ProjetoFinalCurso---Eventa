package com.example.dao;

import com.example.model.OrcamentoServico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrcamentoServicoDAO {
    private final Connection conn;

    public OrcamentoServicoDAO(Connection conn) {
        this.conn = conn;
    }

    // Listar todos os serviços associados a um orçamento
    public List<OrcamentoServico> listarPorOrcamento(int idOrcamento) throws SQLException {
        List<OrcamentoServico> lista = new ArrayList<>();
        String sql = "SELECT * FROM Orcamento_Servico WHERE id_orcamento = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOrcamento);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrcamentoServico os = new OrcamentoServico();
                os.setIdOrcamento(rs.getInt("id_orcamento"));
                os.setIdServico(rs.getInt("id_servico"));
                os.setIdEspaco(rs.getInt("id_espaco"));
                os.setPrecoServico(rs.getDouble("preco_servico"));
                os.setPrecoEspaco(rs.getDouble("preco_espaco"));
                lista.add(os);
            }
        }
        return lista;
    }

    // Inserir um serviço individual (caso precises)
    public void inserir(OrcamentoServico os) throws SQLException {
        String sql = "INSERT INTO Orcamento_Servico (id_orcamento, id_servico, id_espaco, preco_servico, preco_espaco) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, os.getIdOrcamento());
            ps.setInt(2, os.getIdServico());
            ps.setInt(3, os.getIdEspaco());
            ps.setDouble(4, os.getPrecoServico());
            ps.setDouble(5, os.getPrecoEspaco());
            ps.executeUpdate();
        }
    }

    // Remover todos os serviços de um orçamento (ex: ao apagar/atualizar orçamento)
    public void removerPorOrcamento(int idOrcamento) throws SQLException {
        String sql = "DELETE FROM Orcamento_Servico WHERE id_orcamento = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOrcamento);
            ps.executeUpdate();
        }
    }
    
    // Opcional: remover um serviço específico de um orçamento
    public void removerServicoDeOrcamento(int idOrcamento, int idServico) throws SQLException {
        String sql = "DELETE FROM Orcamento_Servico WHERE id_orcamento = ? AND id_servico = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOrcamento);
            ps.setInt(2, idServico);
            ps.executeUpdate();
        }
    }
}
