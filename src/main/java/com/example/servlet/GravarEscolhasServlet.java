package com.example.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/gravarEscolhas")
public class GravarEscolhasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Buscar dados do form
        String idEventoStr = request.getParameter("id_evento");
        String dataEvento = request.getParameter("dataEvento"); // Se quiseres guardar, podes!
        String numConvidadosStr = request.getParameter("numConvidados");
        String idEspacoStr = request.getParameter("id_espaco");

        // Validações rápidas
        if (idEventoStr == null || numConvidadosStr == null || idEspacoStr == null ||
            idEventoStr.isEmpty() || numConvidadosStr.isEmpty() || idEspacoStr.isEmpty()) {
            response.sendRedirect("AgendarEvento.jsp");
            return;
        }

        int idEvento = Integer.parseInt(idEventoStr);
        int numConvidados = Integer.parseInt(numConvidadosStr);
        int idEspaco = Integer.parseInt(idEspacoStr);

        // Guarda na sessão
        session.setAttribute("id_evento", idEvento);
        session.setAttribute("num_convidados", numConvidados);
        session.setAttribute("id_espaco", idEspaco);
        if (dataEvento != null) {
            session.setAttribute("data_evento", dataEvento);
        }

        // Redireciona para escolher serviços
        response.sendRedirect("ExplorarServicosServlet");
    }
}
