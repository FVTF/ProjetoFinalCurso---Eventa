package com.example.servlet;

import com.example.DatabaseConnection;
import com.example.dao.DistritoDAO;
import com.example.dao.RamoDAO;
import com.example.dao.UtilizadorDAO;
import com.example.dao.FornecedorDAO;
import com.example.dao.LocalidadeDAO;
import com.example.model.Distrito;
import com.example.model.Ramo;
import com.example.model.Utilizador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/registoFornecedor")
public class RegistoFornecedorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // GET: carrega distritos e ramos para popular o dropdown
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            DistritoDAO distDAO = new DistritoDAO(conn);
            RamoDAO     ramoDAO = new RamoDAO(conn);

            List<Distrito> distritos = distDAO.listarTodos();
            List<Ramo>     ramos      = ramoDAO.listarTodos();

            req.setAttribute("distritos", distritos);
            req.setAttribute("ramos",     ramos);
            // envia para o JSP de registo (view)
            req.getRequestDispatcher("/RegistoFornecedor.jsp")
               .forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                           "Erro ao carregar dados do formulário de fornecedor.");
        }
    }

    // POST: processa o registo de um novo fornecedor
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1) lê campos do formulário
        String nome        = req.getParameter("nome");
        String email       = req.getParameter("email");
        String telefone    = req.getParameter("telefone");
        String morada      = req.getParameter("morada");
        String pwd         = req.getParameter("password");
        String codPostal   = req.getParameter("cod_postal");
        String localidade  = req.getParameter("localidade");
        int    codDistrito = Integer.parseInt(req.getParameter("cod_distrito"));

        String ramoSel    = req.getParameter("id_ramo");   // id existente ou "novo"
        String novoRamo   = req.getParameter("novo_ramo"); // se o usuário escolheu criar um ramo

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 2) cria Localidade se ainda não existir
            LocalidadeDAO locDAO = new LocalidadeDAO(conn);
            if (!locDAO.existeCodigoPostal(codPostal)) {
                // inserir(nome da localidade, código postal, código distrito)
                locDAO.inserir(localidade, codPostal, codDistrito);
            }

            // 3) determina id_ramo (reaproveita ou insere novo)
            RamoDAO ramoDAO = new RamoDAO(conn);
            int idRamo;
            if ("novo".equals(ramoSel)) {
                idRamo = ramoDAO.inserirNovo(novoRamo.trim());
            } else {
                idRamo = Integer.parseInt(ramoSel);
            }

            // 4) insere o Utilizador
            Utilizador u = new Utilizador();
            u.setNome(nome);
            u.setEmail(email);
            u.setTelefone(telefone);
            u.setMorada(morada);
            u.setCodPostal(codPostal);
            u.setPwd(pwd);
            u.setDataRegisto(new Timestamp(System.currentTimeMillis()));
            u.setAtivo(true);

            UtilizadorDAO userDAO = new UtilizadorDAO(conn);
            int idUser = userDAO.create(u);

            // 5) insere o Fornecedor ligando ao user e ao ramo
            FornecedorDAO fornDAO = new FornecedorDAO(conn);
            fornDAO.createFornecedor(idUser, idRamo);

            conn.commit();

            // redireciona para o servlet de login com flag de sucesso
            resp.sendRedirect(req.getContextPath() + "/LoginServlet?sucesso=true");
        } catch (Exception e) {
            e.printStackTrace();
            // em caso de erro volta para o próprio servlet (GET) com flag de erro
            resp.sendRedirect(req.getContextPath() + "/registoFornecedor?erro=true");
        }
    }
}

