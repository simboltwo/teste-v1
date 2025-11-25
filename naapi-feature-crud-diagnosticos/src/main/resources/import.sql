-- 1. Inserir Papéis (Roles)
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_COORDENADOR_NAAPI'); -- ID 1
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_MEMBRO_TECNICO');    -- ID 2
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_ESTAGIARIO_NAAPI');  -- ID 3
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_COORDENADOR_CURSO'); -- ID 4
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_PROFESSOR');         -- ID 5

-- 2. Inserir Usuários (RESTAURADOS)
-- A senha para todos é "123456" (encriptada com BCrypt)
INSERT INTO TbUsuario (NmUsuario, DsEmail, DsSenha) VALUES ('Admin Coordenador', 'coordenador@naapi.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3e3taqL.zU5aTxtEnA3PAewQYhQOP3y'); -- ID 1
INSERT INTO TbUsuario (NmUsuario, DsEmail, DsSenha) VALUES ('Membro Tecnico', 'membro@naapi.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3e3taqL.zU5aTxtEnA3PAewQYhQOP3y');    -- ID 2
INSERT INTO TbUsuario (NmUsuario, DsEmail, DsSenha) VALUES ('Estagiario 1 (Assistente)', 'estagiario@naapi.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3e3taqL.zU5aTxtEnA3PAewQYhQOP3y'); -- ID 3
-- ADICIONE AQUI MAIS USUÁRIOS DE TESTE, se necessário (ex: professor@naapi.com, coordcurso@naapi.com)

-- 3. Ligar Usuários aos Papéis (RESTAURADOS)
INSERT INTO TbUsuarioPapel (CdUsuario, CdPapel) VALUES (1, 1); -- Admin é Coordenador NAAPI
INSERT INTO TbUsuarioPapel (CdUsuario, CdPapel) VALUES (2, 2); -- Membro é Membro Técnico
INSERT INTO TbUsuarioPapel (CdUsuario, CdPapel) VALUES (3, 3); -- Estagiario é Estagiário (Assistente)

-- 4. Inserir Dados para Alunos (Cursos, Turmas, Diagnósticos)
INSERT INTO TbCurso (NmCurso) VALUES ('Engenharia de Computação'); -- ID 1
INSERT INTO TbCurso (NmCurso) VALUES ('Arquitetura'); -- ID 2

INSERT INTO TbTurma (NmTurma) VALUES ('COM-2023'); -- ID 1
INSERT INTO TbTurma (NmTurma) VALUES ('ARQ-2022'); -- ID 2

INSERT INTO TbDiagnostico (cid, NmDiagnostico, SIGLA) VALUES ('F84.0', 'Autismo', 'TEA'); -- ID 1
INSERT INTO TbDiagnostico (cid, NmDiagnostico, SIGLA) VALUES ('F90.0', 'Hiperatividade', 'TDAH'); -- ID 2

-- 5. INSERIR TIPOS DE ATENDIMENTO
INSERT INTO TbTipoAtendimento (NmAtendimento) VALUES ('Assistência');     -- ID 1
INSERT INTO TbTipoAtendimento (NmAtendimento) VALUES ('Orientação de Estudos'); -- ID 3

-- 6. Inserir Alunos (Base para testes)
INSERT INTO TbAluno (NmAluno, CdMatricula, DsPrioridade, CdCurso, CdTurma) VALUES ('Maria Silva', '123456', 'Baixa', 1, 1); -- ID 1
INSERT INTO TbAluno (NmAluno, NmSocial, CdMatricula, DsPrioridade, CdCurso, CdTurma) VALUES ('João Souza', 'Joana', '789012', 'Alta', 2, 2); -- ID 2
INSERT INTO TbAluno (NmAluno, CdMatricula, DsPrioridade, CdCurso, CdTurma) VALUES ('Carlos Pereira', '654321', 'Média', 1, 1); -- ID 3

-- Ligar Aluno 1 ao Diagnóstico 1
INSERT INTO TbAlunoDiagnostico (CdAluno, CdDiagnostico) VALUES (1, 1);