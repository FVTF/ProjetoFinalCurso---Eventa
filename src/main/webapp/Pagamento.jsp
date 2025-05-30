<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Pagamento (Simulação)</title>
    <link rel="stylesheet" href="ExplorarServicos.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <style>
        .payment-container {
            max-width: 600px;
            margin: 80px auto;
            padding: 20px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .simulacao-msg {
            background: #f5f5ff;
            border: 1px solid #404068;
            border-radius: 5px;
            padding: 10px 15px;
            margin-bottom: 20px;
            color: #404068;
            font-weight: bold;
        }
        .btn-pay {
            background: #404068;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            margin-top: 20px;
        }
        .btn-pay:hover {
            background: #303058;
        }
        /* Modal */
        #modal-pagamento {
            position: fixed;
            top: 0; left: 0; right: 0; bottom: 0;
            width: 100vw; height: 100vh;
            background: rgba(0,0,0,0.5);
            display: none;
            align-items: center; justify-content: center;
            z-index: 99999;
        }
        #modal-pagamento .modal-content {
            background: white;
            border-radius: 8px;
            padding: 35px 40px;
            max-width: 350px;
            text-align: center;
            color: #222;
            box-shadow: 0 2px 16px #2222;
        }
        @media (max-width: 600px) {
            .payment-container { margin: 25px 8px; }
        }
    </style>
</head>
<body>
<header class="header">
    <a href="HomeCliente.jsp" class="logo">
        <i class="fas fa-user"></i> Eventa
    </a>
    <nav class="navbar">
        <a href="HomeClienteServlet">Home</a>
        <a href="agendarEvento">Agendar Evento</a>
        <a href="ExplorarServicosServlet">Explorar Serviços</a>
        <a href="MinhasReservasServlet">Minhas Reservas</a>
        <a href="PerfilClienteServlet">Perfil</a>
    </nav>
    <div class="icons">
        <div id="user-info" class="user-info">
            <i class="fas fa-user"></i>
            <span id="username">${nome}</span>
            <div id="dropdown" class="dropdown-content">
                <a href="LogoutServlet">Logout</a>
            </div>
        </div>
        <div id="menu" class="fas fa-bars"></div>
    </div>
</header>

<div class="payment-container">
    <div class="simulacao-msg">
        <i class="fas fa-info-circle"></i>
        Pagamento simulado.
    </div>
    <h2>Pagamento do Pedido</h2>
    <p><strong>Valor a pagar:</strong> €${total}</p>
    <p><strong>Referência do pedido:</strong> ${idOrcamento}</p>
    <p>Por favor, simule o pagamento clicando no botão abaixo.</p>
    
    <form action="ProcessarPagamentoServlet" method="post" id="paymentForm">
        <input type="hidden" name="idOrcamento" value="${idOrcamento}">
        <input type="hidden" name="total" value="${total}">

        <div style="margin: 30px 0; padding: 15px; border: 1px solid #eee; border-radius: 6px; background: #f9f9f9;">
            <p><strong>Entidade:</strong> 12345 (simulação)</p>
            <p><strong>Referência:</strong> será gerada automaticamente</p>
            <p><strong>IBAN:</strong> PT50 0000 0000 0000 0000 0000 0 (simulação para transferência)</p>
        </div>

        <button type="button" class="btn-pay" onclick="simularPagamento()">Simular Pagamento</button>
    </form>
</div>

<!-- Modal de confirmação de pagamento (inicialmente invisível) -->
<div id="modal-pagamento">
    <div class="modal-content">
        <h2 style="color:#404068;">Pagamento Efetuado!</h2>
        <p style="margin-bottom:8px;">O seu pagamento foi registado com sucesso.</p>
        <p><strong>Entidade:</strong> <span id="modal-entidade"></span></p>
        <p><strong>Referência:</strong> <span id="modal-referencia"></span></p>
        <p><strong>Valor Pago:</strong> €<span id="modal-valor"></span></p>
        <div style="margin-top:30px; display:flex; flex-direction:column; gap:12px;">
            <button onclick="fecharEIr('HomeClienteServlet')" style="background:#404068; color:white; border:none; padding:8px 18px; border-radius:4px; cursor:pointer;">
                Ir para Home
            </button>
            <button onclick="fecharEIr('MinhasReservasServlet')" style="background:#407068; color:white; border:none; padding:8px 18px; border-radius:4px; cursor:pointer;">
                Ver Minhas Reservas
            </button>
        </div>
    </div>
</div>

<script>
function simularPagamento() {
    const idOrcamento = document.querySelector('input[name="idOrcamento"]').value;
    const total = document.querySelector('input[name="total"]').value;

    fetch('ProcessarPagamentoServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'idOrcamento=' + encodeURIComponent(idOrcamento) +
              '&total=' + encodeURIComponent(total)
    })
    .then(response => response.json())
    .then(data => {
        let entidade   = (data.entidade   !== undefined && data.entidade   !== null && data.entidade   !== "") ? data.entidade   : '-';
        let referencia = (data.referencia !== undefined && data.referencia !== null && data.referencia !== "") ? data.referencia : '-';
        let valor      = (data.valor      !== undefined && data.valor      !== null && data.valor      !== "") ? data.valor      : '-';
        mostrarPopup(entidade, referencia, valor);
    })
    .catch((e) => {
        alert('Erro ao simular pagamento!');
        console.log(e);
    });
}

function mostrarPopup(entidade, referencia, valor) {
    // Atualizar o conteúdo dos spans do modal
    document.getElementById('modal-entidade').textContent = entidade;
    document.getElementById('modal-referencia').textContent = referencia;
    document.getElementById('modal-valor').textContent = valor;
    // Exibir o modal
    document.getElementById('modal-pagamento').style.display = 'flex';
}

function fecharEIr(destino) {
    document.getElementById('modal-pagamento').style.display = 'none';
    window.location.href = destino;
}

// Navbar/user info toggle (sem alterações):
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
