<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.example.model.Utilizador"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
<title>Minhas Reservas</title>
<link rel="stylesheet" href="HomeCliente.css" />
<style>
.reservas-list {
	max-width: 950px;
	margin: 60px auto 0 auto;
}

.reserva-card {
	border: 1px solid #e3e3e3;
	border-radius: 10px;
	margin-bottom: 28px;
	background: #fafaff;
	box-shadow: 0 0 6px #eee;
	padding: 24px 28px;
	position: relative;
}

.reserva-header {
	font-weight: bold;
	font-size: 1.1em;
	margin-bottom: 7px;
}

.reserva-info {
	margin-bottom: 3px;
}

.estado-PAGO {
	color: green;
	font-weight: bold;
}

.estado-PENDENTE {
	color: orange;
	font-weight: bold;
}

.estado-CANCELADO {
	color: #d11;
	font-weight: bold;
}
</style>
</head>
<body>
	<header class="header">
		<a href="HomeClienteServlet" class="logo"> <i class="fas fa-user"></i>
			Eventa
		</a>
		<nav class="navbar">
			<a href="HomeClienteServlet">Home</a> <a href="agendarEvento">Agendar
				Evento</a> <a href="ExplorarServicosServlet">Serviços</a> <a
				href="MinhasReservasServlet">Minhas Reservas</a> <a
				href="PerfilClienteServlet">Perfil</a>
		</nav>
		<div class="icons">
			<div id="user-info" class="user-info">
				<i class="fas fa-user"></i> <span id="username"><%= nome %></span>
				<div id="dropdown" class="dropdown-content">
					<a href="LogoutServlet">Logout</a>
				</div>
			</div>
			<div id="menu" class="fas fa-bars"></div>
		</div>
	</header>

	<div class="reservas-list">
		<h2 style="margin-bottom: 30px;">Minhas Reservas</h2>
		<c:choose>
			<c:when test="${empty reservas}">
				<p>Não existem reservas.</p>
			</c:when>
			<c:otherwise>
				<c:forEach var="r" items="${reservas}">
					<div class="reserva-card">
						<div class="reserva-header">
							Pedido #${r.id} – <strong>${r.evento}</strong> <span
								class="estado-${r.estado}">${r.estado}</span>
						</div>
						<div class="reserva-info">
							<strong>Data do pedido:</strong>
							<fmt:formatDate value="${r.data}" pattern="dd/MM/yyyy HH:mm" />
						</div>
						<div class="reserva-info">
							<strong>Espaço:</strong> ${r.espaco}
						</div>
						<c:if test="${r.estado == 'ACEITE'}">
							<form action="PagamentoServlet" method="get">
								<input type="hidden" name="idOrcamento" value="${r.id}" /> <input
									type="hidden" name="total" value="${r.total}" />
								<button type="submit" class="btn">Pagar</button>
							</form>
						</c:if>
					</div>
				</c:forEach>

			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>

