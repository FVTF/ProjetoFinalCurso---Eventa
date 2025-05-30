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
import java.util.List;

@WebServlet("/MeusEspacosServlet")
public class MeusEspacosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1) Valida sessão
        HttpSession session = request.getSession(false);
        Utilizador user = (session != null) ? (Utilizador) session.getAttribute("utilizador") : null;
        if (user == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // 2) Descobre o fornecedor associado ao utilizador
            FornecedorDAO fDao = new FornecedorDAO(conn);
            Fornecedor fornecedor = fDao.findByUserId(user.getIdUser());
            if (fornecedor == null) {
                response.sendRedirect("HomeCliente.jsp");
                return;
            }

            // 3) Carrega apenas os espaços deste fornecedor
            EspacoDAO espDao = new EspacoDAO(conn);
            List<Espaco> lista = espDao.listarPorFornecedor(fornecedor.getIdFornecedor());

            // Debug (remover em produção)
            System.out.println("[MeusEspacosServlet] espaços encontrados: " + lista.size());

            // 4) Passa ao JSP e mostra
            request.setAttribute("espacos", lista);
            request.getRequestDispatcher("MeusEspacos.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Erro ao carregar lista de espaços", e);
        }
    }
}
