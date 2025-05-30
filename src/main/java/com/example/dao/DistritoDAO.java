package com.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.example.model.Distrito;

public class DistritoDAO {
    private final Connection conn;
    public DistritoDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Distrito> listarTodos() throws SQLException {
        List<Distrito> lista = new ArrayList<>();
        String sql = "SELECT cod_distrito, nome FROM Distrito ORDER BY nome";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Distrito d = new Distrito();
                d.setCodDistrito(rs.getInt("cod_distrito"));
                d.setNome(rs.getString("nome"));
                lista.add(d);
            }
        }
        return lista;
    }
}

