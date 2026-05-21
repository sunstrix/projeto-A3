-- Inserir usuário administrador
INSERT INTO usuarios (nome, cpf, email, cargo, login, senha, perfil, ativo) 
VALUES ('Administrador', '11144477735', 'admin@empresa.com', 'TI', 'admin', 'admin', 'ADMINISTRADOR', true);

-- Inserir usuário gerente
INSERT INTO usuarios (nome, cpf, email, cargo, login, senha, perfil, ativo) 
VALUES ('Gerente Projetos', '87654321596', 'gerente@empresa.com', 'Gerente', 'gerente', 'gerente', 'GERENTE', true);

-- Inserir usuário colaborador
INSERT INTO usuarios (nome, cpf, email, cargo, login, senha, perfil, ativo) 
VALUES ('Colaborador', '39053344705', 'colaborador@empresa.com', 'Analista', 'colaborador', '123456', 'COLABORADOR', true);
