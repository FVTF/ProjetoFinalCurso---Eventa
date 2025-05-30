package com.example.servlet;

import com.example.dao.ServicoFornecedorDAO;
import com.example.dao.RamoDAO;
import com.example.dao.EspacoDAO;
import com.example.model.OrcamentoServico;
import com.example.model.ServicoFornecedor;
import com.example.model.Espaco;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/OrcamentoServlet")
public class OrcamentoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Set<Integer> RAMOS_POR_PESSOA = Set.of(
        1,2,3,5,11,12,15,19,20
    );

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        Integer idEvento = (Integer) session.getAttribute("id_evento");
        Integer numConvidados = (Integer) session.getAttribute("num_convidados");
        Integer idEspaco = (Integer) session.getAttribute("id_espaco");
        @SuppressWarnings("unchecked")
        List<Integer> servicosSelecionados = (List<Integer>) session.getAttribute("servicos_selecionados");

        if (idEvento == null || numConvidados == null || idEspaco == null || servicosSelecionados == null) {
            response.sendRedirect("AgendarEvento.jsp");
            return;
        }

        try (Connection conn = com.example.DatabaseConnection.getConnection()) {
            ServicoFornecedorDAO servicoDAO = new ServicoFornecedorDAO(conn);
            RamoDAO ramoDAO = new RamoDAO(conn);

            EspacoDAO espacoDAO = new EspacoDAO(conn);
            Espaco espaco = espacoDAO.findById(idEspaco);
            double precoEspaco = (espaco != null) ? espaco.getPreco() : 0.0;
            request.setAttribute("espaco", espaco);

            double total = precoEspaco;
            List<OrcamentoServico> listaServicos = new ArrayList<>();

            // Adiciona o espaço ao orçamento (como linha especial)
            if (espaco != null) {
                OrcamentoServico espacoOrc = new OrcamentoServico();
                espacoOrc.setIdServico(-1); // Marcação de espaço
                espacoOrc.setIdEspaco(idEspaco);
                espacoOrc.setPrecoServico(0.0);
                espacoOrc.setPrecoEspaco(precoEspaco);
                espacoOrc.setNomeServico(espaco.getDescricao());
                espacoOrc.setIdFornecedor(-1);
                listaServicos.add(espacoOrc);
            }

            // Elimina da lista de serviços selecionados qualquer entrada correspondente ao espaço
            List<Integer> servicosSelecionadosSemEspaco = new ArrayList<>();
            for (Integer idServicoFornecedor : servicosSelecionados) {
                ServicoFornecedor servico = servicoDAO.findById(idServicoFornecedor);
                if (servico != null && servico.getIdEspaco() != null && servico.getIdEspaco().equals(idEspaco)) {
                    continue; // Não adicionar
                }
                servicosSelecionadosSemEspaco.add(idServicoFornecedor);
            }

            // Adiciona os outros serviços ao orçamento
            for (Integer idServicoFornecedor : servicosSelecionadosSemEspaco) {
                ServicoFornecedor servico = servicoDAO.findById(idServicoFornecedor);
                int idRamo = ramoDAO.procurarIdPorServico(servico.getIdServico());
                double precoServico;
                boolean porPessoa = RAMOS_POR_PESSOA.contains(idRamo);

                precoServico = porPessoa ? servico.getPreco() * numConvidados : servico.getPreco();

                OrcamentoServico os = new OrcamentoServico();
                os.setIdServico(servico.getIdServico());
                os.setPrecoServico(precoServico);
                os.setIdEspaco(idEspaco);
                os.setPrecoEspaco(0.0);
                os.setNomeServico(servico.getDescricaoServico());
                os.setIdFornecedor(servico.getIdFornecedor());
                listaServicos.add(os);

                total += precoServico;
            }

            session.setAttribute("orcamento_itens", listaServicos);
            session.setAttribute("orcamento_total", total);

            request.setAttribute("itens", listaServicos);
            request.setAttribute("total", total);
            request.setAttribute("precoEspaco", precoEspaco);

            // User header (opcional)
            String nome = null;
            Object utilizador = session.getAttribute("utilizador");
            if (utilizador != null) {
                try {
                    nome = (String) utilizador.getClass().getMethod("getNome").invoke(utilizador);
                } catch (Exception ignore) {}
            }
            request.setAttribute("nome", nome);

            request.getRequestDispatcher("Orcamento.jsp").forward(request, response);

        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
}
