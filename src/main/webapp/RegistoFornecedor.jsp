<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
  <meta charset="UTF-8">
  <title>Registo de Fornecedor</title>
  <link rel="stylesheet" href="<c:url value='/RegistoFornecedorStyle.css' />">
  <link rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

<div class="container">
  <div class="forms">
    <div class="form-content">
      <div class="signup-form">
        <div class="title">Registo de Fornecedor</div>
        <form action="<c:url value='/registoFornecedor' />" method="post" onsubmit="return validateFormFornecedor()">
          <div class="input-boxes">

            <!-- Nome da Empresa -->
            <div class="input-box">
              <i class="fas fa-building"></i>
              <input type="text" name="nome" placeholder="Nome da Empresa" required>
            </div>

            <!-- E-mail -->
            <div class="input-box">
              <i class="fas fa-envelope"></i>
              <input type="email" name="email" placeholder="E-mail" required>
            </div>

            <!-- Palavra-passe -->
            <div class="input-box">
              <i class="fas fa-lock"></i>
              <input type="password" id="password" name="password"
                     placeholder="Palavra-passe" required>
            </div>
            <!-- Confirmação da palavra-passe -->
            <div class="input-box">
              <i class="fas fa-lock"></i>
              <input type="password" id="confirmPassword" name="confirmPassword"
                     placeholder="Confirme a palavra-passe" required>
            </div>

            <!-- Telefone -->
            <div class="input-box">
              <i class="fas fa-phone"></i>
              <input type="text" name="telefone" placeholder="Telefone">
            </div>

            <!-- Morada -->
            <div class="input-box">
              <i class="fas fa-location-dot"></i>
              <input type="text" name="morada" placeholder="Morada" required>
            </div>

            <!-- Localidade (Concelho) livre -->
            <div class="input-box">
              <i class="fas fa-city"></i>
              <input type="text" name="localidade" placeholder="Localidade (Concelho)" required>
            </div>

            <!-- Código Postal -->
            <div class="input-box">
              <i class="fas fa-map-pin"></i>
              <input type="text" name="cod_postal" placeholder="Código Postal" required>
            </div>

            <!-- Distrito (dropdown da BD) -->
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

            <!-- Ramo (dropdown + opção de novo) -->
            <div class="input-box">
              <i class="fas fa-layer-group"></i>
              <div class="custom-select-wrapper">
                <select name="id_ramo" class="custom-select" required>
                  <option value="">Escolha o ramo</option>
                  <c:forEach var="ramo" items="${ramos}">
                    <option value="${ramo.idRamo}">${ramo.descricao}</option>
                  </c:forEach>
                  <option value="novo">+ Adicionar novo ramo</option>
                </select>
              </div>
            </div>

            <!-- Novo ramo (aparece só se escolher “novo”) -->
            <div class="input-box" id="novoRamoWrapper" style="display:none;">
              <i class="fas fa-plus"></i>
              <input type="text" name="novo_ramo" id="novo_ramo"
                     placeholder="Escreva o novo ramo">
            </div>

            <!-- Botão Registar -->
            <div class="button input-box">
              <button type="submit" class="registar-btn">Registar</button>
            </div>

            <!-- Link para login -->
            <div class="text sign-up-text" style="margin-top: 15px; text-align: center;">
              Já tem uma conta? 
              <a href="<c:url value='/LoginServlet' />">Entrar</a>
            </div>

          </div>
        </form>
      </div>
    </div>
  </div>

  <!-- Imagem lateral -->
  <div class="cover">
    <img src="images/gallery-5.jpg" alt="Imagem de fundo">
  </div>
</div>

<script>
  function validateFormFornecedor() {
    const pw  = document.getElementById('password').value;
    const cpw = document.getElementById('confirmPassword').value;
    if (pw !== cpw) {
      alert('As palavras-passe não coincidem.');
      return false;
    }
    return true;
  }
  document.querySelector('select[name="id_ramo"]').addEventListener('change', function() {
    const show = this.value === 'novo';
    document.getElementById('novoRamoWrapper').style.display = show ? 'flex' : 'none';
    document.getElementById('novo_ramo').required = show;
  });
</script>

</body>
</html>


