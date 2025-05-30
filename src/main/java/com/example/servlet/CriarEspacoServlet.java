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

@WebServlet("/CriarEspacoServlet")
public class CriarEspacoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Utilizador user = (session != null)
                          ? (Utilizador) session.getAttribute("utilizador")
                          : null;
        if (user == null) {
            response.sendRedirect("LoginServlet");
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            FornecedorDAO fDao = new FornecedorDAO(conn);
            Fornecedor fornecedor = fDao.findByUserId(user.getIdUser());
            if (fornecedor == null) {
                response.sendRedirect("HomeCliente.jsp");
                return;
            }
            request.getRequestDispatcher("CriarEspaco.jsp")
                   .forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Erro ao carregar formulário de criação de espaço", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Utilizador user = (session != null)
                          ? (Utilizador) session.getAttribute("utilizador")
                          : null;
        if (user == null) {
            response.sendRedirect("LoginServlet");
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            FornecedorDAO fDao = new FornecedorDAO(conn);
            Fornecedor fornecedor = fDao.findByUserId(user.getIdUser());
            if (fornecedor == null) {
                response.sendRedirect("HomeCliente.jsp");
                return;
            }

            // Lê parâmetros
            String descricao = request.getParameter("descricao");
            double preco      = Double.parseDouble(request.getParameter("preco"));
            String morada     = request.getParameter("morada");
            String codPostal  = request.getParameter("cod_postal");
            double latitude   = Double.parseDouble(
                                  request.getParameter("latitude"));
            double longitude  = Double.parseDouble(
                                  request.getParameter("longitude"));

            // Cria espaço
            Espaco espaco = new Espaco();
            espaco.setDescricao(descricao);
            espaco.setPreco(preco);
            espaco.setMorada(morada);
            espaco.setCodPostal(codPostal);
            espaco.setIdFornecedor(fornecedor.getIdFornecedor());
            espaco.setLatitude(latitude);
            espaco.setLongitude(longitude);

            // Persiste no BD
            EspacoDAO espDao = new EspacoDAO(conn);
            espDao.create(espaco);

            // Volta à listagem
            response.sendRedirect("MeusEspacosServlet");
        } catch (Exception e) {
            throw new ServletException("Erro ao criar espaço", e);
        }
    }
}
