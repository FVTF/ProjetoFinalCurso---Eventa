<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.example.model.ServicoFornecedor, com.example.model.Servico" %>
<%
    ServicoFornecedor sf = (ServicoFornecedor) request.getAttribute("sf");
    java.util.List<Servico> lista = (java.util.List<Servico>) request.getAttribute("servicoList");
%>
<!DOCTYPE html>
<html lang="pt">
<head><meta charset="UTF-8" /><title>Editar Serviço</title><link rel="stylesheet" href="styleindex.css"/></head>
<body>
  <h1>Editar Serviço</h1>
  <form action="EditarServicoFornecedorServlet" method="post">
    <input type="hidden" name="id_servico_fornecedor" value="<%= sf.getIdServicoFornecedor() %>"/>
    <select name="id_servico" required>
      <c:forEach var="s" items="${servicoList}">
        <option value="${s.idServico}" ${s.idServico == sf.idServico ? 'selected' : ''}>${s.descricao}</option>
      </c:forEach>
    </select><br/>
    <input type="number" step="0.01" name="preco" value="<%= sf.getPreco() %>" required/><br/>
    <button type="submit" class="btn">Atualizar</button>
  </form>
</body>
</html>