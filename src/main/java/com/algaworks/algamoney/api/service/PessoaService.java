package com.algaworks.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;

	public Pessoa save(Pessoa pessoa) {

		pessoa.getContatos().forEach(p -> p.setPessoa(pessoa));

		return pessoaRepository.save(pessoa);

	}

	public Pessoa update(Long id, Pessoa pessoa) {

		Pessoa pessoaSalva = this.findById(id);

		pessoaSalva.getContatos().clear();
		pessoaSalva.getContatos().addAll(pessoa.getContatos());
		pessoaSalva.getContatos().forEach(p -> p.setPessoa(pessoaSalva));

		BeanUtils.copyProperties(pessoa, pessoaSalva, "id", "contatos");

		return pessoaRepository.save(pessoaSalva);

	}

	public Pessoa findById(Long id) {

		return pessoaRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));

	}

	public void updateAtivo(Long id, Boolean ativo) {

		Pessoa pessoa = this.findById(id);
		pessoa.setAtivo(ativo);

		pessoaRepository.save(pessoa);

	}

}
