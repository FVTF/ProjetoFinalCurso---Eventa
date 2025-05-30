package com.example.dao;

import com.example.model.Servico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicoDAO {
    private final Connection conn;
    public ServicoDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Servico> listarTodos() throws SQLException {
        List<Servico> lista = new ArrayList<>();
        String sql = "SELECT id_servico, descricao, id_ramo FROM Servico ORDER BY descricao";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Servico s = new Servico();
                s.setIdServico(rs.getInt("id_servico"));
                s.setDescricao(rs.getString("descricao"));
                s.setIdRamo(rs.getInt("id_ramo"));
                lista.add(s);
            }
        }
        return lista;
    }

    // Verifica se serviço existe para o ramo
    public Servico findByDescricaoAndRamo(String descricao, int idRamo) throws SQLException {
        String sql = "SELECT id_servico, descricao, id_ramo FROM Servico WHERE descricao = ? AND id_ramo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, descricao);
            ps.setInt(2, idRamo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Servico s = new Servico();
                    s.setIdServico(rs.getInt("id_servico"));
                    s.setDescricao(rs.getString("descricao"));
                    s.setIdRamo(rs.getInt("id_ramo"));
                    return s;
                }
            }
        }
        return null;
    }

    // Insere novo serviço
    public int inserirServico(Servico s) throws SQLException {
        String sql = "INSERT INTO Servico (descricao, id_ramo) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getDescricao());
            ps.setInt(2, s.getIdRamo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }
    
    public Servico findById(int id) throws SQLException {
        String sql = "SELECT id_servico, descricao FROM Servico WHERE id_servico = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Servico s = new Servico();
                    s.setIdServico(rs.getInt("id_servico"));
                    s.setDescricao(rs.getString("descricao"));
                    return s;
                }
            }
        }
        return null;
    }
}
