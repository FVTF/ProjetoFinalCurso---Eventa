<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
  <meta charset="UTF-8"/>
  <title>Editar Ramo</title>
  <link rel="stylesheet" href="styleindex.css"/>
</head>
<body>
  
  <section class="about" style="padding-top:8rem;">
    <h1 class="heading"><span>Editar</span> Ramo</h1>
    <form action="EditarRamo" method="post" style="max-width:40rem; margin:2rem auto;">
      <div class="input-box">
        <select name="id_ramo" class="custom-select" required>
          <option value="">Escolha o novo ramo</option>
          <c:forEach var="r" items="${ramos}">
            <option value="${r[0]}">${r[1]}</option>
          </c:forEach>
        </select>
      </div>
      <button type="submit" class="btn">Guardar Alteração</button>
      <a href="HomeFornecedorServlet" class="btn">Cancelar</a>
    </form>
  </section>
</body>
</html>
