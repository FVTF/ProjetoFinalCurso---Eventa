<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.example.model.Servico, com.example.model.Utilizador, com.example.model.Fornecedor, com.example.dao.FornecedorDAO, com.example.DatabaseConnection" %>
<%
    // Recuperar user da sessão (igual ao MeusServicos.jsp)
    Utilizador user = (Utilizador) session.getAttribute("utilizador");
    if (user == null) {
        response.sendRedirect("LoginServlet");
        return;
    }
    FornecedorDAO dao = new FornecedorDAO(DatabaseConnection.getConnection());
    Fornecedor f = dao.findByUserId(user.getIdUser());
    if (f == null) {
        response.sendRedirect("HomeCliente.jsp");
        return;
    }
    java.util.List<Servico> lista = (java.util.List<Servico>) request.getAttribute("servicoList");
%>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8" />
    <title>Adicionar Serviço</title>
    <link rel="stylesheet" href="https://unpkg.com/aos@next/dist/aos.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
    <link rel="stylesheet" href="CriarServico.css"/>
</head>
<body>
    <!-- Header igual ao MeusServicos.jsp -->
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
            <div id="user-info" class="user-info" style="position: relative; cursor: pointer;">
                <i class="fas fa-user"></i>
                <span id="username"><%= user.getNome() %></span>
                <div id="dropdown" class="dropdown-content">
                    <a href="LogoutServlet">Logout</a>
                </div>
            </div>
            <div id="menu" class="fas fa-bars" data-aos="zoom-in-left" data-aos-delay="1500"></div>
        </div>
    </header>
    
    <div class="main-content" data-aos="fade-down">
      <h1>Adicionar Serviço</h1>
      <form action="CriarServicoFornecedorServlet" method="post" class="service-form">
        <label for="nome_servico">Nome do Serviço:</label>
		<input type="text" name="nome_servico" id="nome_servico" required placeholder="Ex: Fotografias Casamento" />
        
        <label for="preco">Preço (€)</label>
        <input type="number" step="0.01" name="preco" id="preco" placeholder="Preço" required/>
        
        <button type="submit" class="btn">Guardar</button>
        <a href="MeusServicosServlet" class="btn btn-secondary">Cancelar</a>
      </form>
    </div>
    
    <script src="https://unpkg.com/aos@next/dist/aos.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            AOS.init({ duration: 800, once: true });
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
