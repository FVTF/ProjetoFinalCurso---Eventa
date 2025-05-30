<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.example.model.Utilizador" %>
<%
    Utilizador utilizador = (Utilizador) session.getAttribute("utilizador");
    if (utilizador == null) {
        response.sendRedirect("LoginServlet");
        return;
    }
    String nome = utilizador.getNome();
    java.util.List<Integer> servicosSelecionados = (java.util.List<Integer>) session.getAttribute("servicos_selecionados");
%>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Explorar Serviços</title>
    <link rel="stylesheet" href="ExplorarServicos.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
<header class="header">
    <a href="HomeCliente.jsp" class="logo" data-aos="zoom-in-left" data-aos-delay="150">
        <i class="fas fa-user"></i> Eventa
    </a>
    <nav class="navbar">
        <a href="HomeClienteServlet">Home</a>
        <a href="agendarEvento">Agendar Evento</a>
        <a href="ExplorarServicosServlet">Explorar Serviços</a>
        <a href="MinhasReservasServlet">Minhas Reservas</a>
        <a href="PerfilClienteServlet">Perfil</a>
    </nav>
    <div class="icons">
        <div id="user-info" class="user-info" data-aos="zoom-in-left" data-aos-delay="1350">
            <i class="fas fa-user"></i>
            <span id="username"><%= nome %></span>
            <div id="dropdown" class="dropdown-content">
                <a href="LogoutServlet">Logout</a>
            </div>
        </div>
        <div id="menu" class="fas fa-bars" data-aos="zoom-in-left" data-aos-delay="1500"></div>
    </div>
</header>

<div class="container">
    <!-- Sidebar esquerda -->
    <aside class="filters sidebar">
        <h2>Serviços Selecionados</h2>
        <div id="selected-service" class="selected-service">
            <c:choose>
                <c:when test="${not empty servicosSelecionados}">
                    <c:forEach var="s" items="${servicos}">
                        <c:if test="${servicosSelecionados.contains(s.idServicoFornecedor)}">
                            <span>- ${s.descricaoServico} (${s.preco}€)</span><br/>
                        </c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    Nenhum serviço selecionado
                </c:otherwise>
            </c:choose>
        </div>
        <div class="sidebar-footer">
            <form action="AvancarEscolhaServico" method="post">
                <c:forEach var="s" items="${servicos}">
                    <c:if test="${servicosSelecionados != null && servicosSelecionados.contains(s.idServicoFornecedor)}">
                        <input type="hidden" name="idServicoFornecedor" value="${s.idServicoFornecedor}" />
                    </c:if>
                </c:forEach>
                <button type="submit" class="btn"
                    <c:if test="${empty servicosSelecionados}">disabled</c:if>>
                    Seguinte
                </button>
            </form>
        </div>
    </aside>
    
    <!-- Main content: seleção de ramo e serviços -->
    <main class="services main-content">
        <form id="selecionar-form" action="ExplorarServicosServlet" method="post">
            <div style="margin-bottom: 2rem;">
                <select name="ramoAtual" onchange="this.form.submit()" class="ramo-dropdown">
                    <c:forEach var="ramo" items="${ramos}">
                        <option value="${ramo.idRamo}" <c:if test="${ramo.idRamo == ramoSelecionado}">selected</c:if>>
                            ${ramo.descricao}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="services-container">
                <c:forEach var="s" items="${servicos}">
                    <div class="service-item">
                        <img src="<c:choose>
                            <c:when test="${not empty s.imagemUrl}">${s.imagemUrl}</c:when>
                            <c:otherwise>images/placeholder.jpg</c:otherwise>
                        </c:choose>" alt="${s.descricaoServico}">
                        <div class="content">
                            <h3>${s.descricaoServico}</h3>
                            <p class="price">Preço: ${s.preco}€</p>
                            <label>
                                <input 
                                    type="checkbox"
                                    name="idServicoFornecedor"
                                    value="${s.idServicoFornecedor}"
                                    <c:if test="${servicosSelecionados != null && servicosSelecionados.contains(s.idServicoFornecedor)}">checked</c:if>
                                    onchange="document.getElementById('selecionar-form').submit();"
                                >
                                Selecionar
                            </label>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </form>
    </main>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('menu').addEventListener('click', function() {
            document.querySelector('.navbar').classList.toggle('active');
        });
        document.getElementById('user-info').addEventListener('click', function(event) {
            event.stopPropagation();
            var dd = document.getElementById('dropdown');
            dd.style.display = (dd.style.display === 'block') ? 'none' : 'block';
        });
        document.body.addEventListener('click', function() {
            var dd = document.getElementById('dropdown');
            if (dd) dd.style.display = 'none';
        });
    });
</script>
</body>
</html>
