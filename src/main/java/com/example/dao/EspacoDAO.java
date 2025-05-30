package com.example.dao;

import com.example.model.Espaco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;  
import java.util.ArrayList;
import java.util.List;

public class EspacoDAO {
    private final Connection conn;

    public EspacoDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Lista todos os espaços (para uso genérico, ex.: na página de agendamento).
     */
    public List<Espaco> listarTodos() throws SQLException {
        List<Espaco> lista = new ArrayList<>();
        String sql = "SELECT * FROM Espaco";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Espaco e = new Espaco();
                e.setIdEspaco(rs.getInt("id_espaco"));
                e.setDescricao(rs.getString("descricao"));
                e.setIdFornecedor(rs.getInt("id_fornecedor"));
                e.setPreco(rs.getDouble("preco"));
                e.setMorada(rs.getString("morada"));
                e.setCodPostal(rs.getString("cod_postal"));
                e.setLatitude(rs.getDouble("latitude"));
                e.setLongitude(rs.getDouble("longitude"));
                lista.add(e);
            }
        }
        return lista;
    }

    /**
     * Lista apenas os espaços de um fornecedor específico.
     */
    public List<Espaco> listarPorFornecedor(int idFornecedor) throws SQLException {
        List<Espaco> lista = new ArrayList<>();
        String sql = "SELECT * FROM Espaco WHERE id_fornecedor = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFornecedor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Espaco e = new Espaco();
                    e.setIdEspaco(rs.getInt("id_espaco"));
                    e.setDescricao(rs.getString("descricao"));
                    e.setIdFornecedor(rs.getInt("id_fornecedor"));
                    e.setPreco(rs.getDouble("preco"));
                    e.setMorada(rs.getString("morada"));
                    e.setCodPostal(rs.getString("cod_postal"));
                    e.setLatitude(rs.getDouble("latitude"));
                    e.setLongitude(rs.getDouble("longitude"));
                    lista.add(e);
                }
            }
        }
        return lista;
    }

    /**
     * Insere um novo espaço, retornando o ID gerado no próprio objeto.
     */
    public void create(Espaco e) throws SQLException {
        String sql = "INSERT INTO Espaco "
                   + "(descricao, id_fornecedor, preco, morada, cod_postal, latitude, longitude) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getDescricao());
            ps.setInt(2, e.getIdFornecedor());
            ps.setDouble(3, e.getPreco());
            ps.setString(4, e.getMorada());
            ps.setString(5, e.getCodPostal());
            ps.setDouble(6, e.getLatitude());
            ps.setDouble(7, e.getLongitude());
            ps.executeUpdate();
            // obtém o ID gerado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    e.setIdEspaco(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Busca um espaço pelo seu ID.
     */
    public Espaco findById(int id) throws SQLException {
        String sql = "SELECT * FROM Espaco WHERE id_espaco = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Espaco e = new Espaco();
                    e.setIdEspaco(rs.getInt("id_espaco"));
                    e.setDescricao(rs.getString("descricao"));
                    e.setIdFornecedor(rs.getInt("id_fornecedor"));
                    e.setPreco(rs.getDouble("preco"));
                    e.setMorada(rs.getString("morada"));
                    e.setCodPostal(rs.getString("cod_postal"));
                    e.setLatitude(rs.getDouble("latitude"));
                    e.setLongitude(rs.getDouble("longitude"));
                    return e;
                }
            }
        }
        return null;
    }

    /**
     * Atualiza os dados de um espaço existente.
     */
    public void update(Espaco e) throws SQLException {
        String sql = "UPDATE Espaco SET "
                   + "descricao = ?, preco = ?, morada = ?, cod_postal = ?, latitude = ?, longitude = ? "
                   + "WHERE id_espaco = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getDescricao());
            ps.setDouble(2, e.getPreco());
            ps.setString(3, e.getMorada());
            ps.setString(4, e.getCodPostal());
            ps.setDouble(5, e.getLatitude());
            ps.setDouble(6, e.getLongitude());
            ps.setInt(7, e.getIdEspaco());
            ps.executeUpdate();
        }
    }

    /**
     * Remove um espaço pelo seu ID.
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Espaco WHERE id_espaco = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}