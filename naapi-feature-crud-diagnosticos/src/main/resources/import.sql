-- 1. Inserir Papéis (Roles)
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_COORDENADOR_NAAPI'); -- ID 1
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_MEMBRO_TECNICO');    -- ID 2
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_ESTAGIARIO_NAAPI');  -- ID 3
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_COORDENADOR_CURSO'); -- ID 4
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_PROFESSOR');         -- ID 5

-- 2. Inserir Usuários
-- A senha para ambos é "123456" (encriptada com BCrypt)
INSERT INTO TbUsuario (NmUsuario, DsEmail, DsSenha) VALUES ('Admin Coordenador', 'coordenador@naapi.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3e3taqL.zU5aTxtEnA3PAewQYhQOP3y'); -- ID 1
INSERT INTO TbUsuario (NmUsuario, DsEmail, DsSenha) VALUES ('Membro Tecnico', 'membro@naapi.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3e3taqL.zU5aTxtEnA3PAewQYhQOP3y');    -- ID 2

-- 3. Ligar Usuários aos Papéis
INSERT INTO TbUsuarioPapel (CdUsuario, CdPapel) VALUES (1, 1); -- Admin é Coordenador NAAPI
INSERT INTO TbUsuarioPapel (CdUsuario, CdPapel) VALUES (2, 2); -- Membro é Membro Técnico

-- 4. Inserir Dados para Alunos (para os testes de DELETE e GET)
INSERT INTO TbCurso (NmCurso) VALUES ('Engenharia de Computação'); -- ID 1
INSERT INTO TbCurso (NmCurso) VALUES ('Arquitetura'); -- ID 2

INSERT INTO TbTurma (NmTurma) VALUES ('INFO-2023'); -- ID 1
INSERT INTO TbTurma (NmTurma) VALUES ('ARQ-2022'); -- ID 2

INSERT INTO TbDiagnostico (cid, NmDiagnostico, SIGLA) VALUES ('F84.0', 'Autismo', 'TEA'); -- ID 1

-- Inserir Alunos (ic_ativo = true por defeito, como definido na entidade)
INSERT INTO TbAluno (NmAluno, CdMatricula, IcPrioridade, CdCurso, CdTurma) VALUES ('Maria Silva', '123456', false, 1, 1); -- ID 1
INSERT INTO TbAluno (NmAluno, NmSocial, CdMatricula, IcPrioridade, CdCurso, CdTurma) VALUES ('João Souza', 'Joana', '789012', true, 2, 2); -- ID 2
INSERT INTO TbAluno (NmAluno, CdMatricula, IcPrioridade, CdCurso, CdTurma) VALUES ('Carlos Pereira', '654321', false, 1, 1); -- ID 3

-- Ligar Aluno 1 ao Diagnóstico 1
INSERT INTO TbAlunoDiagnostico (CdAluno, CdDiagnostico) VALUES (1, 1);