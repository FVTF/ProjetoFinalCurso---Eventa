package com.example.servlet;

import com.example.dao.ClienteServicoDAO;
import com.example.model.Utilizador;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/MinhasReservasServlet")
public class MinhasReservasServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Utilizador utilizador = (session != null) ? (Utilizador) session.getAttribute("utilizador") : null;
        if (utilizador == null) {
            response.sendRedirect("LoginServlet");
            return;
        }
        int idUser = utilizador.getIdUser();

        try (Connection conn = com.example.DatabaseConnection.getConnection()) {
            int idCliente = buscarIdClientePorUser(conn, idUser);

            List<PedidoDTO> pedidos = listarTodosPedidosCliente(conn, idCliente);

            request.setAttribute("reservas", pedidos);
            request.getRequestDispatcher("MinhasReservas.jsp").forward(request, response);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private int buscarIdClientePorUser(Connection conn, int idUser) throws SQLException {
        String sql = "SELECT id_cliente FROM Cliente WHERE id_user = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id_cliente");
        }
        throw new SQLException("Cliente não encontrado.");
    }

    // LISTA por id_orcamento (um pedido = um orçamento confirmado)
    private List<PedidoDTO> listarTodosPedidosCliente(Connection conn, int idCliente) throws SQLException {
        List<PedidoDTO> lista = new ArrayList<>();
        String sql = "SELECT o.id_orcamento, o.data, e.descricao AS nome_evento, o.valor_orcamento, " +
                "   (SELECT TOP 1 es.descricao FROM Cliente_Servico cs2 " +
                "    JOIN Espaco es ON cs2.id_espaco = es.id_espaco " +
                "    WHERE cs2.id_orcamento = o.id_orcamento AND cs2.id_espaco IS NOT NULL) AS nome_espaco " +
                "FROM Orcamento o " +
                "JOIN Cliente_Servico cs ON o.id_orcamento = cs.id_orcamento " +
                "JOIN Evento e ON o.id_evento = e.id_evento " +
                "WHERE o.id_cliente = ? " +
                "GROUP BY o.id_orcamento, o.data, e.descricao, o.valor_orcamento " +
                "ORDER BY o.data DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PedidoDTO dto = new PedidoDTO();
                int idOrcamento = rs.getInt("id_orcamento");
                dto.setId(idOrcamento);
                dto.setData(rs.getTimestamp("data"));
                dto.setEvento(rs.getString("nome_evento"));
                dto.setEspaco(rs.getString("nome_espaco"));
                dto.setTotal(rs.getDouble("valor_orcamento"));  // NOVO CAMPO!
                // Estado global do pedido (lógica correta)
                dto.setEstado(ClienteServicoDAO.getEstadoGlobalDoOrcamento(conn, idOrcamento));
                lista.add(dto);
            }
        }
        return lista;
    }

    // DTO para pedido (agora com "espaco" e "total")
    public static class PedidoDTO {
        private int id;
        private java.util.Date data;
        private String estado, evento, espaco;
        private double total;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public java.util.Date getData() { return data; }
        public void setData(java.util.Date data) { this.data = data; }
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
        public String getEvento() { return evento; }
        public void setEvento(String evento) { this.evento = evento; }
        public String getEspaco() { return espaco; }
        public void setEspaco(String espaco) { this.espaco = espaco; }
        public double getTotal() { return total; }
        public void setTotal(double total) { this.total = total; }
    }
}

