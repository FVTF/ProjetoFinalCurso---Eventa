package com.example.servlet;

import com.example.model.Utilizador;
import com.example.model.Fornecedor;
import com.example.dao.FornecedorDAO;
import com.example.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/HomeFornecedorServlet")
public class HomeFornecedorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Utilizador user = (session != null) ? (Utilizador) session.getAttribute("utilizador") : null;

        if (user == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        Fornecedor fornecedor = null;
        try (Connection conn = DatabaseConnection.getConnection()) {
            FornecedorDAO dao = new FornecedorDAO(conn);
            fornecedor = dao.findByUserId(user.getIdUser());
        } catch (Exception ex) {
            throw new ServletException(ex);
        }

        if (fornecedor == null) {
            // Se não for fornecedor, redireciona para home de cliente
            response.sendRedirect("HomeCliente.jsp");
            return;
        }

        // Guarda o fornecedor e user como atributos de request (disponíveis no JSP)
        request.setAttribute("fornecedor", fornecedor);
        request.setAttribute("utilizador", user);
        request.getRequestDispatcher("HomeFornecedor.jsp").forward(request, response);
    }
}
