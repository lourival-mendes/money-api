CREATE TABLE `categoria` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`));
  
  INSERT INTO categoria(nome) values ('Lazer');
  INSERT INTO categoria(nome) values ('Alimentação');
  INSERT INTO categoria(nome) values ('Supermercado');
  INSERT INTO categoria(nome) values ('Farmácia');
  INSERT INTO categoria(nome) values ('Outros');
  