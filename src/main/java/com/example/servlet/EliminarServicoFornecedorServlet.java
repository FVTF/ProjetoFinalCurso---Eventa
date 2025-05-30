package com.example.servlet;

import com.example.DatabaseConnection;
import com.example.dao.ServicoFornecedorDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/EliminarServicoFornecedorServlet")
public class EliminarServicoFornecedorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        try (Connection conn = DatabaseConnection.getConnection()) {
            new ServicoFornecedorDAO(conn).delete(id);
            response.sendRedirect("MeusServicosServlet");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
