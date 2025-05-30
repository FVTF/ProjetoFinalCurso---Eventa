package com.example.servlet;

import com.example.DatabaseConnection;
import com.example.dao.ClienteDAO;
import com.example.dao.FornecedorDAO;
import com.example.model.Utilizador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // GET → mostra o JSP de login
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("Login.jsp")
               .forward(request, response);
    }

    // POST → processa credenciais
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email    = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection conn = DatabaseConnection.getConnection()) {
            // 1) Verifica credenciais
            String sql = "SELECT id_user, nome, email FROM Utilizador WHERE email = ? AND pwd = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    // credenciais inválidas
                    response.sendRedirect("LoginServlet?erro=credenciais");
                    return;
                }

                // monta objeto utilizador
                Utilizador utilizador = new Utilizador();
                utilizador.setIdUser(rs.getInt("id_user"));
                utilizador.setNome(rs.getString("nome"));
                utilizador.setEmail(rs.getString("email"));

                // 2) Detecta tipo
                int idUser = utilizador.getIdUser();
                ClienteDAO clienteDAO       = new ClienteDAO(conn);
                FornecedorDAO fornecedorDAO = new FornecedorDAO(conn);

                String userType;
                Integer idCliente = null;
                if (clienteDAO.existsByUserId(idUser)) {
                    userType = "cliente";
                    // Busca id_cliente à base de dados
                    String sqlCliente = "SELECT id_cliente FROM Cliente WHERE id_user = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sqlCliente)) {
                        ps.setInt(1, idUser);
                        try (ResultSet rsCliente = ps.executeQuery()) {
                            if (rsCliente.next()) {
                                idCliente = rsCliente.getInt("id_cliente");
                            }
                        }
                    }
                } else if (fornecedorDAO.existsByUserId(idUser)) {
                    userType = "fornecedor";
                } else {
                    userType = "cliente"; // fallback
                }

                // 3) Guarda na sessão
                HttpSession session = request.getSession();
                session.setAttribute("utilizador", utilizador);
                session.setAttribute("userType", userType);
                if (idCliente != null) {
                    session.setAttribute("id_cliente", idCliente); // <<<<<<<<<<<<<<<< ADICIONADO!
                }

                // 4) Redireciona consoante o tipo
                if ("fornecedor".equals(userType)) {
                    response.sendRedirect("HomeFornecedorServlet");
                } else {
                    response.sendRedirect("HomeClienteServlet");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("LoginServlet?erro=tecnico");
        }
    }
}
