package com.example.servlet;

import com.example.DatabaseConnection;
import com.example.dao.EventoDAO;
import com.example.dao.EspacoDAO;
import com.example.model.Evento;
import com.example.model.Espaco;
import com.example.model.Utilizador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/agendarEvento")
public class AgendarEventoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
                         throws ServletException, IOException {
        // 1) Verifica sessão de cliente
        HttpSession session = request.getSession(false);
        Utilizador user = (session != null)
                          ? (Utilizador) session.getAttribute("utilizador")
                          : null;
        if (user == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // 2) Carrega tipos de evento
            List<Evento> eventos = new EventoDAO(conn).listarTodos();
            // 3) Carrega espaços com lat/long
            List<Espaco> espacos = new EspacoDAO(conn).listarTodos();

            request.setAttribute("eventos", eventos);
            request.setAttribute("espacos",  espacos);
            // Encaminha para o JSP
            request.getRequestDispatcher("/AgendarEvento.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Erro ao carregar dados para agendamento", e);
        }
    }
}
