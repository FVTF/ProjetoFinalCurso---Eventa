package com.example.servlet;

import com.example.model.Utilizador;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/HomeClienteServlet")
public class HomeClienteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtém a sessão e valida o utilizador
        HttpSession session = request.getSession(false);
        Utilizador utilizador = (session != null) ? (Utilizador) session.getAttribute("utilizador") : null;

        if (utilizador == null) {
            // Se não está autenticado, volta para login
            response.sendRedirect("LoginServlet");
            return;
        }

        // Envia o nome do utilizador para o JSP, se precisares como atributo (opcional)
        request.setAttribute("nome", utilizador.getNome());

        // Encaminha para o JSP correto
        request.getRequestDispatcher("HomeCliente.jsp").forward(request, response);
    }
}
