<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.example.model.Utilizador"%>
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
<title>Dashboard Fornecedor</title>
<link rel="stylesheet" href="DashboardFornecedorLook.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" />
</head>
<body>
	<header class="header">
		<a href="HomeFornecedorServlet" class="logo"><i
			class="fas fa-user"></i> Eventa</a>
		<nav class="navbar">
			<a href="HomeFornecedorServlet">Home</a> <a href="EditarRamo">Alterar
				Ramo</a> <a href="ReservasFornecedorServlet">Reservas</a> <a
				href="DashboardFornecedorServlet">Dashboard</a> <a
				href="PerfilFornecedorServlet">Perfil</a>
		</nav>
		<div class="icons">
			<div id="user-info" class="user-info">
				<i class="fas fa-user"></i> <span id="username"><%= user.getNome() %></span>
				<div id="dropdown" class="dropdown-content">
					<a href="LogoutServlet">Logout</a>
				</div>
			</div>
			<div id="menu" class="fas fa-bars"></div>
		</div>
	</header>

	<div class="main-content" data-aos="fade-down">
		<h1>Dashboard</h1>
		<section>
			<h2>Resumo de Reservas</h2>
			<ul>
				<li>Total de reservas: <strong>${totalReservas}</strong></li>
				<li>Pendentes: <strong>${pendentes}</strong></li>
				<li>Aceites: <strong>${aceites}</strong></li>
				<li>Recusadas: <strong>${recusadas}</strong></li>
			</ul>
		</section>
		<section>
			<h2>Últimas Reservas Recebidas</h2>
			<table>
				<thead>
					<tr>
						<th>Cliente</th>
						<th>Serviço</th>
						<th>Data</th>
						<th>Estado</th>
						<th>Ações</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${empty ultimasReservas}">
							<tr>
								<td colspan="5" style="text-align: center;">Não existem
									reservas pendentes.</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="r" items="${ultimasReservas}">
								<tr>
									<td>${r.nomeCliente}</td>
									<td>${r.descricaoServico}</td>
									<td><fmt:formatDate value="${r.data}"
											pattern="dd/MM/yyyy HH:mm" /></td>
									<td><span class="estado-${r.estado}"> ${r.estado} </span>
									</td>
									<td><c:choose>
											<c:when test="${r.estado == 'PENDENTE'}">
												<form action="AceitarReservaFornecedorServlet" method="post"
													style="display: inline;">
													<input type="hidden" name="idReserva"
														value="${r.idClienteServico}">
													<button type="submit" class="btn-acao btn-aceitar">Aceitar</button>
												</form>
												<form action="RecusarReservaFornecedorServlet" method="post"
													style="display: inline;">
													<input type="hidden" name="idReserva"
														value="${r.idClienteServico}">
													<button type="submit" class="btn-acao btn-recusar">Recusar</button>
												</form>
											</c:when>
											<c:when
												test="${r.estado == 'ACEITE'}">
												<i class="fas fa-check-circle" style="color: #31c45a;"
													title="Aceite"></i>
											</c:when>
											<c:when
												test="${r.estado == 'RECUSADA' || r.estado == 'CANCELADA'}">
												<i class="fas fa-times-circle" style="color: #ff4343;"
													title="Recusada"></i>
											</c:when>
										</c:choose></td>

								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</section>
		<section>
			<h2>Os meus serviços</h2>
			<ul>
				<c:forEach var="servico" items="${servicos}">
					<li>${servico.descricaoServico}</li>
				</c:forEach>
			</ul>
		</section>
	</div>
	<script src="https://unpkg.com/aos@next/dist/aos.js"></script>
	<script>
        document.addEventListener('DOMContentLoaded', function() {
            if (typeof AOS !== 'undefined') {
                AOS.init({ duration: 800, once: true });
            }
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
