<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="<c:url value='/LoginRegistoStyle.css' />">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

<div class="container">
    <!-- Imagem lateral -->
    <div class="cover">
        <img src="images/gallery-2.jpg" alt="Imagem de login">
        <div class="cover-text">
            <span class="text-1">Junte-se a nós nesta aventura</span>
            <span class="text-2">Vamos trabalhar juntos!</span>
        </div>
    </div>

    <!-- Formulário -->
    <div class="forms">
        <!-- Mensagem de sucesso ao registar -->
        <c:if test="${param.sucesso == 'true'}">
            <div class="message success">
                Conta criada com sucesso! Faça login.
            </div>
        </c:if>
        <!-- Mensagem de logout -->
        <c:if test="${param.logout == 'true'}">
            <div class="message success">
                Sessão terminada com sucesso.
            </div>
        </c:if>
        <!-- Erro de credenciais -->
        <c:if test="${param.erro == 'credenciais'}">
            <div class="message error">
                Email ou password incorretos.
            </div>
        </c:if>
        <!-- Erro técnico -->
        <c:if test="${param.erro == 'tecnico'}">
            <div class="message error">
                Ocorreu um erro técnico. Tente novamente mais tarde.
            </div>
        </c:if>

        <div class="form-content">
            <div class="login-form">
                <div class="title">Entrar</div>
                <form action="LoginServlet" method="post">
                    <div class="input-boxes">
                        <div class="input-box">
                            <i class="fas fa-envelope"></i>
                            <input type="text" name="email" placeholder="Escreva o seu e-mail" required>
                        </div>
                        <div class="input-box">
                            <i class="fas fa-lock"></i>
                            <input type="password" name="password" placeholder="Insira a sua palavra-passe" required>
                        </div>
                        <div class="text">
                            <a href="#">Esqueceu-se da palavra-passe?</a>
                        </div>
                        <div class="button input-box">
                            <button type="submit" class="login-btn">Entrar</button>
                        </div>
                        <div class="text sign-up-text">
                            Ainda não tem conta? <a href="registo">Registe-se</a>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

</body>
</html>