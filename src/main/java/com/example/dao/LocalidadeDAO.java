package com.example.dao;

import com.example.model.Localidade;    // importa o modelo
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocalidadeDAO {
    private final Connection conn;

    public LocalidadeDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Lista todas as localidades (cod_postal, nome, cod_distrito).
     */
    public List<Localidade> listarTodos() throws SQLException {
        List<Localidade> lista = new ArrayList<>();
        String sql = "SELECT cod_postal, nome, cod_distrito FROM Localidade ORDER BY nome";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Localidade l = new Localidade();
                l.setCodPostal(rs.getString("cod_postal"));
                l.setNome(rs.getString("nome"));
                l.setCodDistrito(rs.getInt("cod_distrito"));
                lista.add(l);
            }
        }
        return lista;
    }

    /**
     * Alias para compatibilidade com quem usa findAll().
     */
    public List<Localidade> findAll() throws SQLException {
        return listarTodos();
    }

    /**
     * Verifica se já existe uma localidade com este código postal.
     */
    public boolean existeCodigoPostal(String codPostal) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Localidade WHERE cod_postal = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codPostal);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    /**
     * Insere uma nova localidade.
     */
    public void inserir(String nome, String codPostal, int codDistrito) throws SQLException {
        String sql = "INSERT INTO Localidade (cod_postal, nome, cod_distrito) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codPostal);
            ps.setString(2, nome);
            ps.setInt(3, codDistrito);
            ps.executeUpdate();
        }
    }
}
