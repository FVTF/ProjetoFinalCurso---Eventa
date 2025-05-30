package com.example.servlet;

import com.example.DatabaseConnection;
import com.example.dao.EspacoDAO;
import com.example.dao.FornecedorDAO;
import com.example.dao.LocalidadeDAO;
import com.example.model.Espaco;
import com.example.model.Fornecedor;
import com.example.model.Utilizador;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/EditarEspacoServlet")
public class EditarEspacoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Utilizador user = (session != null)
                          ? (Utilizador) session.getAttribute("utilizador")
                          : null;
        if (user == null) {
            resp.sendRedirect("LoginServlet");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            Fornecedor f = new FornecedorDAO(conn).findByUserId(user.getIdUser());
            if (f == null) {
                resp.sendRedirect("HomeCliente.jsp");
                return;
            }

            int id = Integer.parseInt(req.getParameter("id"));
            Espaco esp = new EspacoDAO(conn).findById(id);
            req.setAttribute("espaco", esp);
            req.getRequestDispatcher("EditarEspaco.jsp")
               .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException("Erro ao carregar dados do espaço", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Utilizador user = (session != null)
                          ? (Utilizador) session.getAttribute("utilizador")
                          : null;
        if (user == null) {
            resp.sendRedirect("LoginServlet");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            Fornecedor f = new FornecedorDAO(conn).findByUserId(user.getIdUser());
            if (f == null) {
                resp.sendRedirect("HomeCliente.jsp");
                return;
            }

            LocalidadeDAO locDao = new LocalidadeDAO(conn);
            String codPostal = req.getParameter("cod_postal").trim();

            // valida existência do código postal
            if (!locDao.existeCodigoPostal(codPostal)) {
                // volta ao form com mensagem de erro
                int idEspaco = Integer.parseInt(req.getParameter("id_espaco"));
                Espaco esp = new EspacoDAO(conn).findById(idEspaco);
                req.setAttribute("espaco", esp);
                req.setAttribute("error", "Código postal inválido ou não cadastrado.");
                req.getRequestDispatcher("EditarEspaco.jsp")
                   .forward(req, resp);
                return;
            }

            // carrega o objeto original e atualiza apenas o que mudou
            int idEspaco = Integer.parseInt(req.getParameter("id_espaco"));
            Espaco e = new EspacoDAO(conn).findById(idEspaco);

            e.setDescricao(req.getParameter("descricao"));
            e.setPreco(Double.parseDouble(req.getParameter("preco")));
            e.setMorada(req.getParameter("morada"));
            e.setCodPostal(codPostal);
            e.setLatitude(Double.parseDouble(req.getParameter("latitude")));
            e.setLongitude(Double.parseDouble(req.getParameter("longitude")));

            new EspacoDAO(conn).update(e);

            resp.sendRedirect("MeusEspacosServlet");
        } catch (Exception ex) {
            throw new ServletException("Erro ao atualizar espaço", ex);
        }
    }
}
