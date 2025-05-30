package com.example.dao;

import com.example.model.Evento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {
    private final Connection conn;

    public EventoDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Busca todos os tipos de evento na tabela Evento.
     */
    public List<Evento> listarTodos() throws SQLException {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT id_evento, descricao FROM Evento";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Evento e = new Evento();
                e.setIdEvento(rs.getInt("id_evento"));
                e.setDescricao(rs.getString("descricao"));
                lista.add(e);
            }
        }
        return lista;
    }
}
