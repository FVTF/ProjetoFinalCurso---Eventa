/*
@WebServlet("/calcularOrcamento")
public class CalcularOrcamentoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Abra a conexão em try-with-resources para garantir que fecha
        try (Connection conn = DatabaseConnection.getConnection()) {
            // 1) Lê parâmetros do formulário
            int idCliente = (int) request.getSession().getAttribute("idCliente");
            int idEvento  = Integer.parseInt(request.getParameter("id_evento"));
            int nConvid   = Integer.parseInt(request.getParameter("numConvidados"));
            int idEspaco  = Integer.parseInt(request.getParameter("id_espaco"));

            // 2) Busca preço do espaço
            double precoEsp = new EspacoDAO(conn).findById(idEspaco).getPreco();

            // 3) Processa serviços (pode ser null se não selecionou nenhum)
            String[] servs = request.getParameterValues("id_servico_fornecedor");
            List<OrcamentoServico> itens = new ArrayList<>();
            double somaServ = 0;
            if (servs != null) {
                for (String sId : servs) {
                    int idSF = Integer.parseInt(sId);
                    double p = new ServicoFornecedorDAO(conn).findById(idSF).getPreco();

                    OrcamentoServico os = new OrcamentoServico();
                    os.setIdServico(idSF);
                    os.setIdEspaco(idEspaco);
                    os.setPrecoServico(p);
                    os.setPrecoEspaco(precoEsp);
                    itens.add(os);

                    somaServ += p;
                }
            }

            // 4) Calcula total
            double total = precoEsp + somaServ;

            // 5) Monta e grava o Orçamento
            Orcamento o = new Orcamento();
            o.setIdCliente(idCliente);
            o.setIdEvento(idEvento);
            o.setNumConvidados(nConvid);
            o.setData(new Date());      // grava data de criação do orçamento
            o.setDesconto(0f);          // sem desconto por enquanto
            o.setValorOrcamento(total);

            OrcamentoDAO orcDAO = new OrcamentoDAO(conn);
            int idOrc = orcDAO.inserir(o);
            orcDAO.inserirServicos(idOrc, itens);

            // 6) Envia para a JSP de resultado
            request.setAttribute("total", total);
            request.setAttribute("itens", itens);
            request.getRequestDispatcher("/WEB-INF/orcamentoResult.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Erro ao calcular orçamento", e);
        }
    }
}
*/
