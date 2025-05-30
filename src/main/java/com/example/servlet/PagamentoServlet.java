package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/PagamentoServlet")
public class PagamentoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Busca os dados enviados no formulário (via GET)
        String idOrcamentoStr = request.getParameter("idOrcamento");
        String totalStr = request.getParameter("total");

        if (idOrcamentoStr == null || totalStr == null) {
            response.sendRedirect("MinhasReservasServlet");
            return;
        }

        Integer idOrcamento;
        Double total;
        try {
            idOrcamento = Integer.parseInt(idOrcamentoStr);
            total = Double.parseDouble(totalStr);
        } catch (Exception e) {
            // Em caso de erro nos parâmetros, volta para reservas
            response.sendRedirect("MinhasReservasServlet");
            return;
        }

        // Gera entidade/referência/valor fictícios para mostrar (simulação)
        String entidade = "12345";
        String referencia = "999 888 777";

        // Passa tudo para o JSP
        request.setAttribute("idOrcamento", idOrcamento);
        request.setAttribute("total", total);
        request.setAttribute("entidade", entidade);
        request.setAttribute("referencia", referencia);

        // Vai para o JSP do pagamento
        request.getRequestDispatcher("Pagamento.jsp").forward(request, response);
    }
}

