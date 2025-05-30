package com.example.servlet;

import com.example.dao.DistritoDAO;
import com.example.dao.LocalidadeDAO;
import com.example.dao.UtilizadorDAO;
import com.example.model.Distrito;
import com.example.model.Utilizador;
import com.example.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/registo")
public class RegistoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // GET: carrega o formulário + distritos
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            DistritoDAO distritoDAO = new DistritoDAO(conn);
            List<Distrito> distritos = distritoDAO.listarTodos();

            request.setAttribute("distritos", distritos);
            request.getRequestDispatcher("Registo.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // POST: processa o registo do cliente
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nome        = request.getParameter("nome");
        String email       = request.getParameter("email");
        String password    = request.getParameter("password");
        String telefone    = request.getParameter("telefone");
        String morada      = request.getParameter("morada");
        String localidade  = request.getParameter("localidade");
        String codPostal   = request.getParameter("cod_postal");
        int    codDistrito = Integer.parseInt(request.getParameter("cod_distrito"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 1) garante que a Localidade existe
            LocalidadeDAO locDAO = new LocalidadeDAO(conn);
            if (!locDAO.existeCodigoPostal(codPostal)) {
                locDAO.inserir(localidade, codPostal, codDistrito);
            }

            // 2) criar Utilizador
            Utilizador u = new Utilizador();
            u.setNome(nome);
            u.setEmail(email);
            u.setPwd(password);
            u.setDataRegisto(new Timestamp(System.currentTimeMillis()));
            u.setAtivo(true);
            u.setTelefone(telefone);
            u.setMorada(morada);
            u.setCodPostal(codPostal);

            UtilizadorDAO userDAO = new UtilizadorDAO(conn);
            int idUser = userDAO.create(u);

            // 3) inserir cliente
            String sqlCliente = "INSERT INTO Cliente (id_user) VALUES (?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlCliente)) {
                stmt.setInt(1, idUser);
                stmt.executeUpdate();
            }

            conn.commit();
            System.out.println("✅ Cliente registado com sucesso. id_user=" + idUser);

            // 4) redirecionar para login
            response.sendRedirect("LoginServlet?sucesso=true");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                               "Erro ao registar cliente: " + e.getMessage());
        }
    }
}
