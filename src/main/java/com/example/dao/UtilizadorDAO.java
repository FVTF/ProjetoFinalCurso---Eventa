package com.example.dao;

import com.example.model.Utilizador;
import java.sql.*;

public class UtilizadorDAO {
    private final Connection conn;

    public UtilizadorDAO(Connection conn) {
        this.conn = conn;
    }

    /** Insere e devolve o id gerado. */
    public int create(Utilizador u) throws SQLException {
    	String sql = "INSERT INTO Utilizador (nome, email, pwd, data_registo, ativo, telefone, morada, cod_postal) "
    	           + "VALUES (?, ?, HASHBYTES('SHA2_512', ?), ?, ?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, u.getNome());
            st.setString(2, u.getEmail());
            st.setString(3, u.getPwd());
            st.setTimestamp(4, u.getDataRegisto());
            st.setBoolean(5, u.isAtivo());
            st.setString(6, u.getTelefone());
            st.setString(7, u.getMorada());
            st.setString(8, u.getCodPostal());

            int rows = st.executeUpdate();
            if (rows == 0) throw new SQLException("Falha ao inserir utilizador.");
            try (ResultSet keys = st.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new SQLException("Falha ao obter id_user.");
    }
}

