package com.algaworks.algamoney.api.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.algaworks.algamoney.api.mail.Mailer;
import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.model.Usuario;
import com.algaworks.algamoney.api.repository.LancamentoRepository;
import com.algaworks.algamoney.api.repository.PessoaRepository;
import com.algaworks.algamoney.api.repository.UsuarioRepository;
import com.algaworks.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {

	private static final Logger logger = LoggerFactory.getLogger(LancamentoService.class);

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private Mailer mailer;

	@Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
	public void avisarSobreLancamentosVencidos() {

		if (logger.isDebugEnabled())
			logger.debug("Preparando envio de e-mails de avisos de lançamentos vencidos.");

		List<Lancamento> lancamentosVencidos = lancamentoRepository
				.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());

		if (lancamentosVencidos.isEmpty()) {

			logger.info("Não há lançamentos vencidos. Nenhum e-mail foi enviado.");
			return;

		}

		logger.info("Existem {} lançamento(s) vencido(s).", lancamentosVencidos.size());

		List<Usuario> usuarios = usuarioRepository.findByPermissoesDescricao("ROLE_PESQUISAR_LANCAMENTO");

		if (usuarios.isEmpty()) {

			logger.warn("Não há usuário com permissão para pesquisa de lançamento. Nenhum e-mail foi enviado.");
			return;

		}

		logger.info("Existe(m) {} destinatário(s).", usuarios.size());

		Map<String, Object> variaveis = new HashMap<>();
		variaveis.put("lancamentos", lancamentosVencidos);

		String template = "mail/aviso-lancamentos-vencidos";

		this.mailer.avisarSobreLancamentosVencidos(lancamentosVencidos, usuarios, template);

		logger.info("E-mails de avisos de lançamentos vencidos realizados.");

	}

	public Lancamento save(Lancamento lancamento) {

		Pessoa pessoaLancamento = lancamento.getPessoa();
		Optional<Pessoa> pessoaSalva = pessoaRepository.findById(pessoaLancamento.getId());

		if (pessoaSalva.isEmpty() || pessoaSalva.get().isInativo())
			throw new PessoaInexistenteOuInativaException();

		return lancamentoRepository.save(lancamento);

	}

}
