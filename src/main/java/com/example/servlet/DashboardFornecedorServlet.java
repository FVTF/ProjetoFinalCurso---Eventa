package com.example.servlet;

import com.example.DatabaseConnection;
import com.example.dao.ClienteServicoDAO;
import com.example.dao.ServicoFornecedorDAO;
import com.example.dao.FornecedorDAO;
import com.example.model.Fornecedor;
import com.example.model.ServicoFornecedor;
import com.example.model.Utilizador;
import com.example.model.ClienteServico;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/DashboardFornecedorServlet")
public class DashboardFornecedorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Utilizador user = (Utilizador) session.getAttribute("utilizador");
        if (user == null) {
            response.sendRedirect("LoginServlet");
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            FornecedorDAO fornecedorDAO = new FornecedorDAO(conn);
            Fornecedor fornecedor = fornecedorDAO.findByUserId(user.getIdUser());
            if (fornecedor == null) {
                response.sendRedirect("HomeCliente.jsp");
                return;
            }

            // MOSTRAR APENAS AS RESERVAS DO PRÓPRIO FORNECEDOR
            List<ClienteServico> ultimasReservas = ClienteServicoDAO.listarUltimasReservasPorFornecedor(conn, fornecedor.getIdFornecedor(), 5);
            int totalReservas   = ClienteServicoDAO.countReservasPorFornecedor(conn, fornecedor.getIdFornecedor(), null);
            int pendentes       = ClienteServicoDAO.countReservasPorFornecedor(conn, fornecedor.getIdFornecedor(), "PENDENTE");
            int aceites         = ClienteServicoDAO.countReservasPorFornecedor(conn, fornecedor.getIdFornecedor(), "ACEITE");
            int recusadas       = ClienteServicoDAO.countReservasPorFornecedor(conn, fornecedor.getIdFornecedor(), "RECUSADO");

            ServicoFornecedorDAO servicoFornecedorDAO = new ServicoFornecedorDAO(conn);
            List<ServicoFornecedor> servicos = servicoFornecedorDAO.listarPorFornecedor(fornecedor.getIdFornecedor());

            request.setAttribute("servicos", servicos);
            request.setAttribute("totalReservas", totalReservas);
            request.setAttribute("pendentes", pendentes);
            request.setAttribute("aceites", aceites);
            request.setAttribute("recusadas", recusadas);
            request.setAttribute("ultimasReservas", ultimasReservas);

            request.getRequestDispatcher("DashboardFornecedor.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
