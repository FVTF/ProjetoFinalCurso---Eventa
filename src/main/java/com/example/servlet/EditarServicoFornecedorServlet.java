package com.example.servlet;

import com.example.DatabaseConnection;
import com.example.dao.ServicoDAO;
import com.example.dao.ServicoFornecedorDAO;
import com.example.model.Servico;
import com.example.model.ServicoFornecedor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;


@WebServlet("/EditarServicoFornecedorServlet")
public class EditarServicoFornecedorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        try (Connection conn = DatabaseConnection.getConnection()) {
            ServicoFornecedorDAO sfDao = new ServicoFornecedorDAO(conn);
            ServicoFornecedor sf = sfDao.findById(id);

            // Pega apenas o serviço associado (podes permitir listar todos, mas não recomendo trocar de serviço)
            ServicoDAO servicoDAO = new ServicoDAO(conn);
            Servico servico = null;
            if (sf != null) {
                servico = servicoDAO.findById(sf.getIdServico());
            }
            request.setAttribute("sf", sf);
            request.setAttribute("servico", servico);
            request.getRequestDispatcher("EditarServico.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServicoFornecedor sf = new ServicoFornecedor();
        sf.setIdServicoFornecedor(Integer.parseInt(request.getParameter("id_servico_fornecedor")));
        sf.setIdServico(Integer.parseInt(request.getParameter("id_servico"))); // deve vir escondido no form!
        sf.setPreco(Double.parseDouble(request.getParameter("preco")));

        try (Connection conn = DatabaseConnection.getConnection()) {
            new ServicoFornecedorDAO(conn).update(sf);
            response.sendRedirect("MeusServicosServlet");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

