<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Registo de Utilizador</title>

    <!-- CSS personalizado -->
    <link rel="stylesheet" href="<c:url value='/LoginRegistoStyle.css' />">

    <!-- Font Awesome para ícones -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

<div class="container">
    <!-- Formulário -->
    <div class="forms">
        <div class="form-content">
            <div class="signup-form">
                <div class="title">Registo</div>

                <form action="<c:url value='/registo' />" method="post" onsubmit="return validateForm()">
                    <div class="input-boxes">
                        <div class="input-box">
                            <i class="fas fa-user"></i>
                            <input type="text" id="nome" name="nome" placeholder="Escreva o seu nome" required>
                        </div>
                        <div class="input-box">
                            <i class="fas fa-envelope"></i>
                            <input type="email" id="email" name="email" placeholder="Escreva o seu e-mail" required>
                        </div>
                        <div class="input-box">
                            <i class="fas fa-lock"></i>
                            <input type="password" id="password" name="password" placeholder="Insira a sua palavra-passe" required>
                        </div>
                        <div class="input-box">
                            <i class="fas fa-lock"></i>
                            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirme a sua palavra-passe" required>
                        </div>
                        <div class="input-box">
                            <i class="fas fa-phone"></i>
                            <input type="text" name="telefone" placeholder="Telefone">
                        </div>
                        <div class="input-box">
                            <i class="fas fa-location-dot"></i>
                            <input type="text" name="morada" placeholder="Morada">
                        </div>
                        <div class="input-box">
                            <i class="fas fa-city"></i>
                            <input type="text" name="localidade" placeholder="Localidade (Concelho)" required>
                        </div>
                        <div class="input-box">
                            <i class="fas fa-map-pin"></i>
                            <input type="text" name="cod_postal" placeholder="Código Postal" required>
                        </div>
                        <div class="input-box">
                            <i class="fas fa-location-arrow"></i>
                            <div class="custom-select-wrapper">
                                <select name="cod_distrito" class="custom-select" required>
                                    <option value="">Escolha o distrito</option>
                                    <c:forEach var="d" items="${distritos}">
                                        <option value="${d.codDistrito}">${d.nome}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="text">
                            <a href="<c:url value='/registoFornecedor' />">Quer registar-se como fornecedor?</a>
                        </div>
                        <div class="button input-box">
                            <button type="submit" class="registar-btn">Registar</button>
                        </div>
                        <div class="text sign-up-text" style="margin-top:15px; text-align:center;">
                            Já tem uma conta? <a href="<c:url value='/LoginServlet' />">Entrar</a>
                        </div>
                    </div>
                </form>

            </div>
        </div>
    </div>

    <!-- Imagem lateral -->
    <div class="cover">
        <img src="<c:url value='/images/gallery-1.jpg' />" alt="Imagem de fundo lateral">
    </div>
</div>

<script>
  function validateForm() {
    const pw  = document.getElementById('password').value;
    const cpw = document.getElementById('confirmPassword').value;
    if (pw !== cpw) {
      alert('As palavras-passe não coincidem.');
      return false;
    }
    return true;
  }
</script>

</body>
</html>
