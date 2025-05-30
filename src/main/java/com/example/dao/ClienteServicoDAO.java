package com.example.dao;

import com.example.model.ClienteServico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteServicoDAO {
    private final Connection conn;

    public ClienteServicoDAO(Connection conn) {
        this.conn = conn;
    }

    // ======================= MÉTODOS ESTÁTICOS =======================

    // 1. Reservas de espaços deste fornecedor (SÓ reservas de espaço, sem serviço)
    public static int countReservasPorEspacosFornecedor(Connection conn, int idFornecedor, String estado) throws SQLException {
        String sql = "SELECT COUNT(*) " +
                "FROM Cliente_Servico cs " +
                "JOIN Espaco e ON cs.id_espaco = e.id_espaco " +
                "WHERE e.id_fornecedor = ? " +
                "AND cs.id_servico_fornecedor IS NULL "; // <--- Só entradas de ESPAÇO!
        if (estado != null && !estado.trim().isEmpty()) {
            sql += "AND LOWER(cs.estado) = LOWER(?) ";
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFornecedor);
            if (estado != null && !estado.trim().isEmpty()) ps.setString(2, estado);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // 2. Reservas de serviços deste fornecedor (SÓ reservas de serviço, com id_servico_fornecedor)
    public static int countReservasPorServicosFornecedor(Connection conn, int idFornecedor, String estado) throws SQLException {
        String sql = "SELECT COUNT(*) " +
                "FROM Cliente_Servico cs " +
                "JOIN Servico_Fornecedor sf ON cs.id_servico_fornecedor = sf.id_servico_fornecedor " +
                "WHERE sf.id_fornecedor = ? " +
                "AND cs.id_servico_fornecedor IS NOT NULL "; // <--- Só entradas de SERVIÇO!
        if (estado != null && !estado.trim().isEmpty()) {
            sql += "AND LOWER(cs.estado) = LOWER(?) ";
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFornecedor);
            if (estado != null && !estado.trim().isEmpty()) ps.setString(2, estado);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // 1. Listar últimas reservas de espaços deste fornecedor (sem reservas de serviço)
    public static List<ClienteServico> listarUltimasReservasPorEspacosFornecedor(Connection conn, int idFornecedor, int limite) throws SQLException {
        String sql = "SELECT cs.*, u.nome AS nomeCliente, e.descricao AS descricaoServico " +
                "FROM Cliente_Servico cs " +
                "JOIN Espaco e ON cs.id_espaco = e.id_espaco " +
                "JOIN Cliente c ON cs.id_cliente = c.id_cliente " +
                "JOIN Utilizador u ON c.id_user = u.id_user " +
                "WHERE e.id_fornecedor = ? " +
                "AND cs.id_servico_fornecedor IS NULL " + // <--- Só reservas de espaço
                "ORDER BY cs.data DESC";
        if (limite > 0) sql += " OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        List<ClienteServico> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFornecedor);
            if (limite > 0) ps.setInt(2, limite);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ClienteServico cs = new ClienteServico();
                cs.setIdClienteServico(rs.getInt("id_cliente_servico"));
                cs.setIdServicoFornecedor(rs.getInt("id_servico_fornecedor"));
                cs.setIdCliente(rs.getInt("id_cliente"));
                cs.setData(rs.getTimestamp("data"));
                cs.setIdEvento(rs.getInt("id_evento"));
                cs.setIdEspaco(rs.getInt("id_espaco"));
                cs.setPrecoServico(rs.getDouble("preco_servico"));
                cs.setPrecoEspaco(rs.getDouble("preco_espaco"));
                cs.setEstado(rs.getString("estado"));
                cs.setNomeCliente(rs.getString("nomeCliente"));
                cs.setDescricaoServico(rs.getString("descricaoServico"));
                cs.setIdOrcamento(rs.getInt("id_orcamento"));
                lista.add(cs);
            }
        }
        return lista;
    }

    // 2. Listar últimas reservas de serviços deste fornecedor (só as que têm serviço)
    public static List<ClienteServico> listarUltimasReservasPorServicosFornecedor(Connection conn, int idFornecedor, int limite) throws SQLException {
        String sql = "SELECT cs.*, u.nome AS nomeCliente, s.descricao AS descricaoServico " +
                "FROM Cliente_Servico cs " +
                "JOIN Servico_Fornecedor sf ON cs.id_servico_fornecedor = sf.id_servico_fornecedor " +
                "JOIN Servico s ON sf.id_servico = s.id_servico " +
                "JOIN Cliente c ON cs.id_cliente = c.id_cliente " +
                "JOIN Utilizador u ON c.id_user = u.id_user " +
                "WHERE sf.id_fornecedor = ? " +
                "AND cs.id_servico_fornecedor IS NOT NULL " + // <--- Só reservas de serviço!
                "ORDER BY cs.data DESC";
        if (limite > 0) sql += " OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        List<ClienteServico> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFornecedor);
            if (limite > 0) ps.setInt(2, limite);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ClienteServico cs = new ClienteServico();
                cs.setIdClienteServico(rs.getInt("id_cliente_servico"));
                cs.setIdServicoFornecedor(rs.getInt("id_servico_fornecedor"));
                cs.setIdCliente(rs.getInt("id_cliente"));
                cs.setData(rs.getTimestamp("data"));
                cs.setIdEvento(rs.getInt("id_evento"));
                cs.setIdEspaco(rs.getInt("id_espaco"));
                cs.setPrecoServico(rs.getDouble("preco_servico"));
                cs.setPrecoEspaco(rs.getDouble("preco_espaco"));
                cs.setEstado(rs.getString("estado"));
                cs.setNomeCliente(rs.getString("nomeCliente"));
                cs.setDescricaoServico(rs.getString("descricaoServico"));
                cs.setIdOrcamento(rs.getInt("id_orcamento"));
                lista.add(cs);
            }
        }
        return lista;
    }

    // 3. JUNTA TUDO: Lista reservas para o fornecedor (sem duplicados)
    public static List<ClienteServico> listarUltimasReservasPorFornecedor(Connection conn, int idFornecedor, int limite) throws SQLException {
        List<ClienteServico> lista = new ArrayList<>();

        // 1. Reservas de espaços deste fornecedor (sem serviço)
        lista.addAll(listarUltimasReservasPorEspacosFornecedor(conn, idFornecedor, 0));
        // 2. Reservas de serviços deste fornecedor (com serviço)
        lista.addAll(listarUltimasReservasPorServicosFornecedor(conn, idFornecedor, 0));

        // Elimina duplicados pelo id_cliente_servico
        List<ClienteServico> semDuplicados = new ArrayList<>();
        java.util.Set<Integer> idsVistos = new java.util.HashSet<>();
        for (ClienteServico cs : lista) {
            if (!idsVistos.contains(cs.getIdClienteServico())) {
                semDuplicados.add(cs);
                idsVistos.add(cs.getIdClienteServico());
            }
        }

        // Ordenar por data DESC
        semDuplicados.sort((a, b) -> b.getData().compareTo(a.getData()));
        if (limite > 0 && semDuplicados.size() > limite) {
            return semDuplicados.subList(0, limite);
        }
        return semDuplicados;
    }

    // Conta apenas as reservas DO fornecedor autenticado
    public static int countReservasPorFornecedor(Connection conn, int idFornecedor, String estado) throws SQLException {
        String sql = "SELECT COUNT(*) " +
                "FROM Cliente_Servico cs " +
                "LEFT JOIN Espaco e ON cs.id_espaco = e.id_espaco " +
                "LEFT JOIN Servico_Fornecedor sf ON cs.id_servico_fornecedor = sf.id_servico_fornecedor " +
                "WHERE (cs.id_espaco IS NOT NULL AND e.id_fornecedor = ? AND cs.id_servico_fornecedor IS NULL) " +
                "   OR (cs.id_servico_fornecedor IS NOT NULL AND sf.id_fornecedor = ?) ";
        if (estado != null && !estado.trim().isEmpty()) {
            sql += "AND LOWER(cs.estado) = LOWER(?) ";
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFornecedor);
            ps.setInt(2, idFornecedor);
            if (estado != null && !estado.trim().isEmpty()) ps.setString(3, estado);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // ... O resto do DAO (não mudou, mantém os métodos de inserir, atualizar, etc) ...
    // (os métodos de inserirReserva, inserirReservas, atualizarEstadoReserva, etc, não precisam ser alterados)
    
    public static String getEstadoGlobalDoOrcamento(Connection conn, int idOrcamento) throws SQLException {
        String sql = "SELECT COUNT(*) as total, " +
                "SUM(CASE WHEN estado = 'ACEITE' THEN 1 ELSE 0 END) as aceites, " +
                "SUM(CASE WHEN estado = 'RECUSADO' THEN 1 ELSE 0 END) as recusados, " +
                "SUM(CASE WHEN estado = 'PAGO' THEN 1 ELSE 0 END) as pagos " +
                "FROM Cliente_Servico WHERE id_orcamento = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOrcamento);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int total = rs.getInt("total");
                int aceites = rs.getInt("aceites");
                int recusados = rs.getInt("recusados");
                int pagos = rs.getInt("pagos");
                if (recusados > 0) return "RECUSADO";
                if (pagos == total && total > 0) return "PAGO"; // NOVO!
                if (aceites == total && total > 0) return "ACEITE";
                return "PENDENTE";
            }
        }
        return "PENDENTE";
    }


    public static List<String> listarEstadosReservasPorOrcamento(Connection conn, int idOrcamento) throws SQLException {
        List<String> estados = new ArrayList<>();
        String sql = "SELECT estado FROM Cliente_Servico WHERE id_orcamento = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOrcamento);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                estados.add(rs.getString("estado"));
            }
        }
        return estados;
    }

    // ======================= MÉTODOS DE INSTÂNCIA =======================

    public void inserirReservas(List<ClienteServico> reservas, Integer idOrcamento) throws SQLException {
        for (ClienteServico cs : reservas) {
            inserirReserva(cs, idOrcamento);
        }
    }

    public void inserirReserva(ClienteServico r, Integer idOrcamento) throws SQLException {
        Double precoEspaco = 0.0;
        Double precoServico = 0.0;

        // Buscar precoEspaco se id_espaco existir
        if (r.getIdEspaco() != null && r.getIdEspaco() > 0) {
            String sqlEspaco = "SELECT preco FROM Espaco WHERE id_espaco = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlEspaco)) {
                ps.setInt(1, r.getIdEspaco());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) precoEspaco = rs.getDouble("preco");
            }
        }

        // Buscar precoServico se id_servico_fornecedor existir
        if (r.getIdServicoFornecedor() != null && r.getIdServicoFornecedor() > 0) {
            String sqlServico = "SELECT preco FROM Servico_Fornecedor WHERE id_servico_fornecedor = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlServico)) {
                ps.setInt(1, r.getIdServicoFornecedor());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) precoServico = rs.getDouble("preco");
            }
        }

        String sql = "INSERT INTO Cliente_Servico " +
                "(id_servico_fornecedor, id_cliente, data, id_evento, id_espaco, preco_servico, preco_espaco, estado, id_orcamento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (r.getIdServicoFornecedor() != null && r.getIdServicoFornecedor() > 0)
                stmt.setInt(1, r.getIdServicoFornecedor());
            else
                stmt.setNull(1, java.sql.Types.INTEGER);

            stmt.setInt(2, r.getIdCliente());
            stmt.setTimestamp(3, new java.sql.Timestamp(r.getData().getTime()));
            stmt.setInt(4, r.getIdEvento());

            if (r.getIdEspaco() != null && r.getIdEspaco() > 0)
                stmt.setInt(5, r.getIdEspaco());
            else
                stmt.setNull(5, java.sql.Types.INTEGER);

            stmt.setDouble(6, precoServico);
            stmt.setDouble(7, precoEspaco);

            stmt.setString(8, r.getEstado());
            if (idOrcamento != null)
                stmt.setInt(9, idOrcamento);
            else
                stmt.setNull(9, java.sql.Types.INTEGER);

            stmt.executeUpdate();
        }
    }

    public void atualizarEstadoReserva(int idReserva, String novoEstado) throws SQLException {
        String sql = "UPDATE Cliente_Servico SET estado = ? WHERE id_cliente_servico = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, novoEstado);
            ps.setInt(2, idReserva);
            ps.executeUpdate();
        }
    }

    public void atualizarEstadoGlobalOrcamento(int idOrcamento) throws SQLException {
        String novoEstado = getEstadoGlobalDoOrcamento(this.conn, idOrcamento);
        String sql = "UPDATE Orcamento SET estado_global = ? WHERE id_orcamento = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, novoEstado);
            ps.setInt(2, idOrcamento);
            ps.executeUpdate();
        }
    }
}
