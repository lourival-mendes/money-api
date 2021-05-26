package com.algaworks.algamoney.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

/**
 * 	@Scheduled(fixedDelay = 1000 * 60 * 60)
	public void fixedDelay() {
		System.out.println(">>>>>>> Método fixedDelay sendo executado.");
	}

	@Scheduled(cron = "0 55 18 * * *")
	public void cron() {
		System.out.println(">>>>>>> Método cron sendo executado.");
	}
*/
	public Lancamento save(Lancamento lancamento) {

		Pessoa pessoaLancamento = lancamento.getPessoa();
		Optional<Pessoa> pessoaSalva = pessoaRepository.findById(pessoaLancamento.getId());

		if (pessoaSalva.isEmpty() || pessoaSalva.get().isInativo())
			throw new PessoaInexistenteOuInativaException();

		return lancamentoRepository.save(lancamento);

	}

}
