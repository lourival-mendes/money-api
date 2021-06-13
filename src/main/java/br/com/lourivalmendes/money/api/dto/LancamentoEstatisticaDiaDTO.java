package br.com.lourivalmendes.money.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.lourivalmendes.money.api.model.TipoLancamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LancamentoEstatisticaDiaDTO {

	private TipoLancamento tipo;
	private LocalDate dia;
	private BigDecimal total;

}
