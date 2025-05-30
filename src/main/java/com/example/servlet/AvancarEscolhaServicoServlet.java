package com.example.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/AvancarEscolhaServico")
public class AvancarEscolhaServicoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String[] servicosSelecionados = request.getParameterValues("idServicoFornecedor");
        HttpSession session = request.getSession();

        // Se nada selecionado, volta à página de seleção
        if (servicosSelecionados == null || servicosSelecionados.length == 0) {
            response.sendRedirect("ExplorarServicosServlet");
            return;
        }

        List<Integer> lista = new ArrayList<>();
        for (String s : servicosSelecionados) {
            try {
                lista.add(Integer.parseInt(s));
            } catch (NumberFormatException ex) {
                // Ignora valores inválidos
            }
        }

        // Guarda na sessão para ser usado no orçamento
        session.setAttribute("servicos_selecionados", lista);

        // Redireciona para a página de orçamento
        response.sendRedirect("OrcamentoServlet");
    }
}
