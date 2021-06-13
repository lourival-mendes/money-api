package br.com.lourivalmendes.money.api.dto;

import java.math.BigDecimal;

import br.com.lourivalmendes.money.api.model.Pessoa;
import br.com.lourivalmendes.money.api.model.TipoLancamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LancamentoEstatisticaPessoaDTO {

	private TipoLancamento tipo;
	private Pessoa pessoa;
	private BigDecimal total;

}
