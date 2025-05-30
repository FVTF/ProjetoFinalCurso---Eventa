package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO {
    private final Connection conn;

    public ClienteDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Verifica se existe uma linha em Cliente para o dado idUser.
     */
    public boolean existsByUserId(int idUser) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Cliente WHERE id_user = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }
}
