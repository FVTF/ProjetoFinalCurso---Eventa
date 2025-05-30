package com.example.dao;

import com.example.model.Orcamento;
import com.example.model.OrcamentoServico;

import java.sql.*;
import java.util.List;

public class OrcamentoDAO {
    private Connection connection;

    public OrcamentoDAO(Connection connection) {
        this.connection = connection;
    }

    // Insere orçamento e retorna o ID gerado
    public int inserir(Orcamento o) throws SQLException {
        String sql = "INSERT INTO Orcamento (id_cliente, id_evento, num_convidados, data, data_aprovacao, desconto, valor_orcamento) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, o.getIdCliente());
            stmt.setInt(2, o.getIdEvento());
            stmt.setInt(3, o.getNumConvidados());
            stmt.setTimestamp(4, new java.sql.Timestamp(o.getData().getTime()));
            if (o.getDataAprovacao() != null) {
                stmt.setTimestamp(5, new java.sql.Timestamp(o.getDataAprovacao().getTime()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }
            stmt.setFloat(6, o.getDesconto());
            stmt.setDouble(7, o.getValorOrcamento());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Erro ao criar orçamento.");
    }

    // Insere os serviços (não inserir idServico == -1!)
    public void inserirServicos(int idOrcamento, List<OrcamentoServico> servicos) throws SQLException {
        String sql = "INSERT INTO Orcamento_Servico (id_orcamento, id_servico, id_espaco, preco_servico, preco_espaco) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (OrcamentoServico os : servicos) {
                // Só insere serviços reais
                if (os.getIdServico() != -1) {
                    stmt.setInt(1, idOrcamento);
                    stmt.setInt(2, os.getIdServico());
                    stmt.setInt(3, os.getIdEspaco());
                    stmt.setDouble(4, os.getPrecoServico());
                    stmt.setDouble(5, os.getPrecoEspaco());
                    stmt.addBatch();
                }
            }
            stmt.executeBatch();
        }
    }
}
