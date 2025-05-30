package com.example.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/CancelarOrcamentoServlet")
public class CancelarOrcamentoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Apenas redireciona para ExplorarServicosServlet sem limpar as escolhas
        response.sendRedirect("ExplorarServicosServlet");
    }
}
