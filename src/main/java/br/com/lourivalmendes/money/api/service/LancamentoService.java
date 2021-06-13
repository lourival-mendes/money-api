package br.com.lourivalmendes.money.api.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.lourivalmendes.money.api.mail.Mailer;
import br.com.lourivalmendes.money.api.model.Lancamento;
import br.com.lourivalmendes.money.api.model.Pessoa;
import br.com.lourivalmendes.money.api.model.Usuario;
import br.com.lourivalmendes.money.api.repository.LancamentoRepository;
import br.com.lourivalmendes.money.api.repository.PessoaRepository;
import br.com.lourivalmendes.money.api.repository.UsuarioRepository;
import br.com.lourivalmendes.money.api.service.exception.LancamentoInexistenteException;
import br.com.lourivalmendes.money.api.service.exception.PessoaInexistenteOuInativaException;
import br.com.lourivalmendes.money.api.storage.S3;

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

	@Autowired
	private S3 s3;

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

		validarPessoa(lancamento);

		if (StringUtils.hasText(lancamento.getAnexo()))
			s3.salvar(lancamento.getAnexo());

		Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);

		return lancamentoSalvo;

	}

	private void validarPessoa(Lancamento lancamento) {

		Long idPessoa = lancamento.getPessoa().getId();
		Optional<Pessoa> pessoa = pessoaRepository.findById(idPessoa);

		if (pessoa.isEmpty() || pessoa.get().isInativo())
			throw new PessoaInexistenteOuInativaException();

	}

	public Lancamento update(Long id, Lancamento lancamento) {

		Lancamento lancamentoSalvo = lancamentoRepository.findById(id)
				.orElseThrow(() -> new LancamentoInexistenteException());

		validarPessoa(lancamentoSalvo);

		if (!StringUtils.hasText(lancamento.getAnexo()) && StringUtils.hasText(lancamentoSalvo.getAnexo()))
			s3.remover(lancamentoSalvo.getAnexo());
		else if (StringUtils.hasText(lancamento.getAnexo())
				&& !lancamento.getAnexo().equals(lancamentoSalvo.getAnexo()))
			s3.substituir(lancamentoSalvo.getAnexo(), lancamento.getAnexo());

		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "id");

		return lancamentoRepository.save(lancamentoSalvo);

	}

}
