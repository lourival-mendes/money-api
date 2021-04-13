package com.algaworks.algamoneyapi.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.NumberFormat;

import lombok.Data;

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

	@Column(name = "data_vencimento", nullable = false)
	private LocalDate dataVencimento;

	@Column(name = "data_pagamento")
	private LocalDate dataPagamento;

	@NotNull
	@NumberFormat(pattern = "#0.00")
	private BigDecimal valor;

	@Size(max = 100)
	private String observacao;

	@NotNull
	@Enumerated(EnumType.STRING)
	private TipoLancameto tipo;

	@JoinColumn(name = "id_categoria", nullable = false)
	@ManyToOne
	private Integer categoria;

	@JoinColumn(name = "id_pessoa", nullable = false)
	@ManyToOne
	private Integer pessoa;

}
