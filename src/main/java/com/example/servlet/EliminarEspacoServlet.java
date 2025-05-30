// EliminarEspacoServlet.java
package com.example.servlet;

import com.example.DatabaseConnection;
import com.example.dao.EspacoDAO;
import com.example.dao.FornecedorDAO;
import com.example.model.Espaco;
import com.example.model.Fornecedor;
import com.example.model.Utilizador;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/EliminarEspacoServlet")
public class EliminarEspacoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Valida sessão
        HttpSession session = request.getSession(false);
        Utilizador user = (session != null) ? (Utilizador) session.getAttribute("utilizador") : null;
        if (user == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        // Lê o ID do espaço a apagar
        int idEspaco = Integer.parseInt(request.getParameter("id"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Verifica se é fornecedor
            FornecedorDAO fDao = new FornecedorDAO(conn);
            Fornecedor fornecedor = fDao.findByUserId(user.getIdUser());
            if (fornecedor == null) {
                response.sendRedirect("HomeCliente.jsp");
                return;
            }

            // Confirma que o espaço pertence a este fornecedor
             Espaco e = new EspacoDAO(conn).findById(idEspaco);
             if (e == null || e.getIdFornecedor() != fornecedor.getIdFornecedor()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                 return;
             }

            // Executa remoção
            EspacoDAO espDao = new EspacoDAO(conn);
            espDao.delete(idEspaco);

            // Redireciona de volta à listagem
            response.sendRedirect("MeusEspacosServlet");
        } catch (Exception e) {
            throw new ServletException("Erro ao apagar espaço", e);
        }
    }
}
