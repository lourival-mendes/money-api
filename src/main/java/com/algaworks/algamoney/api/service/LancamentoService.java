package com.algaworks.algamoney.api.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.algaworks.algamoney.api.exceptionhandler.AlgaMoneyResponseEntityExceptionHandler.Erro;
import com.algaworks.algamoney.api.mail.Mailer;
import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.model.Usuario;
import com.algaworks.algamoney.api.repository.LancamentoRepository;
import com.algaworks.algamoney.api.repository.PessoaRepository;
import com.algaworks.algamoney.api.repository.UsuarioRepository;
import com.algaworks.algamoney.api.service.exception.LancamentoInexistenteException;
import com.algaworks.algamoney.api.service.exception.PessoaInexistenteOuInativaException;
import com.algaworks.algamoney.api.storage.S3;

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
	private MessageSource messageSource;

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

//		if (StringUtils.hasText(lancamentoSalvo.getAnexo()))
//			lancamentoSalvo.setUrlAnexo(s3.configurarUrl(lancamentoSalvo.getAnexo()));

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

	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	public ResponseEntity<Object> PessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {

		String mensagemUsuario = messageSource.getMessage("recurso.pessoa-inexistente-ou-inativa", null,
				LocaleContextHolder.getLocale());

		String mensagemDesenvolvedor = Optional.ofNullable(ex.getCause()).orElse(ex).toString();

		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

		return ResponseEntity.badRequest().body(erros);

	}

	@ExceptionHandler({ LancamentoInexistenteException.class })
	public ResponseEntity<Object> LancamentoInexistenteException(LancamentoInexistenteException ex) {

		String mensagemUsuario = messageSource.getMessage("recurso.lancamento-inexistente", null,
				LocaleContextHolder.getLocale());

		String mensagemDesenvolvedor = Optional.ofNullable(ex.getCause()).orElse(ex).toString();

		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

		return ResponseEntity.badRequest().body(erros);

	}
}
