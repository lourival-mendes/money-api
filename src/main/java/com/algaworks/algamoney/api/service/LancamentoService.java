package com.algaworks.algamoney.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.LancamentoRepository;
import com.algaworks.algamoney.api.repository.PessoaRepository;
import com.algaworks.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	public Lancamento save(Lancamento lancamento) {

		Pessoa pessoaLancamento = lancamento.getPessoa();
		Optional<Pessoa> pessoaSalva = pessoaRepository.findById(pessoaLancamento.getId());

		if (pessoaSalva.isEmpty() || pessoaSalva.get().isInativo())
			throw new PessoaInexistenteOuInativaException();

		return lancamentoRepository.save(lancamento);

	}

}
