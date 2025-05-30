<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Painel Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav class="col-md-2 bg-light sidebar">
                <div class="sidebar-sticky pt-3">
                    <ul class="nav flex-column">
                        <li class="nav-item"><a class="nav-link active" href="?tab=dashboard">Dashboard</a></li>
                        <li class="nav-item"><a class="nav-link" href="?tab=users">Utilizadores</a></li>
                        <li class="nav-item"><a class="nav-link" href="?tab=suppliers">Fornecedores</a></li>
                        <li class="nav-item"><a class="nav-link" href="?tab=events">Eventos</a></li>
                        <li class="nav-item"><a class="nav-link" href="?tab=payments">Pagamentos</a></li>
                        <li class="nav-item"><a class="nav-link" href="?tab=reviews">Avaliações</a></li>
                    </ul>
                </div>
            </nav>

            <!-- Main Content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <c:choose>
                    <c:when test="${param.tab == 'users'}">
                        <%@ include file="admin-users.jsp" %>
                    </c:when>
                    <c:when test="${param.tab == 'suppliers'}">
                        <%@ include file="admin-suppliers.jsp" %>
                    </c:when>
                    <c:otherwise>
                        <h2>Bem-vindo, Admin!</h2>
                        <!-- Exibir estatísticas rápidas -->
                        <div class="row mt-4">
                            <div class="col-md-4">
                                <div class="card">
                                    <div class="card-body">
                                        <h5 class="card-title">Utilizadores Registados</h5>
                                        <p class="card-text">${totalUsers}</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="card">
                                    <div class="card-body">
                                        <h5 class="card-title">Pagamentos Pendentes</h5>
                                        <p class="card-text">${pendingPayments}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </main>
        </div>
    </div>
</body>
</html>