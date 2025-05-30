<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="pt">
<head>
<meta charset="UTF-8">
<title>Orçamento</title>
<link rel="stylesheet" href="ExplorarServicos.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
<style>
.btn-cancel {
	background: #f5f5f5;
	color: #f45866;
	border: 1px solid #f45866;
	margin-left: 15px;
	transition: background 0.2s;
}

.btn-cancel:hover {
	background: #f45866;
	color: #fff;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-bottom: 1rem;
}

th, td {
	padding: .7rem 1.2rem;
	border-bottom: 1px solid #ececec;
	text-align: left;
}

th {
	background: #faf8f8;
	color: #404068;
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
				Evento</a> <a href="ExplorarServicosServlet">Explorar Serviços</a> <a
				href="MinhasReservasServlet">Minhas Reservas</a> <a
				href="PerfilClienteServlet">Perfil</a>
		</nav>
		<div class="icons">
			<div id="user-info" class="user-info">
				<i class="fas fa-user"></i> <span id="username">${nome}</span>
				<div id="dropdown" class="dropdown-content">
					<a href="LogoutServlet">Logout</a>
				</div>
			</div>
			<div id="menu" class="fas fa-bars"></div>
		</div>
	</header>

	<div class="container" style="margin-top: 80px; max-width: 800px">
		<h2>Orçamento</h2>
		<p>
			<b>Total (sem desconto):</b> €${total}
		</p>
		<h3>Detalhes</h3>
		<table>
			<tr>
				<th>Serviço</th>
				<th>Preço</th>
			</tr>
			<!-- Espaço selecionado -->
			<c:if test="${espaco != null}">
				<tr>
					<td><c:out value="${espaco.descricao}" /></td>
					<td>€<c:out value="${precoEspaco}" />
					</td>
				</tr>
			</c:if>

			<!-- Serviços escolhidos (excluindo espaço) -->
			<c:forEach var="it" items="${itens}">
				<c:if test="${it.idServico != -1}">
					<tr>
						<td><c:choose>
								<c:when test="${not empty it.nomeServico}">
									<c:out value="${it.nomeServico}" />
								</c:when>
								<c:when test="${not empty nomesServicos[it.idServico]}">
									<c:out value="${nomesServicos[it.idServico]}" />
								</c:when>
								<c:otherwise>
									<c:out value="${it.idServico}" />
								</c:otherwise>
							</c:choose></td>
						<td>€<c:out value="${it.precoServico}" /></td>
					</tr>
				</c:if>
			</c:forEach>
		</table>
		<div style="margin-top: 2rem;">
			<form action="ConfirmarOrcamentoServlet" method="post"
				style="display: inline;">
				<button type="submit" class="btn">Confirmar pedido</button>
			</form>
			<form action="CancelarOrcamentoServlet" method="post"
				style="display: inline;">
				<button type="submit" class="btn"
					style="background: #aaa; color: #fff;">Cancelar</button>
			</form>
		</div>
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
