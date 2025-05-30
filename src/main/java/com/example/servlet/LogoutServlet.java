package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // GET → invalida sessão e volta ao login
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Se existir sessão, invalida
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // Redireciona diretamente ao LoginServlet (que por GET mostra o Login.jsp)
        resp.sendRedirect("LoginServlet?logout=true");
    }

 
}
