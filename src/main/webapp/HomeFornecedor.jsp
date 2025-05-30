<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.model.Utilizador, com.example.model.Fornecedor" %>
<%
    Utilizador user = (Utilizador) session.getAttribute("utilizador");
    Fornecedor f = (Fornecedor) request.getAttribute("fornecedor");
    if (user == null || f == null) {
        response.sendRedirect("LoginServlet");
        return;
    }
%>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Home - Fornecedor</title>
    <link rel="stylesheet" href="https://unpkg.com/aos@next/dist/aos.css" />
    <link rel="stylesheet" href="https://unpkg.com/swiper/swiper-bundle.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" />
    <link rel="stylesheet" href="HomeFornecedor.css" />
</head>
<body>
    <!-- Header comum -->
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

    <!-- Seção Home do Fornecedor -->
    <section class="home" id="home">
        <div class="content" data-aos="fade-down">
            <h3>Bem-vindo, <%= user.getNome() %>!</h3>
            <p>O seu ramo de serviço: <strong><%= f.getRamoDescricao() %></strong></p>

            <div class="buttons" style="margin-top:1.5rem;">
                <!-- Sempre permitir alterar o ramo -->
                <a href="EditarRamo" class="btn">Alterar Ramo</a>

                <!-- Se o ramo for "Espaços para Eventos", mostrar GERIR MEUS ESPAÇOS -->
                <% if ("Espaços para Eventos".equals(f.getRamoDescricao())) { %>
                    <a href="MeusEspacosServlet" class="btn">Gerir Meus Espaços</a>
                <% } else { %>
                    <!-- Para outros ramos, um link para detalhes do serviço -->
                    <a href="MeusServicosServlet" class="btn">Detalhes do Serviço</a>
                <% } %>
            </div>
        </div>
    </section>

    <!-- Scripts -->
    <script src="https://unpkg.com/aos@next/dist/aos.js"></script>
    <script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
    <script src="script.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', () => {
            AOS.init({ duration: 800, once: true });
            document.getElementById('menu').addEventListener('click', () => {
                document.querySelector('.navbar').classList.toggle('active');
            });
            document.getElementById('user-info').addEventListener('click', () => {
                const dd = document.getElementById('dropdown');
                dd.style.display = dd.style.display === 'block' ? 'none' : 'block';
            });
        });
    </script>
</body>
</html>
