<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.model.Utilizador" %>
<%
    Utilizador utilizador = (Utilizador) session.getAttribute("utilizador");
    if (utilizador == null) {
        response.sendRedirect("LoginServlet");
        return;
    }
    String nome = utilizador.getNome();
%>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Home - Cliente</title>
    <link rel="stylesheet" href="https://unpkg.com/aos@next/dist/aos.css" />
    <link rel="stylesheet" href="https://unpkg.com/swiper/swiper-bundle.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" />
    <link rel="stylesheet" href="HomeCliente.css" />
</head>
<body>
    <!-- Cabeçalho comum -->
    <header class="header">
        <a href="HomeClienteServlet" class="logo" data-aos="zoom-in-left" data-aos-delay="150">
            <i class="fas fa-user"></i> Eventa
        </a>
        <nav class="navbar">
		    <a href="HomeClienteServlet" data-aos="zoom-in-left" data-aos-delay="300">Home</a>
		    <a href="agendarEvento" data-aos="zoom-in-left" data-aos-delay="450">Agendar Evento</a>
		    <a href="ExplorarServicosServlet" data-aos="zoom-in-left" data-aos-delay="600">Serviços</a>
		    <a href="MinhasReservasServlet" data-aos="zoom-in-left" data-aos-delay="900">Minhas Reservas</a>
		    <a href="PerfilClienteServlet" data-aos="zoom-in-left" data-aos-delay="1050">Perfil</a>
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

    <!-- Seção Home -->
    <section class="home" id="home">
        <div class="content" data-aos="fade-down">
            <h3>Bem-vindo, <%= nome %>!<br>Vamos planear o seu evento</h3>
            <a href="${pageContext.request.contextPath}/agendarEvento">Começar</a>
        </div>
    </section>
	
	<!-- sobre -->
    <section class="about" id="about">
        <h1 class="heading"><span>Sobre</span> nós</h1>
        <div class="row">
            <div class="content" data-aos="fade-up" data-aos-delay="150">
                <h3>O que podemos ajudar</h3>
                <p>Na Eventa, transformamos momentos especiais em memórias inesquecíveis. Especializados em planear e executar cerimônias personalizadas, cuidamos de cada detalhe para que o seu evento seja perfeito.</p>
                <p>Oferecemos serviços para casamentos, aniversários, eventos corporativos e celebrações temáticas, sempre com dedicação e excelência. A nossa equipa entende as suas necessidades e cria experiências únicas que refletem a sua personalidade e desejos.</p>
                <a href="#" class="btn">Ler mais</a>
            </div>
            <div class="image" data-aos="fade-down" data-aos-delay="300">
                <img src="images/about.jpg" alt="Sobre nós">
            </div>
        </div>
    </section>

    <!-- serviços -->
    <section class="services" id="services">
        <h1 class="heading">serviços</h1>
        <div class="box-container">
            <div class="box" data-aos="zoom-in" data-aos-delay="150">
                <img src="images/service1.png" alt="">
                <h3>Fotos e Multimédia</h3>
                <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Fugiat, non?</p>
                <a href="detalhesFotosMultimedia.html" class="btn">Ver mais</a>
            </div>
            <div class="box" data-aos="zoom-in" data-aos-delay="300">
                <img src="images/service2.png" alt="">
                <h3>Decorações</h3>
                <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Fugiat, non?</p>
                <a href="detalhesDecoracao.html" class="btn">Ver mais</a>
            </div>
            <div class="box" data-aos="zoom-in" data-aos-delay="450">
                <img src="images/service3.png" alt="">
                <h3>Local Perfeito</h3>
                <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Esse, minima?</p>
                <a href="detalhesEspaços.html" class="btn">Ver mais</a>
            </div>
        </div>
    </section>

    <!-- galeria -->
    <section class="gallery" id="gallery">
        <h1 class="heading">Galeria</h1>
        <div class="swiper gallery-slider" data-aos="fade-up">
            <div class="swiper-wrapper">
                <div class="swiper-slide"><img src="images/gallery-1.jpg" alt=""></div>
                <div class="swiper-slide"><img src="images/gallery-2.jpg" alt=""></div>
                <div class="swiper-slide"><img src="images/gallery-3.jpg" alt=""></div>
                <div class="swiper-slide"><img src="images/gallery-4.jpg" alt=""></div>
                <div class="swiper-slide"><img src="images/gallery-5.jpg" alt=""></div>
                <div class="swiper-slide"><img src="images/gallery-6.jpg" alt=""></div>
                <div class="swiper-slide"><img src="images/gallery-7.jpg" alt=""></div>
            </div>
        </div>
    </section>

    
    <!-- contacte-nos -->
    <section class="contact" id="contact">
        <h1 class="heading">contacte-nos</h1>
        <form action="" data-aos="zoom">
            <div class="inputBox">
                <input type="text" placeholder="Nome" data-aos="fade-up">
                <input type="email" placeholder="Email" data-aos="fade-up">
            </div>
            <textarea placeholder="Escreva aqui a sua mensagem..." cols="30" rows="10" data-aos="fade-up"></textarea>
            <a href="#" class="btn">enviar mensagem</a>
        </form>
    </section>

    <!-- footer -->
    <section class="footer">
        <div class="box-container">
            <div class="box" data-aos="fade-up">
                <h3><i class="fas fa-user"></i> planeamento</h3>
                <p>O planeamento de cerimónias é a arte de coordenar...</p>
            </div>
            <div class="box" data-aos="fade-up">
                <h3>Serviços</h3>
                <a href="#"><i class="fas fa-chevron-right"></i> Fotos e Multimédia</a>
                <a href="#"><i class="fas fa-chevron-right"></i> Decorações</a>
                <a href="#"><i class="fas fa-chevron-right"></i> Local Perfeito</a>
            </div>
            <div class="box" data-aos="fade-up">
                <h3>Contacte-nos</h3>
                <a href="#"><i class="fas fa-phone"></i> 226927510</a>
                <a href="#"><i class="fas fa-phone"></i> 965537022</a>
                <a href="#"><i class="fas fa-envelope"></i> info@eventa.com</a>
                <a href="#"><i class="fas fa-map"></i> Rua da Lusófona 123, Porto</a>
            </div>
            <div class="box" data-aos="fade-up">
                <h3>Segue-nos</h3>
                <a href="#"><i class="fab fa-facebook-f"></i> facebook</a>
                <a href="#"><i class="fab fa-twitter"></i> twitter</a>
                <a href="#"><i class="fab fa-instagram"></i> instagram</a>
            </div>
        </div>
    </section>
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
