CREATE TABLE Distrito (
    cod_distrito INT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(255)
);

CREATE TABLE Localidade (
    cod_postal VARCHAR(20) PRIMARY KEY,
    nome VARCHAR(255),
    cod_distrito INT FOREIGN KEY REFERENCES Distrito(cod_distrito)
);

CREATE TABLE Ramo (
    id_ramo INT IDENTITY(1,1) PRIMARY KEY,
    descricao VARCHAR(255)
);

/* Se ainda não existir, força unicidade da descrição */
ALTER TABLE Ramo
ADD CONSTRAINT UQ_Ramo_Descricao UNIQUE (descricao);


INSERT INTO Ramo (descricao)
VALUES
  ('Catering / Restauração'),
  ('Pastelaria & Bolos'),
  ('Bar / Cocktails'),
  ('Decoração & Design Floral'),
  ('Aluguer de Mobiliário'),
  ('Som, Luz & Palco'),
  ('Fotografia'),
  ('Videografia'),
  ('Música ao Vivo'),
  ('DJ / Animação Musical'),
  ('Pirotecnia & Efeitos Especiais'),
  ('Convites & Papetaria'),
  ('Lembranças / Brindes'),
  ('Planeamento / Wedding Planner'),
  ('Transporte & Logística'),
  ('Aluguer de Veículos Clássicos'),
  ('Espaços para Eventos'),
  ('Equipamento Audiovisual'),
  ('Serviços de Limpeza'),
  ('Staff / Segurança'),
  ('Tecnologias Interativas');

CREATE TABLE Utilizador (
    id_user INT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(255),
    email VARCHAR(255),
    pwd VARCHAR(255),
    data_registo DATETIME,
    ativo BIT,
    telefone VARCHAR(50),
    morada VARCHAR(255),
    cod_postal VARCHAR(20) FOREIGN KEY REFERENCES Localidade(cod_postal)
);

CREATE TABLE Cliente (
    id_cliente INT IDENTITY(1,1) PRIMARY KEY,
    id_user INT UNIQUE FOREIGN KEY REFERENCES Utilizador(id_user)
);

CREATE TABLE Admin (
    id_admin INT IDENTITY(1,1) PRIMARY KEY,
    id_user INT UNIQUE FOREIGN KEY REFERENCES Utilizador(id_user),
    permissao VARCHAR(255)
);

CREATE TABLE Fornecedor (
    id_fornecedor INT IDENTITY(1,1) PRIMARY KEY,
    id_user INT UNIQUE FOREIGN KEY REFERENCES Utilizador(id_user),
    id_ramo INT UNIQUE FOREIGN KEY REFERENCES Ramo(id_ramo)
);

CREATE TABLE Servico (
    id_servico INT IDENTITY(1,1) PRIMARY KEY,
    descricao VARCHAR(255)
);

ALTER TABLE Servico
ADD id_ramo INT FOREIGN KEY REFERENCES Ramo(id_ramo);

CREATE TABLE Servico_Fornecedor (
    id_servico_fornecedor INT IDENTITY(1,1) PRIMARY KEY,
    id_servico INT FOREIGN KEY REFERENCES Servico(id_servico),
    id_fornecedor INT FOREIGN KEY REFERENCES Fornecedor(id_fornecedor),
    preco FLOAT,
    CONSTRAINT unique_servico_fornecedor UNIQUE (id_servico, id_fornecedor)
);

ALTER TABLE Servico_Fornecedor ADD imagem_url VARCHAR(255);


CREATE TABLE Espaco (
    id_espaco INT IDENTITY(1,1) PRIMARY KEY,
    descricao VARCHAR(255),
    id_fornecedor INT FOREIGN KEY REFERENCES Fornecedor(id_fornecedor),
    preco FLOAT,
    morada VARCHAR(255),
    cod_postal VARCHAR(20) FOREIGN KEY REFERENCES Localidade(cod_postal)
);

ALTER TABLE Espaco
  ADD latitude  DECIMAL(10,7)  NULL,
      longitude DECIMAL(10,7)  NULL;

CREATE TABLE Evento (
    id_evento INT IDENTITY(1,1) PRIMARY KEY,
    descricao VARCHAR(255)
);

CREATE TABLE Cliente_Servico (
    id_cliente_servico INT IDENTITY(1,1) PRIMARY KEY,
    id_servico_fornecedor INT FOREIGN KEY REFERENCES Servico_Fornecedor(id_servico_fornecedor),
    id_cliente INT FOREIGN KEY REFERENCES Cliente(id_cliente),
    data DATETIME,
    id_evento INT FOREIGN KEY REFERENCES Evento(id_evento),
    id_espaco INT FOREIGN KEY REFERENCES Espaco(id_espaco),
    preco_servico FLOAT,
    preco_espaco FLOAT
);

ALTER TABLE Cliente_Servico
ADD estado VARCHAR(30) DEFAULT 'PENDENTE';

CREATE TABLE Orcamento (
    id_orcamento INT IDENTITY(1,1) PRIMARY KEY,
    id_cliente INT FOREIGN KEY REFERENCES Cliente(id_cliente),
    id_evento INT FOREIGN KEY REFERENCES Evento(id_evento),
	num_convidados INT CHECK (num_convidados > 0),
    data DATETIME,
    data_aprovacao DATETIME,
    desconto FLOAT CHECK (desconto BETWEEN 0 AND 1),
    valor_orcamento FLOAT,
    valor_com_desconto AS (valor_orcamento * (1 - desconto) * num_convidados) PERSISTED
);


