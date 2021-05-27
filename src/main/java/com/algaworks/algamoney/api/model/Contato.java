package com.algaworks.algamoney.api.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name = "contato")
public class Contato {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(min = 3, max = 20)
	private String nome;

	@NotBlank
	@Email
	@Size(min = 5, max = 100)
	private String email;

	@NotBlank
	@Size(min = 10, max = 20)
	private String telefone;

	@NotEmpty
	@ManyToOne
	@JoinColumn(name = "id_pessoa")
	private Pessoa pessoa;

}
