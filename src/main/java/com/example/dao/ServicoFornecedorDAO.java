package com.example.dao;

import com.example.model.ServicoFornecedor;
import com.example.model.ReservaFornecedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicoFornecedorDAO {
    private final Connection conn;

    public ServicoFornecedorDAO(Connection conn) {
        this.conn = conn;
    }

    // Lista todos os serviços do fornecedor
    public List<ServicoFornecedor> listarPorFornecedor(int idFornecedor) throws SQLException {
        List<ServicoFornecedor> lista = new ArrayList<>();
        String sql = "SELECT sf.id_servico_fornecedor, sf.id_servico, sf.preco, s.descricao, sf.imagem_url " +
                     "FROM Servico_Fornecedor sf " +
                     "JOIN Servico s ON sf.id_servico = s.id_servico " +
                     "WHERE sf.id_fornecedor = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFornecedor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ServicoFornecedor sf = new ServicoFornecedor();
                    sf.setIdServicoFornecedor(rs.getInt("id_servico_fornecedor"));
                    sf.setIdServico(rs.getInt("id_servico"));
                    sf.setPreco(rs.getDouble("preco"));
                    sf.setDescricaoServico(rs.getString("descricao"));
                    sf.setImagemUrl(rs.getString("imagem_url"));
                   // sf.setIdEspaco(rs.getInt("id_espaco"));
                    lista.add(sf);
                }
            }
        }
        return lista;
    }

    // Lista serviços por ramo
    public List<ServicoFornecedor> listarPorRamo(int idRamo) throws SQLException {
        List<ServicoFornecedor> lista = new ArrayList<>();
        String sql = "SELECT sf.id_servico_fornecedor, sf.preco, sf.id_fornecedor, " +
                     "s.descricao, s.id_servico, sf.imagem_url " +
                     "FROM Servico_Fornecedor sf " +
                     "JOIN Servico s ON sf.id_servico = s.id_servico " +
                     "WHERE s.id_ramo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRamo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ServicoFornecedor sf = new ServicoFornecedor();
                    sf.setIdServicoFornecedor(rs.getInt("id_servico_fornecedor"));
                    sf.setIdServico(rs.getInt("id_servico"));
                    sf.setIdFornecedor(rs.getInt("id_fornecedor"));
                    sf.setPreco(rs.getDouble("preco"));
                    sf.setDescricaoServico(rs.getString("descricao"));
                    sf.setImagemUrl(rs.getString("imagem_url"));
                    lista.add(sf);
                }
            }
        }
        return lista;
    }

    // Cria serviço associado ao fornecedor
    public void create(ServicoFornecedor sf) throws SQLException {
        String sql = "INSERT INTO Servico_Fornecedor (id_servico, id_fornecedor, preco, imagem_url) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sf.getIdServico());
            ps.setInt(2, sf.getIdFornecedor());
            ps.setDouble(3, sf.getPreco());
            ps.setString(4, sf.getImagemUrl());
            ps.executeUpdate();
        }
    }

    // Busca serviço do fornecedor pelo ID
    public ServicoFornecedor findById(int id) throws SQLException {
        String sql = "SELECT sf.id_servico_fornecedor, sf.id_servico, sf.id_fornecedor, sf.preco, " +
                     "s.descricao, sf.imagem_url " +
                     "FROM Servico_Fornecedor sf " +
                     "JOIN Servico s ON sf.id_servico = s.id_servico " +
                     "WHERE sf.id_servico_fornecedor = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ServicoFornecedor sf = new ServicoFornecedor();
                    sf.setIdServicoFornecedor(rs.getInt("id_servico_fornecedor"));
                    sf.setIdServico(rs.getInt("id_servico"));
                    sf.setIdFornecedor(rs.getInt("id_fornecedor"));
                    sf.setPreco(rs.getDouble("preco"));
                    sf.setDescricaoServico(rs.getString("descricao"));
                    sf.setImagemUrl(rs.getString("imagem_url"));
                    return sf;
                }
            }
        }
        return null;
    }

    // Atualiza serviço (se quiseres permitir editar imagem_url)
    public void update(ServicoFornecedor sf) throws SQLException {
        String sql = "UPDATE Servico_Fornecedor SET id_servico=?, preco=?, imagem_url=? WHERE id_servico_fornecedor=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sf.getIdServico());
            ps.setDouble(2, sf.getPreco());
            ps.setString(3, sf.getImagemUrl());
            ps.setInt(4, sf.getIdServicoFornecedor());
            ps.executeUpdate();
        }
    }

    // Apaga serviço do fornecedor
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Servico_Fornecedor WHERE id_servico_fornecedor = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Lista reservas por fornecedor (mantém igual ao que já tinhas)
    public List<ReservaFornecedor> listarReservasPorFornecedor(int idFornecedor) throws SQLException {
        List<ReservaFornecedor> lista = new ArrayList<>();
        String sql = "SELECT cs.id_cliente_servico, cs.data, " +
                "u.nome AS nome_cliente, " +
                "e.descricao AS evento, " +
                "s.descricao AS descricao_servico, " +
                "cs.preco_servico, " +
                "esp.descricao AS descricao_espaco " +
                "FROM Cliente_Servico cs " +
                "JOIN Cliente c ON cs.id_cliente = c.id_cliente " +
                "JOIN Utilizador u ON c.id_user = u.id_user " +
                "JOIN Evento e ON cs.id_evento = e.id_evento " +
                "JOIN Servico_Fornecedor sf ON cs.id_servico_fornecedor = sf.id_servico_fornecedor " +
                "JOIN Servico s ON sf.id_servico = s.id_servico " +
                "LEFT JOIN Espaco esp ON cs.id_espaco = esp.id_espaco " +
                "WHERE sf.id_fornecedor = ? " +
                "ORDER BY cs.data DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFornecedor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReservaFornecedor r = new ReservaFornecedor();
                    r.setIdClienteServico(rs.getInt("id_cliente_servico"));
                    r.setDataEvento(rs.getTimestamp("data"));
                    r.setNomeCliente(rs.getString("nome_cliente"));
                    r.setEvento(rs.getString("evento"));
                    r.setDescricaoServico(rs.getString("descricao_servico"));
                    r.setPrecoServico(rs.getDouble("preco_servico"));
                    r.setDescricaoEspaco(rs.getString("descricao_espaco"));
                    lista.add(r);
                }
            }
        }
        return lista;
    }
}
