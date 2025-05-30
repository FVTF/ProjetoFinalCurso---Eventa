package com.example.servlet;

import com.example.DatabaseConnection;
import com.example.dao.ClienteServicoDAO;
import com.example.dao.FornecedorDAO;
import com.example.model.Fornecedor;
import com.example.model.Utilizador;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/RecusarReservaFornecedorServlet")
public class RecusarReservaFornecedorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Utilizador user = (Utilizador) session.getAttribute("utilizador");
        if (user == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        String idReservaStr = request.getParameter("idReserva");
        if (idReservaStr == null) {
            response.sendRedirect("DashboardFornecedorServlet");
            return;
        }
        int idReserva = Integer.parseInt(idReservaStr);

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Obter o id_fornecedor autenticado
            FornecedorDAO fornecedorDAO = new FornecedorDAO(conn);
            Fornecedor fornecedor = fornecedorDAO.findByUserId(user.getIdUser());
            if (fornecedor == null) {
                response.sendRedirect("DashboardFornecedorServlet");
                return;
            }
            int idFornecedor = fornecedor.getIdFornecedor();

            // Confirma que a reserva pertence MESMO a este fornecedor (serviço OU espaço)
            String checkSql = "SELECT COUNT(*) FROM Cliente_Servico cs " +
                    "LEFT JOIN Espaco e ON cs.id_espaco = e.id_espaco " +
                    "LEFT JOIN Servico_Fornecedor sf ON cs.id_servico_fornecedor = sf.id_servico_fornecedor " +
                    "WHERE cs.id_cliente_servico = ? " +
                    "AND (e.id_fornecedor = ? OR sf.id_fornecedor = ?)";
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setInt(1, idReserva);
                ps.setInt(2, idFornecedor);
                ps.setInt(3, idFornecedor);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) == 1) {
                    ClienteServicoDAO dao = new ClienteServicoDAO(conn);
                    dao.atualizarEstadoReserva(idReserva, "RECUSADO");

                    // Buscar o id_orcamento associado a esta reserva
                    int idOrcamento = 0;
                    String sql = "SELECT id_orcamento FROM Cliente_Servico WHERE id_cliente_servico = ?";
                    try (PreparedStatement ps2 = conn.prepareStatement(sql)) {
                        ps2.setInt(1, idReserva);
                        ResultSet rs2 = ps2.executeQuery();
                        if (rs2.next()) {
                            idOrcamento = rs2.getInt("id_orcamento");
                        }
                    }
                    if (idOrcamento > 0) {
                        dao.atualizarEstadoGlobalOrcamento(idOrcamento);
                    }
                } else {
                    // Não tem permissão para alterar esta reserva!
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Não autorizado.");
                    return;
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
        response.sendRedirect("DashboardFornecedorServlet");
    }
}
