-- Possivel reutilização
INSERT INTO lead (id, nome, email, empresa, site, telefones, status, anotacoes, id_usuario_responsavel)
VALUES(34, 'Meu primeiro Lead', 'milton@meetime.com','Meetime', 'http://meetime.com.br',
       '+551149091999;+554890097888', 'LOST', 'some annotations', 1);

-- Sem possivel reutilização
INSERT INTO lead (id, nome, email, empresa, site, telefones, status, anotacoes, id_usuario_responsavel)
VALUES(36, 'Meu segundo Lead', 'diego@meetime.com','Meetime', 'http://meetime.com.br',
       '+551149091999;+554890097888', 'OPEN', 'some annotations', 3);