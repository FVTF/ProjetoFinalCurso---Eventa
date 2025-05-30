<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.example.model.Utilizador" %>
<%
    Utilizador utilizador = (Utilizador) session.getAttribute("utilizador");
    if (utilizador == null) {
        response.sendRedirect("LoginServlet");
        return;
    }
    String nome = utilizador.getNome();
%>

<fmt:setLocale value="pt_PT" />

<!DOCTYPE html>
<html lang="pt">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Eventa – Mapa de Espaços</title>
  <link rel="stylesheet" href="<c:url value='/HomeCliente.css'/>"/>
  <link rel="stylesheet" href="https://unpkg.com/aos@next/dist/aos.css"/>
  <link rel="stylesheet" href="https://unpkg.com/swiper/swiper-bundle.min.css"/>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
  <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.3/dist/leaflet.css"/>
  <link rel="stylesheet" href="<c:url value='/detalhesEspacos2.css'/>"/>
</head>
<body>
  <header class="header">
      <a href="HomeClienteServlet" class="logo" data-aos="zoom-in-left" data-aos-delay="150">
          <i class="fas fa-user"></i> Eventa
      </a>
      <nav class="navbar">
          <a href="HomeClienteServlet" data-aos="zoom-in-left" data-aos-delay="300">Home</a>
          <a href="agendarEvento" data-aos="zoom-in-left" data-aos-delay="450">Agendar Evento</a>
          <a href="ExplorarServicosServlet" data-aos="zoom-in-left" data-aos-delay="600">Explorar Serviços</a>
          <a href="MinhasReservasServlet" data-aos="zoom-in-left" data-aos-delay="900">As Minhas Reservas</a>
          <a href="PerfilClienteServlet" data-aos="zoom-in-left" data-aos-delay="1050">Perfil</a>
      </nav>
      <div class="icons">
            <div id="user-info" class="user-info" data-aos="zoom-in-left" data-aos-delay="1350" style="position: relative;">
                <i class="fas fa-user"></i>
			    <span id="username"><%= nome %></span>
			    <div id="dropdown" class="dropdown-content" style="display: none; position: absolute; right: 0; top: 2.5rem; background: #fff; box-shadow: 0 2px 8px rgba(44,44,84,0.08); border-radius: 8px;">
			        <a href="LogoutServlet" style="color: #f45866; padding: 10px 22px; display: block; text-align: left; font-size: 1.15rem; border-radius: 8px;">Logout</a>
			    </div>
			</div>
            <div id="menu" class="fas fa-bars" data-aos="zoom-in-left" data-aos-delay="1500"></div>
        </div>
  </header>

  <form action="<c:url value='/gravarEscolhas'/>" method="post">
    <div class="container">
      <aside class="sidebar" data-aos="fade-right">
        <h2>Planeamento – Fase I</h2>
        <ul>
          <li>
            <label for="event-type">Tipo de Evento:</label>
            <select id="event-type" name="id_evento" required>
              <c:choose>
                <c:when test="${not empty eventos}">
                  <c:forEach var="e" items="${eventos}">
                    <option value="${e.idEvento}">${e.descricao}</option>
                  </c:forEach>
                </c:when>
                <c:otherwise>
                  <option value="">Sem eventos disponíveis</option>
                </c:otherwise>
              </c:choose>
            </select>
          </li>
          <li>
            <label for="event-date">Data do Evento:</label>
            <input type="date" id="event-date" name="dataEvento" required/>
          </li>
          <li>
            <label for="num-guests">Número de Convidados:</label>
            <input type="number" id="num-guests" name="numConvidados" min="1" required/>
          </li>
          <li>
            <label for="espaco">Espaço:</label>
            <select id="espaco" name="id_espaco" required>
              <option value="" disabled selected>— Seleciona no Mapa —</option>
              <!-- O JS vai preencher as opções -->
            </select>
          </li>
        </ul>
         <button type="submit" class="btn" data-aos="zoom-in-up">
      		Gravar Escolhas
    	</button>
      </aside>


      <main class="main-content" data-aos="fade-left">
        <div class="map-header">
          <h2>Mapa de Espaços</h2>
          <div>
            <input type="range" id="radius" min="1" max="50" value="10" oninput="updateRadius()"/>
            <span id="radiusValue">10 km</span>
          </div>
        </div>
        <div id="map"></div>
      </main>
    </div>
    
  </form>

  <!-- JS Externos -->
  <script src="https://unpkg.com/aos@next/dist/aos.js"></script>
  <script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/js/all.min.js"></script>
  <script src="https://unpkg.com/leaflet@1.9.3/dist/leaflet.js"></script>
  
  <!-- Passar os espaços para o JavaScript -->
  <script>
  // ---- Serialização dos espaços (mantido igual) ----
  var todosEspacos = [
    <c:forEach var="s" items="${espacos}" varStatus="loop">
      {
        id: "<c:out value='${s.idEspaco}'/>",
        descricao: "<c:out value='${s.descricao}'/>",
        preco: <c:out value='${s.preco != null ? s.preco : 0}'/>,
        latitude: <c:out value='${s.latitude}'/>,
        longitude: <c:out value='${s.longitude}'/>
      }<c:if test="${!loop.last}">,</c:if>
    </c:forEach>
  ];

  // ---- Funções utilitárias para truncar e escapar ----
  function truncateText(text, maxLength) {
    return text.length > maxLength ? text.substring(0, maxLength) + "..." : text;
  }
  function escapeHtml(str) {
    if (!str) return "";
    return str.replace(/&/g, "&amp;")
              .replace(/</g, "&lt;")
              .replace(/>/g, "&gt;")
              .replace(/\"/g, "&quot;")
              .replace(/'/g, "&#39;");
  }

  document.addEventListener("DOMContentLoaded", function () {
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

    // Menu mobile (opcional)
    var menu = document.getElementById('menu');
    if (menu) {
      menu.onclick = function() {
        document.querySelector('.navbar').classList.toggle('active');
      };
    }

    // Função para calcular a distância entre dois pontos em km
    function haversine(lat1, lon1, lat2, lon2) {
      var R = 6371; // km
      var dLat = (lat2 - lat1) * Math.PI / 180;
      var dLon = (lon2 - lon1) * Math.PI / 180;
      var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
              Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
              Math.sin(dLon/2) * Math.sin(dLon/2);
      var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
      return R * c;
    }

    // Círculo visual do raio
    window.raioCircle = null;
    function desenharCirculo(lat, lng, raioKm) {
      if (window.raioCircle) {
        window.map.removeLayer(window.raioCircle);
      }
      window.raioCircle = L.circle([lat, lng], {
        color: '#f45866',
        fillColor: '#f4586677',
        fillOpacity: 0.25,
        radius: raioKm * 1000 // metros
      }).addTo(window.map);
    }

    // --- AQUI está a função atualizada ---
    function atualizarEspacos(lat, lng, raioKm) {
      var selectEspaco = document.getElementById("espaco");
      selectEspaco.innerHTML = "";

      // Filtrar espaços dentro do raio
      var espacosFiltrados = todosEspacos.filter(function (espaco) {
        if (
          espaco.latitude == null ||
          espaco.longitude == null ||
          isNaN(espaco.latitude) ||
          isNaN(espaco.longitude)
        ) return false;
        var dist = haversine(lat, lng, espaco.latitude, espaco.longitude);
        return dist <= raioKm;
      });

      // Adicionar opções ao select
      if (espacosFiltrados.length > 0) {
        var optionPadrao = document.createElement("option");
        optionPadrao.disabled = true;
        optionPadrao.selected = true;
        optionPadrao.textContent = "— Seleciona no Mapa —";
        selectEspaco.appendChild(optionPadrao);

        espacosFiltrados.forEach(function (espaco) {
          var opt = document.createElement("option");
          var precoVal = (typeof espaco.preco === 'number' && !isNaN(espaco.preco)) ? espaco.preco.toFixed(2) : "0.00";
          // Descrição COMPLETA no select + preço
          opt.textContent = (espaco.descricao ? espaco.descricao : "(Sem descrição)") + " (€" + precoVal + ")";
          opt.value = espaco.id;
          selectEspaco.appendChild(opt);
        });
      } else {
        var opt = document.createElement("option");
        opt.value = "";
        opt.textContent = "Sem espaços disponíveis";
        selectEspaco.appendChild(opt);
      }

      // Atualizar marcadores no mapa
      if (typeof window.espacosMarkers !== "undefined") {
        window.espacosMarkers.forEach(function (m) { m.remove(); });
      }
      window.espacosMarkers = [];

      espacosFiltrados.forEach(function (espaco) {
        // Descrição TRUNCADA (50 chars) no popup
        var desc = espaco.descricao ? espaco.descricao : "(Sem descrição)";
        var descCorta = truncateText(desc, 50);
        var precoTxt = (typeof espaco.preco === 'number' && !isNaN(espaco.preco)) ? "€" + espaco.preco.toFixed(2) : "Preço indisponível";
        // Escape em ambos para garantir integridade do HTML
        var popupHtml = "<strong>" + escapeHtml(descCorta) + "</strong><br>" + escapeHtml(precoTxt);
        var marker = L.marker([espaco.latitude, espaco.longitude])
          .addTo(window.map)
          .bindPopup(popupHtml);
        window.espacosMarkers.push(marker);
      });

      // Atualizar círculo do raio visual
      desenharCirculo(lat, lng, raioKm);
    }

    // Inicializar o mapa
    var map = L.map('map').setView([39.5, -8.0], 7);
    window.map = map;
    window.espacosMarkers = [];

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution:'© OpenStreetMap contributors'
    }).addTo(map);

    // Obter raio selecionado (slider)
    function getRaioKm() {
      var range = document.getElementById("radius");
      return range ? parseInt(range.value) : 10;
    }
    function updateRadius() {
      document.getElementById("radiusValue").textContent = getRaioKm() + " km";
      if (window.currentPos) {
        atualizarEspacos(window.currentPos.lat, window.currentPos.lng, getRaioKm());
      }
    }
    document.getElementById("radius").addEventListener("input", updateRadius);

    // Marcar a localização do utilizador ou localização default
    function iniciarMarcador(lat, lng) {
      // Se já houver marcador, remove
      if (window.userMarker) map.removeLayer(window.userMarker);

      // Adiciona marcador arrastável
      window.userMarker = L.marker([lat, lng], { draggable: true })
        .addTo(map)
        .bindPopup("Você está aqui. Arraste para outra localização.");

      window.currentPos = { lat, lng };
      map.setView([lat, lng], 11);

      atualizarEspacos(lat, lng, getRaioKm());
      desenharCirculo(lat, lng, getRaioKm());

      // Quando arrastar marcador, atualizar lista de espaços e círculo
      window.userMarker.on('dragend', function (e) {
        var pos = e.target.getLatLng();
        window.currentPos = { lat: pos.lat, lng: pos.lng };
        atualizarEspacos(pos.lat, pos.lng, getRaioKm());
        desenharCirculo(pos.lat, pos.lng, getRaioKm());
      });
    }

    // Tenta obter localização do utilizador
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(function (pos) {
        iniciarMarcador(pos.coords.latitude, pos.coords.longitude);
      }, function () {
        // Se não der, usa centro de Portugal
        iniciarMarcador(39.5, -8.0);
      });
    } else {
      iniciarMarcador(39.5, -8.0);
    }
  });
</script>
  <script src="<c:url value='/script.js'/>"></script>
</body>
</html>

