<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.example.model.Utilizador" %>
<%
    Utilizador user = (Utilizador) session.getAttribute("utilizador");
    if (user == null) {
        response.sendRedirect("LoginServlet");
        return;
    }
%>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8" />
    <title>Minhas Reservas</title>
    <link rel="stylesheet" href="MeusServicos.css" />
</head>
<body>
    <!-- Header igual ao resto -->
    <header class="header">
        <a href="HomeFornecedorServlet" class="logo"><i class="fas fa-user"></i> Eventa</a>
        <nav class="navbar">
            <a href="HomeFornecedorServlet">Home</a>
            <a href="EditarRamo">Alterar Ramo</a>
            <a href="ReservasFornecedorServlet">Reservas</a>
            <a href="DashboardFornecedorServlet">Dashboard</a>
            <a href="PerfilFornecedorServlet">Perfil</a>
        </nav>
        <div class="icons">
            <div id="user-info" class="user-info">
                <i class="fas fa-user"></i>
                <span id="username"><%= user.getNome() %></span>
                <div id="dropdown" class="dropdown-content">
                    <a href="LogoutServlet">Logout</a>
                </div>
            </div>
            <div id="menu" class="fas fa-bars"></div>
        </div>
    </header>

    <div class="main-content" style="margin-top: 6rem;">
        <h1>Minhas Reservas</h1>
        <c:choose>
            <c:when test="${empty reservas}">
                <p>Não existem reservas ainda.</p>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>Cliente</th>
                            <th>Evento</th>
                            <th>Serviço</th>
                            <th>Espaço</th>
                            <th>Preço (€)</th>
                            <th>Data</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="r" items="${reservas}">
                            <tr>
                                <td>${r.nomeCliente}</td>
                                <td>${r.evento}</td>
                                <td>${r.descricaoServico}</td>
                                <td><c:out value="${r.descricaoEspaco}" default="-" /></td>
                                <td>${r.precoServico}</td>
                                <td>
                                    <fmt:formatDate value="${r.dataEvento}" pattern="dd/MM/yyyy HH:mm"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
