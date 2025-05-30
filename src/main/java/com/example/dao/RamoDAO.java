package com.example.dao;

import com.example.model.Ramo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RamoDAO {
    private final Connection conn;

    public RamoDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Ramo> listarTodos() throws SQLException {
        List<Ramo> lista = new ArrayList<>();
        String sql = "SELECT id_ramo, descricao FROM Ramo ORDER BY descricao";
        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Ramo r = new Ramo();
                r.setIdRamo(rs.getInt("id_ramo"));
                r.setDescricao(rs.getString("descricao"));
                lista.add(r);
            }
        }
        return lista;
    }

    public int procurarIdPorDescricao(String desc) throws SQLException {
        String sql = "SELECT id_ramo FROM Ramo WHERE descricao = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, desc);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return rs.getInt("id_ramo");
            }
        }
        return 0;
    }

    public int inserirNovo(String desc) throws SQLException {
        String sql = "INSERT INTO Ramo (descricao) VALUES (?)";
        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, desc);
            st.executeUpdate();
            try (ResultSet keys = st.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new SQLException("Falha ao gerar id_ramo.");
    }

    public int getOrCreateRamo(String desc) throws SQLException {
        int id = procurarIdPorDescricao(desc);
        if (id != 0) return id;
        try {
            return inserirNovo(desc);
        } catch (SQLException e) {
            // concorrência: duplicado
            return procurarIdPorDescricao(desc);
        }
    }
    
 // Retorna o id_ramo de um serviço (por id_servico)
    public int procurarIdPorServico(int idServico) throws SQLException {
        String sql = "SELECT id_ramo FROM Servico WHERE id_servico = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idServico);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return rs.getInt("id_ramo");
            }
        }
        return 0;
    }
}

