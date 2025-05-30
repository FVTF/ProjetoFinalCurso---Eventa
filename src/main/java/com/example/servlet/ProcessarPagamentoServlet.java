package com.example.servlet;

import com.example.dao.ClienteServicoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/ProcessarPagamentoServlet")
public class ProcessarPagamentoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idOrcamentoStr = request.getParameter("idOrcamento");
        String totalStr = request.getParameter("total");

        int idOrcamento = Integer.parseInt(idOrcamentoStr);
        double valor = Double.parseDouble(totalStr);

        long entidade = 12345;
        String referencia = gerarReferenciaPagamento(idOrcamento);

        try (Connection conn = com.example.DatabaseConnection.getConnection()) {
            conn.setAutoCommit(true); // ou false se quiseres rollback automático (ver notas abaixo)

            // 1. Validar estado do orçamento (apenas ACEITE pode pagar)
            String estadoGlobal = ClienteServicoDAO.getEstadoGlobalDoOrcamento(conn, idOrcamento);
            if (!"ACEITE".equalsIgnoreCase(estadoGlobal)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"erro\": \"Só pode pagar pedidos ACEITES por todos os fornecedores.\"}");
                return;
            }

            // 2. Buscar cliente
            int idCliente = buscarIdCliente(conn, idOrcamento);

            // 3. Buscar as reservas do orçamento
            List<Integer> reservas = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT id_cliente_servico FROM Cliente_Servico WHERE id_orcamento = ?")) {
                ps.setInt(1, idOrcamento);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) reservas.add(rs.getInt("id_cliente_servico"));
                }
            }

            // 4. Criar o pagamento
            int idPagamento = criarPagamento(conn, new java.util.Date(), entidade, referencia, valor, idCliente);

            // 5. Associar e atualizar reservas
            for (Integer idClienteServico : reservas) {
                associarPagamentoClienteServico(conn, idClienteServico, idPagamento);
                atualizarEstadoClienteServico(conn, idClienteServico, "PAGO");
            }

            // 6. Resposta JSON para o front-end (popup)
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String json = String.format(Locale.US,
                    "{\"entidade\":\"%d\",\"referencia\":\"%s\",\"valor\":\"%.2f\"}",
                    entidade, referencia, valor
            );
            response.getWriter().write(json);

            // Commit só seria preciso se usasses conn.setAutoCommit(false);

        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"erro\": \"Erro ao processar o pagamento.\"}");
            ex.printStackTrace(); // útil em DEV!
        }
    }

    private int buscarIdCliente(Connection conn, int idOrcamento) throws SQLException {
        String sql = "SELECT id_cliente FROM Orcamento WHERE id_orcamento = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idOrcamento);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return rs.getInt("id_cliente");
            }
        }
        throw new SQLException("Orçamento não encontrado!");
    }

    private String gerarReferenciaPagamento(int idOrcamento) {
        // Exemplo: "2024" + idOrcamento + random 3 digitos
        int random = new Random().nextInt(900) + 100; // 100 a 999
        return String.format("%04d%03d", idOrcamento, random);
    }

    private int criarPagamento(Connection conn, java.util.Date data, long entidade, String referencia, double valor, int idCliente) throws SQLException {
        String sql = "INSERT INTO Pagamento (data, entidade, referencia, valor, id_cliente) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, new java.sql.Timestamp(data.getTime()));
            ps.setLong(2, entidade);
            ps.setString(3, referencia);
            ps.setDouble(4, valor);
            ps.setInt(5, idCliente);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("Erro ao criar pagamento.");
    }

    private void associarPagamentoClienteServico(Connection conn, int idClienteServico, int idPagamento) throws SQLException {
        String sql = "INSERT INTO Pagamento_Cliente_Servico (id_cliente_servico, id_pagamento) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idClienteServico);
            ps.setInt(2, idPagamento);
            ps.executeUpdate();
        }
    }

    private void atualizarEstadoClienteServico(Connection conn, int idClienteServico, String estado) throws SQLException {
        String sql = "UPDATE Cliente_Servico SET estado = ? WHERE id_cliente_servico = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, idClienteServico);
            ps.executeUpdate();
        }
    }
}

