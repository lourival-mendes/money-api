CREATE TABLE `pessoa` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(200) NOT NULL,
  `logradouro` VARCHAR(200) NULL,
  `numero` VARCHAR(20) NULL,
  `complemento` VARCHAR(100) NULL,
  `bairro` VARCHAR(200) NULL,
  `cep` VARCHAR(10) NULL,
  `cidade` VARCHAR(200) NULL,
  `estado` VARCHAR(2) NULL,
  `ativo` TINYINT NOT NULL,
  PRIMARY KEY (`id`));
  
  INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ("Lourival", "Condominio Quinta dos Ipês", "1","complemento", "bairro", "71693400", "cidade", "DF", true);
  INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ("Lourival", "Condominio Quinta dos Ipês", "2","complemento", "bairro", "71693400", "cidade", "DF", true);
  INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ("Lourival", "Condominio Quinta dos Ipês", "3","complemento", "bairro", "71693400", "cidade", "DF", true);
  INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ("Lourival", "Condominio Quinta dos Ipês", "4","complemento", "bairro", "71693400", "cidade", "DF", true);
  INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ("Lourival", "Condominio Quinta dos Ipês", "5","complemento", "bairro", "71693400", "cidade", "DF", true);
  INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ("Lourival", "Condominio Quinta dos Ipês", "6","complemento", "bairro", "71693400", "cidade", "DF", true);
  INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ("Lourival", "Condominio Quinta dos Ipês", "7","complemento", "bairro", "71693400", "cidade", "DF", true);
  INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ("Lourival", "Condominio Quinta dos Ipês", "8","complemento", "bairro", "71693400", "cidade", "DF", true);
  INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ("Lourival", "Condominio Quinta dos Ipês", "9","complemento", "bairro", "71693400", "cidade", "DF", true);
  INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ("Lourival", "Condominio Quinta dos Ipês", "10","complemento", "bairro", "71693400", "cidade", "DF", false);
