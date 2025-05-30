package com.example.dao;

import com.example.model.Fornecedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FornecedorDAO {
    private final Connection conn;

    public FornecedorDAO(Connection conn) {
        this.conn = conn;
    }

    /** Insere a associação user→ramo em Fornecedor */
    public void createFornecedor(int idUser, int idRamo) throws SQLException {
        String sql = "INSERT INTO Fornecedor (id_user, id_ramo) VALUES (?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idUser);
            st.setInt(2, idRamo);
            st.executeUpdate();
        }
    }

    /** Verifica se existe uma linha em Fornecedor para o dado idUser. */
    public boolean existsByUserId(int idUser) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Fornecedor WHERE id_user = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    /** Carrega Fornecedor + descrição do ramo + código postal via JOIN */
    public Fornecedor findByUserId(int idUser) throws SQLException {
        String sql =
          "SELECT f.id_fornecedor, f.id_user, f.id_ramo, r.descricao, u.cod_postal " +
          "FROM Fornecedor f " +
          "JOIN Ramo r ON f.id_ramo = r.id_ramo " +
          "JOIN Utilizador u ON f.id_user = u.id_user " +
          "WHERE f.id_user = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Fornecedor f = new Fornecedor();
                f.setIdFornecedor(rs.getInt("id_fornecedor"));
                f.setIdUser(rs.getInt("id_user"));
                f.setIdRamo(rs.getInt("id_ramo"));
                f.setRamoDescricao(rs.getString("descricao"));
                f.setCodPostal(rs.getString("cod_postal")); // NOVO!
                return f;
            }
        }
    }

    /** Atualiza o ramo de um fornecedor */
    public void updateRamo(int idFornecedor, int novoRamo) throws SQLException {
        String sql = "UPDATE Fornecedor SET id_ramo = ? WHERE id_fornecedor = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, novoRamo);
            ps.setInt(2, idFornecedor);
            ps.executeUpdate();
        }
    }

    public String getRamoDescricaoByFornecedorId(int idFornecedor) throws SQLException {
        String sql = "SELECT r.descricao FROM Fornecedor f JOIN Ramo r ON f.id_ramo = r.id_ramo WHERE f.id_fornecedor = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFornecedor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("descricao");
                }
            }
        }
        return null;
    }
}