CREATE TABLE Orcamento_Servico (
	CONSTRAINT PK_Orcamento_Servico PRIMARY KEY (id_orcamento, id_servico),
	id_orcamento INT FOREIGN KEY REFERENCES Orcamento(id_orcamento),
    id_servico INT FOREIGN KEY REFERENCES Servico(id_servico),
    id_espaco INT FOREIGN KEY REFERENCES Espaco(id_espaco),
    preco_servico FLOAT,
    preco_espaco FLOAT
);


CREATE TABLE Avaliacao (
    id_avaliacao INT IDENTITY(1,1) PRIMARY KEY,
    id_cliente INT FOREIGN KEY REFERENCES Cliente(id_cliente),
    comentario VARCHAR(255),
    data DATETIME,
    data_validacao DATETIME
);

CREATE TABLE Avaliacao_Cliente_Servico (
	CONSTRAINT PK_Avaliacao_Cliente_Servico PRIMARY KEY (id_avaliacao, id_cliente_servico),
    id_avaliacao INT FOREIGN KEY REFERENCES Avaliacao(id_avaliacao),
    id_cliente_servico INT UNIQUE FOREIGN KEY REFERENCES Cliente_Servico(id_cliente_servico),
    nota INT,
    comentario VARCHAR(255)
);

CREATE TABLE Pagamento (
    id_pagamento INT IDENTITY(1,1) PRIMARY KEY,
    data DATETIME,
    entidade BIGINT,
    referencia BIGINT,
    valor FLOAT,
    id_cliente INT FOREIGN KEY REFERENCES Cliente(id_cliente),
    valida_id_admin INT FOREIGN KEY REFERENCES Admin(id_admin),
    valida_data DATETIME
);

CREATE TABLE Pagamento_Cliente_Servico (
    id_cliente_servico INT UNIQUE FOREIGN KEY REFERENCES Cliente_Servico(id_cliente_servico),
    id_pagamento INT FOREIGN KEY REFERENCES Pagamento(id_pagamento),
	CONSTRAINT PK_Pagamento_Cliente_Servico PRIMARY KEY (id_cliente_servico, id_pagamento)
);

-- Inserir todos os distritos de Portugal
INSERT INTO Distrito (nome) VALUES 
('Aveiro'), ('Beja'), ('Braga'), ('Bragança'), ('Castelo Branco'), ('Coimbra'), 
('Évora'), ('Faro'), ('Guarda'), ('Leiria'), ('Lisboa'), ('Portalegre'), 
('Porto'), ('Santarém'), ('Setúbal'), ('Viana do Castelo'), ('Vila Real'), ('Viseu'), ('Madeira'), ('Açores');

-- Inserir Localidades para os espaços
INSERT INTO Localidade (cod_postal, nome, cod_distrito) VALUES
('4000-001', 'Porto Centro', 13),
('8000-002', 'Faro Marina', 8),
('1050-003', 'Lisboa Baixa', 11);

-- Inserir Espaços com localização
INSERT INTO Espaco (descricao, id_fornecedor, preco, morada, cod_postal) VALUES
('Salão Porto', 1, 1200.00, 'Rua da Alegria, 100', '4000-001'),
('Resort Faro', 1, 2000.00, 'Avenida Marítima, 50', '8000-002'),
('Palácio Lisboa', 1, 3000.00, 'Praça do Comércio, 1', '1050-003');

-- Adicionar 5 tipos de eventos
INSERT INTO Evento (descricao) VALUES 
('Casamento'), ('Aniversário'), ('Conferência'), ('Festa Corporativa'), ('Batizado');


ALTER TABLE Fornecedor
DROP CONSTRAINT UQ__Forneced__08537BCF95C412AB;
/*id_ ramo na tabela fornecedor já não é UNIQUE*/


SELECT id_cliente_servico, id_espaco, preco_espaco FROM Cliente_Servico ORDER BY id_cliente_servico DESC;

Select * from Espaco

UPDATE cs
SET cs.preco_espaco = e.preco
FROM Cliente_Servico cs
JOIN Espaco e ON cs.id_espaco = e.id_espaco
WHERE cs.preco_espaco = 0;


SELECT os.id_espaco, e.preco FROM Orcamento_Servico os
                    JOIN Espaco e ON os.id_espaco = e.id_espaco 
                    WHERE os.id_orcamento =  AND os.id_espaco IS NOT NULL);

					SeLEct * from Espaco
					SeLEct * from Orcamento
				
ALTER TABLE Cliente_Servico
ADD id_orcamento INT FOREIGN KEY REFERENCES Orcamento(id_orcamento);

/*DELETE FROM Pagamento_Cliente_Servico;
DELETE FROM Pagamento;
DELETE FROM Cliente_Servico;
DELETE FROM Orcamento_Servico;
DELETE FROM Orcamento;*/

ALTER TABLE Orcamento ADD estado_global VARCHAR(30) DEFAULT 'PENDENTE';

SELECT * FROM Cliente_Servico WHERE id_espaco IS NOT NULL AND id_servico_fornecedor IS NOT NULL;

SELECT cs.*, u.nome AS nomeCliente, e.descricao AS descricaoServico 
            FROM Cliente_Servico cs 
            JOIN Espaco e ON cs.id_espaco = e.id_espaco 
            JOIN Cliente c ON cs.id_cliente = c.id_cliente 
            JOIN Utilizador u ON c.id_user = u.id_user 
            WHERE e.id_fornecedor = 2
            ORDER BY cs.data DESC

			select * from Cliente_Servico
			

DELETE FROM Cliente_Servico
WHERE id_servico_fornecedor IS NULL 

