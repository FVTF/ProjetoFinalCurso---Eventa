package com.example.servlet;

import com.example.DatabaseConnection;
import com.example.dao.FornecedorDAO;
import com.example.model.Fornecedor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/EditarRamo")
public class EditarRamoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Carrega lista de ramos
        try (Connection conn = DatabaseConnection.getConnection()) {
            List<String[]> ramos = new ArrayList<>();
            String sql = "SELECT id_ramo, descricao FROM Ramo ORDER BY descricao";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ramos.add(new String[]{
                        String.valueOf(rs.getInt("id_ramo")),
                        rs.getString("descricao")
                    });
                }
            }
            req.setAttribute("ramos", ramos);
            req.getRequestDispatcher("EditarRamo.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int novoRamo = Integer.parseInt(req.getParameter("id_ramo"));
        HttpSession session = req.getSession();
        int idUser = ((com.example.model.Utilizador) session.getAttribute("utilizador")).getIdUser();

        try (Connection conn = DatabaseConnection.getConnection()) {
            FornecedorDAO fDao = new FornecedorDAO(conn);
            Fornecedor f = fDao.findByUserId(idUser);
            if (f != null) {
                fDao.updateRamo(f.getIdFornecedor(), novoRamo);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
        resp.sendRedirect("HomeFornecedor.jsp");
    }
}
