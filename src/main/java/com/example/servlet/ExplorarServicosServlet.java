package com.example.servlet;

import com.example.dao.RamoDAO;
import com.example.dao.ServicoFornecedorDAO;
import com.example.model.Ramo;
import com.example.model.ServicoFornecedor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ExplorarServicosServlet")
public class ExplorarServicosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Connection conn = com.example.DatabaseConnection.getConnection()) {
            RamoDAO ramoDAO = new RamoDAO(conn);
            ServicoFornecedorDAO servicoFornecedorDAO = new ServicoFornecedorDAO(conn);

            List<Ramo> ramos = ramoDAO.listarTodos();

            // Descobre o ramo selecionado (por query string)
            String paramRamo = request.getParameter("ramoAtual");
            int ramoSelecionadoId = 0;
            if (paramRamo != null && !paramRamo.isEmpty()) {
                ramoSelecionadoId = Integer.parseInt(paramRamo);
            } else if (!ramos.isEmpty()) {
                ramoSelecionadoId = ramos.get(0).getIdRamo(); // Primeiro ramo
            }

            List<ServicoFornecedor> servicos = servicoFornecedorDAO.listarPorRamo(ramoSelecionadoId);

            // Proteção contra o warning de cast não verificado
            HttpSession session = request.getSession();
            List<Integer> servicosSelecionados = new ArrayList<>();
            Object objSelecionados = session.getAttribute("servicos_selecionados");
            if (objSelecionados instanceof List<?>) {
                for (Object o : (List<?>) objSelecionados) {
                    if (o instanceof Integer) {
                        servicosSelecionados.add((Integer) o);
                    } else if (o instanceof String) {
                        try { servicosSelecionados.add(Integer.parseInt((String) o)); } catch (Exception ignore) {}
                    }
                }
            }

            request.setAttribute("ramos", ramos);
            request.setAttribute("ramoSelecionado", ramoSelecionadoId);
            request.setAttribute("servicos", servicos);
            request.setAttribute("servicosSelecionados", servicosSelecionados);

            request.getRequestDispatcher("ExplorarServicos.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Grava os serviços selecionados (pode ser múltiplo)
        String[] selecionados = request.getParameterValues("idServicoFornecedor");
        List<Integer> servicosSelecionados = new ArrayList<>();
        if (selecionados != null) {
            for (String s : selecionados) {
                try {
                    servicosSelecionados.add(Integer.parseInt(s));
                } catch (NumberFormatException ignore) { }
            }
        }
        session.setAttribute("servicos_selecionados", servicosSelecionados);

        // Mantém o ramo selecionado ao recarregar
        String ramoAtual = request.getParameter("ramoAtual");
        if (ramoAtual != null && !ramoAtual.isEmpty()) {
            response.sendRedirect("ExplorarServicosServlet?ramoAtual=" + ramoAtual);
        } else {
            response.sendRedirect("ExplorarServicosServlet");
        }
    }
}
