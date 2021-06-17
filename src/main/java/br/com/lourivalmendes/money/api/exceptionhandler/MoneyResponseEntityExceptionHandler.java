package br.com.lourivalmendes.money.api.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.lourivalmendes.money.api.service.exception.LancamentoInexistenteException;
import br.com.lourivalmendes.money.api.service.exception.PessoaComLancamentoException;
import br.com.lourivalmendes.money.api.service.exception.PessoaInexistenteOuInativaException;
import lombok.Getter;
import lombok.Setter;

@ControllerAdvice
public class MoneyResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = Optional.ofNullable(ex.getCause()).orElse(ex).toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<Erro> erros = criarListaDeErros(ex.getBindingResult());
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);

	}

	private List<Erro> criarListaDeErros(BindingResult bindingResult) {

		List<Erro> erros = new ArrayList<>();

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			String mensagemUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			String mensagemDesenvolvedor = fieldError.toString();
			erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		}
		return erros;

	}

	@Getter
	@Setter
	public static class Erro {

		private String mensagemUsuario;
		private String mensagemDesenvolvedor;

		public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
			this.mensagemUsuario = mensagemUsuario;
			this.mensagemDesenvolvedor = mensagemDesenvolvedor;
		}
	}

	@ExceptionHandler({ EmptyResultDataAccessException.class })
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,
			WebRequest request) {

		String mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = Optional.ofNullable(ex.getCause()).orElse(ex).toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

	}

	@ExceptionHandler({ java.sql.SQLIntegrityConstraintViolationException.class })
	public ResponseEntity<Object> SQLIntegrityConstraintViolationException(
			java.sql.SQLIntegrityConstraintViolationException ex, WebRequest request) {

		String mensagemUsuario = messageSource.getMessage("recurso.operacao-nao-permitida", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = Optional.ofNullable(ex.getCause()).orElse(ex).toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

	}

	@ExceptionHandler({ PessoaComLancamentoException.class })
	public ResponseEntity<Object> PessoaComLancamentoException(PessoaComLancamentoException ex, WebRequest request) {

		String mensagemUsuario = messageSource.getMessage("recurso.pessoa-com-lancamento", null,
				LocaleContextHolder.getLocale());

		String mensagemDesenvolvedor = Optional.ofNullable(ex.getCause()).orElse(ex).toString();

		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

	}

	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	public ResponseEntity<Object> PessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex,
			WebRequest request) {

		String mensagemUsuario = messageSource.getMessage("recurso.pessoa-inexistente-ou-inativa", null,
				LocaleContextHolder.getLocale());

		String mensagemDesenvolvedor = Optional.ofNullable(ex.getCause()).orElse(ex).toString();

		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

	}

	@ExceptionHandler({ LancamentoInexistenteException.class })
	public ResponseEntity<Object> LancamentoInexistenteException(LancamentoInexistenteException ex,
			WebRequest request) {

		String mensagemUsuario = messageSource.getMessage("recurso.lancamento-inexistente", null,
				LocaleContextHolder.getLocale());

		String mensagemDesenvolvedor = Optional.ofNullable(ex.getCause()).orElse(ex).toString();

		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

	}
}
