package com.example.servlet;

import com.example.DatabaseConnection;
import com.example.dao.FornecedorDAO;
import com.example.dao.ServicoDAO;
import com.example.dao.ServicoFornecedorDAO;
import com.example.model.Fornecedor;
import com.example.model.Servico;
import com.example.model.ServicoFornecedor;
import com.example.model.Utilizador;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/CriarServicoFornecedorServlet")
public class CriarServicoFornecedorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("CriarServico.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Utilizador user = (Utilizador) session.getAttribute("utilizador");
        if (user == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        String nomeServico = request.getParameter("nome_servico");
        double preco = Double.parseDouble(request.getParameter("preco"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            FornecedorDAO fornecedorDAO = new FornecedorDAO(conn);
            Fornecedor fornecedor = fornecedorDAO.findByUserId(user.getIdUser());
            if (fornecedor == null) {
                response.sendRedirect("HomeCliente.jsp");
                return;
            }

            // 1. Verifica se serviço já existe para este ramo
            ServicoDAO servicoDAO = new ServicoDAO(conn);
            Servico servico = servicoDAO.findByDescricaoAndRamo(nomeServico, fornecedor.getIdRamo());
            int idServico;
            if (servico == null) {
                // Não existe, cria novo
                servico = new Servico();
                servico.setDescricao(nomeServico);
                servico.setIdRamo(fornecedor.getIdRamo());
                idServico = servicoDAO.inserirServico(servico);
            } else {
                idServico = servico.getIdServico();
            }

            // 2. Cria na Servico_Fornecedor
            ServicoFornecedor sf = new ServicoFornecedor();
            sf.setIdServico(idServico);
            sf.setIdFornecedor(fornecedor.getIdFornecedor());
            sf.setPreco(preco);

            ServicoFornecedorDAO sfDAO = new ServicoFornecedorDAO(conn);
            sfDAO.create(sf);

            response.sendRedirect("MeusServicosServlet");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
