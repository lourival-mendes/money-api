CREATE TABLE `usuario` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `nome` VARCHAR(50) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `senha` VARCHAR(150) NOT NULL);

CREATE TABLE `permissao` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `descricao` VARCHAR(50) NOT NULL);

CREATE TABLE `usuario_permissao` (
  `id_usuario` BIGINT(20) NOT NULL,
  `id_permissao` BIGINT(20) NOT NULL,
  PRIMARY KEY (id_usuario, id_permissao),
  FOREIGN KEY (id_usuario) REFERENCES usuario(id),
  FOREIGN KEY (id_permissao) REFERENCES permissao(id));
  
  INSERT INTO usuario (nome, email, senha) values ("Administrador", "minhavirtude@gmail.com", "$2a$10$MDc27YAC4RJdZTzkfN4dLOPNLGkv.BQMX/JSZ0JeryCfH1mn0p/Gq");
  INSERT INTO usuario (nome, email, senha) values ("Pesquisador", "lourivalmendes@hotmail.com", "$2a$10$MDc27YAC4RJdZTzkfN4dLOPNLGkv.BQMX/JSZ0JeryCfH1mn0p/Gq");

  INSERT INTO permissao (descricao) values ("ROLE_CADASTRAR_CATEGORIA");
  INSERT INTO permissao (descricao) values ("ROLE_REMOVER_CATEGORIA");
  INSERT INTO permissao (descricao) values ("ROLE_PESQUISAR_CATEGORIA");
  INSERT INTO permissao (descricao) values ("ROLE_CADASTRAR_PESSOA");
  INSERT INTO permissao (descricao) values ("ROLE_REMOVER_PESSOA");
  INSERT INTO permissao (descricao) values ("ROLE_PESQUISAR_PESSOA");
  INSERT INTO permissao (descricao) values ("ROLE_CADASTRAR_LANCAMENTO");
  INSERT INTO permissao (descricao) values ("ROLE_REMOVER_LANCAMENTO");
  INSERT INTO permissao (descricao) values ("ROLE_PESQUISAR_LANCAMENTO");

-- Administrador
  INSERT INTO usuario_permissao (id_usuario,id_permissao) values (1,1);
  INSERT INTO usuario_permissao (id_usuario,id_permissao) values (1,2);
  INSERT INTO usuario_permissao (id_usuario,id_permissao) values (1,3);
  INSERT INTO usuario_permissao (id_usuario,id_permissao) values (1,4);
  INSERT INTO usuario_permissao (id_usuario,id_permissao) values (1,5);
  INSERT INTO usuario_permissao (id_usuario,id_permissao) values (1,6);
  INSERT INTO usuario_permissao (id_usuario,id_permissao) values (1,7);
  INSERT INTO usuario_permissao (id_usuario,id_permissao) values (1,8);
  INSERT INTO usuario_permissao (id_usuario,id_permissao) values (1,9);

-- Agente
  INSERT INTO usuario_permissao (id_usuario,id_permissao) values (2,2);
  INSERT INTO usuario_permissao (id_usuario,id_permissao) values (2,5);
  INSERT INTO usuario_permissao (id_usuario,id_permissao) values (2,8);
