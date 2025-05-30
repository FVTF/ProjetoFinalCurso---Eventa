<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.model.Utilizador, com.example.model.Fornecedor, com.example.dao.FornecedorDAO, com.example.DatabaseConnection" %>
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

    // Validação: só permite fornecedores NÃO do ramo "Espaços para Eventos"
    String ramo = dao.getRamoDescricaoByFornecedorId(f.getIdFornecedor());
    if ("Espaços para Eventos".equals(ramo)) {
        response.sendRedirect("HomeCliente.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Meus Serviços</title>
    <link rel="stylesheet" href="https://unpkg.com/aos@next/dist/aos.css" />
    <link rel="stylesheet" href="https://unpkg.com/swiper/swiper-bundle.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" />
    <link rel="stylesheet" href="MeusServicos.css" />
</head>
<body>
    <!-- Header igual ao HomeFornecedor.jsp -->
    <header class="header">
        <a href="HomeFornecedorServlet" class="logo" data-aos="zoom-in-left" data-aos-delay="150">
            <i class="fas fa-user"></i> Eventa
        </a>
        <nav class="navbar">
            <a href="HomeFornecedor.Servlet" data-aos="zoom-in-left" data-aos-delay="300">Home</a>
            <a href="EditarRamo" data-aos="zoom-in-left" data-aos-delay="450">Alterar Ramo</a>
            <a href="ReservasFornecedorServlet" data-aos="zoom-in-left" data-aos-delay="600">Reservas</a>
            <a href="DashboardFornecedorServlet" data-aos="zoom-in-left" data-aos-delay="900">Dashboard</a>
            <a href="PerfilFornecedorServlet" data-aos="zoom-in-left" data-aos-delay="1050">Perfil</a>
        </nav>
        <div class="icons">
            <div id="user-info" class="user-info" data-aos="zoom-in-left" data-aos-delay="1350" style="position: relative;">
                <i class="fas fa-user"></i>
                <span id="username"><%= user.getNome() %></span>
                <div id="dropdown" class="dropdown-content" style="display: none; position: absolute; right: 0; top: 2.5rem; background: #fff; box-shadow: 0 2px 8px rgba(44,44,84,0.08); border-radius: 8px;">
                    <a href="LogoutServlet" style="color: #f45866; padding: 10px 22px; display: block; text-align: left; font-size: 1.15rem; border-radius: 8px;">Logout</a>
                </div>
            </div>
            <div id="menu" class="fas fa-bars" data-aos="zoom-in-left" data-aos-delay="1500"></div>
        </div>
    </header>

    <!-- Conteúdo principal -->
    <div class="main-content" data-aos="fade-down">
        <h1>Meus Serviços</h1>
        <a href="CriarServicoFornecedorServlet" class="btn">Adicionar Serviço</a>
        <table>
            <thead>
                <tr>
                    <th>Serviço</th>
                    <th>Preço (€)</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
			    <c:forEach var="s" items="${servicos}">
			        <tr>
			            <td>${s.descricaoServico}</td>
			            <td>${s.preco}</td>
			            <td>
			                <a href="EditarServicoFornecedorServlet?id=${s.idServicoFornecedor}" class="btn">Editar</a>
			                <form action="EliminarServicoFornecedorServlet" method="post" style="display:inline;"
			                      onsubmit="return confirm('Tem a certeza que deseja apagar este serviço?');">
			                    <input type="hidden" name="id" value="${s.idServicoFornecedor}" />
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
    <script>
        document.addEventListener('DOMContentLoaded', () => {
            AOS.init({ duration: 800, once: true });
            document.getElementById('menu').addEventListener('click', () => {
                document.querySelector('.navbar').classList.toggle('active');
            });
            document.getElementById('user-info').addEventListener('click', (e) => {
                e.stopPropagation();
                const dd = document.getElementById('dropdown');
                dd.style.display = (dd.style.display === 'block') ? 'none' : 'block';
            });
            document.body.addEventListener('click', () => {
                const dd = document.getElementById('dropdown');
                if (dd) dd.style.display = 'none';
            });
        });
    </script>
</body>
</html>
