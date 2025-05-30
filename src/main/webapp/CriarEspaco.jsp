<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.model.Utilizador, com.example.model.Fornecedor, com.example.dao.FornecedorDAO, com.example.DatabaseConnection" %>
<%
    // Verifica sessão de utilizador
    Utilizador user = (Utilizador) session.getAttribute("utilizador");
    if (user == null) {
        response.sendRedirect("LoginServlet");
        return;
    }
    // Carrega dados do fornecedor (já com código postal)
    FornecedorDAO fDao = new FornecedorDAO(DatabaseConnection.getConnection());
    Fornecedor f = fDao.findByUserId(user.getIdUser());
    if (f == null) {
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
    <title>Adicionar Espaço</title>
    <link rel="stylesheet" href="https://unpkg.com/aos@next/dist/aos.css" />
    <link rel="stylesheet" href="https://unpkg.com/swiper/swiper-bundle.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" />
    <link rel="stylesheet" href="CriarEspaco.css" />
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

    <!-- Conteúdo principal -->
    <div class="main-content" data-aos="fade-down">
        <h1>Adicionar Novo Espaço</h1>
        <form action="CriarEspacoServlet" method="post" class="form-espaco" id="form-espaco">
            <div class="form-group">
                <label for="descricao">Descrição</label>
                <input type="text" id="descricao" name="descricao" required />
            </div>
            <div class="form-group">
                <label for="preco">Preço (€)</label>
                <input type="number" id="preco" name="preco" step="0.01" min="0" required />
            </div>
            <div class="form-group">
                <label for="morada">Morada</label>
                <input type="text" id="morada" name="morada" required />
            </div>
            <div class="form-group">
                <label for="cod_postal">Código Postal</label>
                <input type="text" id="cod_postal" name="cod_postal"
                       value="<%= (f.getCodPostal() != null ? f.getCodPostal() : "") %>"
                       readonly required />
            </div>
            <div class="form-group">
                <label for="latitude">Latitude</label>
                <input type="number" step="any" id="latitude" name="latitude" required />
            </div>
            <div class="form-group">
                <label for="longitude">Longitude</label>
                <input type="number" step="any" id="longitude" name="longitude" required />
            </div>
            <button type="submit" class="btn">Guardar Espaço</button>
        </form>
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
        document.getElementById('user-info').addEventListener('click', () => {
            const dd = document.getElementById('dropdown');
            dd.style.display = dd.style.display === 'block' ? 'none' : 'block';
        });

        // Impede submissão se código postal estiver vazio
        document.getElementById('form-espaco').addEventListener('submit', function(e) {
            var cp = document.getElementById('cod_postal').value;
            if (!cp || cp.trim() === "") {
                alert("Não é possível criar o espaço porque não existe código postal associado ao seu perfil.");
                e.preventDefault();
                return false;
            }
        });
    });
    </script>
</body>
</html>
