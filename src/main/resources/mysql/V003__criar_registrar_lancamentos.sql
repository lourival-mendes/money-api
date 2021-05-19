CREATE TABLE `lancamento` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `descricao` VARCHAR(50) NOT NULL,
  `data_vencimento` DATE NOT NULL,
  `data_pagamento` DATE NOT NULL,
  `valor` DECIMAL(10,2) NOT NULL,
  `observacao` VARCHAR(100),
  `tipo` VARCHAR(20) NULL,
  `id_categoria` BIGINT(20) NOT NULL,
  `id_pessoa` BIGINT(20) NOT NULL,
  FOREIGN KEY (id_categoria) REFERENCES categoria(id),
  FOREIGN KEY (id_pessoa) REFERENCES pessoa(id));
  
  INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, id_categoria, id_pessoa) values ("descrição", "2021-04-13", "2021-04-14",10.50, "observação", "DESPESA", 1, 1);
  INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, id_categoria, id_pessoa) values ("descrição", "2021-04-13", "2021-04-14",11.50, "observação", "DESPESA", 2, 2);
  INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, id_categoria, id_pessoa) values ("descrição", "2021-04-13", "2021-04-14",12.50, "observação", "DESPESA", 3, 3);
  INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, id_categoria, id_pessoa) values ("descrição", "2021-04-13", "2021-04-14",13.50, "observação", "DESPESA", 4, 4);
  INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, id_categoria, id_pessoa) values ("descrição", "2021-04-13", "2021-04-14",14.50, "observação", "DESPESA", 5, 5);
  INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, id_categoria, id_pessoa) values ("descrição", "2021-04-13", "2021-04-14",15.50, "observação", "RECEITA", 1, 6);
  INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, id_categoria, id_pessoa) values ("descrição", "2021-04-13", "2021-04-14",16.50, "observação", "RECEITA", 2, 7);
  INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, id_categoria, id_pessoa) values ("descrição", "2021-04-13", "2021-04-14",17.50, "observação", "RECEITA", 3, 8);
  INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, id_categoria, id_pessoa) values ("descrição", "2021-04-13", "2021-04-14",18.50, "observação", "RECEITA", 4, 9);
  INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, id_categoria, id_pessoa) values ("descrição", "2021-04-13", "2021-04-14",19.50, "observação", "RECEITA", 5, 10);
