package com.algaworks.algamoney.api.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.algaworks.algamoney.api.mail.Mailer;
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

	@Autowired
	private Mailer mailer;

	@Scheduled(fixedDelay = 1000 * 60 * 60)
	public void fixedDelay() {

		List<Lancamento> lancamentos = lancamentoRepository.findAll();

		Map<String, Object> variaveis = new HashMap<>();
		variaveis.put("lancamentos", lancamentos);

		String template = "mail/aviso-lancamentos-vencidos";

		this.mailer.enviarEmail(Arrays.asList("minhavirtude@gmail.com"), "Teste de envio de e-mail", template,
				variaveis);
	}

	/**
	 * @Scheduled(cron = "0 55 18 * * *") public void cron() {
	 *                 System.out.println(">>>>>>> Método cron sendo executado."); }
	 */
	public Lancamento save(Lancamento lancamento) {

		Pessoa pessoaLancamento = lancamento.getPessoa();
		Optional<Pessoa> pessoaSalva = pessoaRepository.findById(pessoaLancamento.getId());

		if (pessoaSalva.isEmpty() || pessoaSalva.get().isInativo())
			throw new PessoaInexistenteOuInativaException();

		return lancamentoRepository.save(lancamento);

	}

}
