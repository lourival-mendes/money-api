package com.algaworks.algamoneyapi.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.algaworks.algamoneyapi.model.TipoLancamento;

import lombok.Data;

@Data
public class LancamentoResumo {

	private Long id;
	private String descricao;
	private LocalDate dataVencimento;
	private LocalDate dataPagamento;
	private BigDecimal valor;
	private TipoLancamento tipo;
	private String categoria;
	private String pessoa;

	public LancamentoResumo(Long id, String descricao, LocalDate dataVencimento, LocalDate dataPagamento,
			BigDecimal valor, TipoLancamento tipo, String categoria, String pessoa) {
		this.id = id;
		this.descricao = descricao;
		this.dataVencimento = dataVencimento;
		this.dataPagamento = dataPagamento;
		this.valor = valor;
		this.tipo = tipo;
		this.categoria = categoria;
		this.pessoa = pessoa;
	}
}
