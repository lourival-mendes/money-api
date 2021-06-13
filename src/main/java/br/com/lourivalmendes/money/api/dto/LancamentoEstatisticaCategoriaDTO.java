package br.com.lourivalmendes.money.api.dto;

import java.math.BigDecimal;

import br.com.lourivalmendes.money.api.model.Categoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LancamentoEstatisticaCategoriaDTO {

	private Categoria categoria;
	private BigDecimal total;

}
