CREATE TABLE `contato` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `nome` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `telefone` VARCHAR(13) NOT NULL,
  `id_pessoa` BIGINT(20) NOT NULL,
  FOREIGN KEY (id_pessoa) REFERENCES pessoa(id)
  );
  
  INSERT INTO contato (id_pessoa, nome, email, telefone) values (1, "Robson", "robson@algamoney", "61 88888-8888");