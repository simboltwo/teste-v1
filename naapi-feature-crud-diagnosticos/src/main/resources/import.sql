-- simboltwo/teste-v1/teste-v1-34e2a9e7180b2ecb8b56ff799a867520bb85ea5d/naapi-feature-crud-diagnosticos/src/main/resources/import.sql

-- 1. Inserir Papéis (Roles)
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_COORDENADOR_NAAPI'); -- ID 1
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_MEMBRO_TECNICO');    -- ID 2
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_ESTAGIARIO_NAAPI');  -- ID 3
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_COORDENADOR_CURSO'); -- ID 4
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_PROFESSOR');         -- ID 5

-- 2. Inserir Usuários (APENAS O COORDENADOR)
-- A senha para todos é "123456" (encriptada com BCrypt)
INSERT INTO TbUsuario (NmUsuario, DsEmail, DsSenha) VALUES ('Admin Coordenador', 'coordenador@naapi.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3e3taqL.zU5aTxtEnA3PAewQYhQOP3y'); -- ID 1

-- 3. Ligar Usuários aos Papéis (APENAS O COORDENADOR)
INSERT INTO TbUsuarioPapel (CdUsuario, CdPapel) VALUES (1, 1); -- Admin é Coordenador NAAPI
-- REMOVIDO: VÍNCULO DO MEMBRO (ID 2)
-- REMOVIDO: VÍNCULO DO ESTAGIARIO (ID 3)
