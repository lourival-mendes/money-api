package com.algaworks.algamoney.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.NumberFormat;

import com.algaworks.algamoney.api.repository.listener.LancamentoAnexoListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@EntityListeners(LancamentoAnexoListener.class)
@Data
@Entity
@Table(name = "lancamento")
public class Lancamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(min = 5, max = 50)
	private String descricao;

	@NotNull
	@Column(name = "data_vencimento")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataVencimento;

	@NotNull
	@Column(name = "data_pagamento")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataPagamento;

	@NotNull
	@NumberFormat(pattern = "#0.00")
	private BigDecimal valor;

	@Size(max = 100)
	private String observacao;

	@NotNull
	@Enumerated(EnumType.STRING)
	private TipoLancamento tipo;

	@JsonIgnore
	public boolean isReceita() {
		return TipoLancamento.RECEITA.equals(this.tipo);
	}

	@NotNull
	@JoinColumn(name = "id_categoria")
	@ManyToOne
	private Categoria categoria;

	@JsonIgnoreProperties("contatos")
	@NotNull
	@JoinColumn(name = "id_pessoa")
	@ManyToOne
	private Pessoa pessoa;

	private String anexo;

	private String urlAnexo;

}
