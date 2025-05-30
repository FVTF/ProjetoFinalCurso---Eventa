<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.model.Utilizador, com.example.model.Fornecedor, com.example.dao.FornecedorDAO, com.example.DatabaseConnection" %>
<%@ page import="java.util.List, com.example.model.Espaco" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    Utilizador user = (Utilizador) session.getAttribute("utilizador");
    if (user == null) {
        response.sendRedirect("LoginServlet");
        return;
    }

    FornecedorDAO dao = new FornecedorDAO(DatabaseConnection.getConnection());
    Fornecedor f = dao.findByUserId(user.getIdUser());
    if (f == null) {
        response.sendRedirect("HomeClienteServlet");
        return;
    }

    List<Espaco> espacos = (List<Espaco>) request.getAttribute("espacos");
%>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Meus Espaços</title>
    <link rel="stylesheet" href="https://unpkg.com/aos@next/dist/aos.css" />
    <link rel="stylesheet" href="https://unpkg.com/swiper/swiper-bundle.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" />
    <link rel="stylesheet" href="MeusEspacos.css" />
</head>
<body>
    <!-- Header -->
    <header class="header">
        <a href="HomeFornecedorServlet" class="logo" data-aos="zoom-in-left" data-aos-delay="150">
            <i class="fas fa-user"></i> Eventa
        </a>
        <nav class="navbar">
            <a href="HomeFornecedorServlet" data-aos="zoom-in-left" data-aos-delay="300">Home</a>
            <a href="EditarRamo" data-aos="zoom-in-left" data-aos-delay="450">Alterar Ramo</a>
            <a href="ReservasFornecedorServlet" data-aos="zoom-in-left" data-aos-delay="600">Reservas</a>
            <a href="DashboardFornecedorServlet" data-aos="zoom-in-left" data-aos-delay="900">Dashboard</a>
            <a href="PerfilFornecedorServlet" data-aos="zoom-in-left" data-aos-delay="1050">Perfil</a>
        </nav>
        <div class="icons">
            <div id="user-info" class="user-info" data-aos="zoom-in-left" data-aos-delay="1350">
                <i class="fas fa-user"></i>
                <span id="username"><%= user.getNome() %></span>
                <div id="dropdown" class="dropdown-content">
                    <a href="LogoutServlet">Logout</a>
                </div>
            </div>
            <div id="menu" class="fas fa-bars" data-aos="zoom-in-left" data-aos-delay="1500"></div>
        </div>
    </header>

    <!-- Conteúdo principal -->
    <div class="main-content" data-aos="fade-down">
        <h1>Meus Espaços</h1>
        <a href="CriarEspacoServlet" class="btn">Adicionar Espaço</a>

        <table>
            <thead>
                <tr>
                    <th>Descrição</th>
                    <th>Preço</th>
                    <th>Morada</th>
                    <th>Código Postal</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="e" items="${espacos}">
                    <tr>
                        <td>${e.descricao}</td>
                        <td>${e.preco}</td>
                        <td>${e.morada}</td>
                        <td>${e.codPostal}</td>
                        <td>
                            <!-- Editar -->
                            <a href="EditarEspacoServlet?id=${e.idEspaco}" class="btn">Editar</a>

                            <!-- Apagar -->
                            <form action="EliminarEspacoServlet" method="post" style="display:inline;" 
                                  onsubmit="return confirm('Tem a certeza que deseja apagar este espaço?');">
                                <input type="hidden" name="id" value="${e.idEspaco}" />
                                <button type="submit" class="btn">Apagar</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Scripts -->
    <script src="https://unpkg.com/aos@next/dist/aos.js"></script>
    <script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
    <script src="script.js"></script>
</body>
</html>
