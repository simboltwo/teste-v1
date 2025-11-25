
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_COORDENADOR_NAAPI'); -- ID 1
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_MEMBRO_TECNICO');    -- ID 2
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_ESTAGIARIO_NAAPI');  -- ID 3
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_COORDENADOR_CURSO'); -- ID 4
INSERT INTO TbPapel (DsAuthority) VALUES ('ROLE_PROFESSOR');         -- ID 5

INSERT INTO TbUsuario (NmUsuario, DsEmail, DsSenha) VALUES ('Admin Coordenador', 'coordenador@naapi.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3e3taqL.zU5aTxtEnA3PAewQYhQOP3y');

INSERT INTO TbUsuarioPapel (CdUsuario, CdPapel) VALUES (1, 1); -- Admin é Coordenador NAAPI
