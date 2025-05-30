package com.example.servlet;

import com.example.DatabaseConnection;
import com.example.dao.FornecedorDAO;
import com.example.dao.ServicoFornecedorDAO;
import com.example.model.Fornecedor;
import com.example.model.ServicoFornecedor;
import com.example.model.Utilizador;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/MeusServicosServlet")
public class MeusServicosServlet extends HttpServlet {
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
            ServicoFornecedorDAO dao = new ServicoFornecedorDAO(conn);
            List<ServicoFornecedor> lista = dao.listarPorFornecedor(fornecedor.getIdFornecedor());
            request.setAttribute("servicos", lista);
            request.getRequestDispatcher("MeusServicos.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
