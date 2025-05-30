package com.example.servlet;

import com.example.dao.ClienteServicoDAO;
import com.example.dao.OrcamentoDAO;
import com.example.model.ClienteServico;
import com.example.model.Orcamento;
import com.example.model.OrcamentoServico;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/ConfirmarOrcamentoServlet")
public class ConfirmarOrcamentoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        try (Connection conn = com.example.DatabaseConnection.getConnection()) {
            OrcamentoDAO orcamentoDAO = new OrcamentoDAO(conn);
            ClienteServicoDAO clienteServicoDAO = new ClienteServicoDAO(conn);

            Integer idCliente = (Integer) session.getAttribute("id_cliente");
            Integer idEvento = (Integer) session.getAttribute("id_evento");
            Integer numConvidados = (Integer) session.getAttribute("num_convidados");
            Double total = (Double) session.getAttribute("orcamento_total");
            @SuppressWarnings("unchecked")
            List<OrcamentoServico> listaServicos = (List<OrcamentoServico>) session.getAttribute("orcamento_itens");

            if (idCliente == null || idEvento == null || numConvidados == null || listaServicos == null || total == null) {
                response.sendRedirect("AgendarEventoServlet");
                return;
            }

            // 1. Inserir o orçamento
            Orcamento o = new Orcamento();
            o.setIdCliente(idCliente);
            o.setIdEvento(idEvento);
            o.setNumConvidados(numConvidados);
            o.setData(new java.util.Date());
            o.setDataAprovacao(null);
            o.setDesconto(0f);
            o.setValorOrcamento(total);

            int idOrcamento = orcamentoDAO.inserir(o);

            // 2. Inserir serviços do orçamento
            orcamentoDAO.inserirServicos(idOrcamento, listaServicos);

            // 3. Montar a lista de reservas (sem inserir aqui, só preparar)
            List<ClienteServico> reservas = new ArrayList<>();
            for (OrcamentoServico os : listaServicos) {
                ClienteServico reserva = new ClienteServico();

                // Espaço: se idEspaco > 0, regista, senão null
                reserva.setIdEspaco(os.getIdEspaco() > 0 ? os.getIdEspaco() : null);

                // Serviço: se idServico != -1, regista, senão null
                if (os.getIdServico() != -1) {
                    int idServicoFornecedor = buscarIdServicoFornecedor(conn, os.getIdServico(), os.getIdFornecedor());
                    reserva.setIdServicoFornecedor(idServicoFornecedor);
                } else {
                    reserva.setIdServicoFornecedor(null);
                }

                reserva.setIdCliente(idCliente);
                reserva.setData(new java.util.Date());
                reserva.setIdEvento(idEvento);

                reserva.setPrecoServico(os.getPrecoServico());
                reserva.setPrecoEspaco(os.getPrecoEspaco());

                reserva.setEstado("PENDENTE");
                reserva.setIdOrcamento(idOrcamento);

                reservas.add(reserva);
            }

            // 4. Insere TODAS as reservas via DAO (SÓ AQUI É FEITO O INSERT)
            clienteServicoDAO.inserirReservas(reservas, idOrcamento);

            // Limpeza da sessão e redirecionamento final
            session.setAttribute("idOrcamento", idOrcamento);
            session.setAttribute("total", total);

            session.removeAttribute("servicos_selecionados");
            session.removeAttribute("orcamento_total");
            session.removeAttribute("orcamento_itens");

            response.sendRedirect("MinhasReservasServlet");
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private int buscarIdServicoFornecedor(Connection conn, int idServico, int idFornecedor) throws SQLException {
        String sql = "SELECT id_servico_fornecedor FROM Servico_Fornecedor WHERE id_servico = ? AND id_fornecedor = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idServico);
            ps.setInt(2, idFornecedor);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id_servico_fornecedor");
        }
        throw new SQLException("Não existe Servico_Fornecedor para o serviço/fornecedor indicado.");
    }
}
