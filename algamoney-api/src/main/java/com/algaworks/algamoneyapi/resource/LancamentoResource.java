package com.algaworks.algamoneyapi.resource;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algamoneyapi.event.RecursoAtualizadoEvent;
import com.algaworks.algamoneyapi.event.RecursoCriadoEvent;
import com.algaworks.algamoneyapi.model.Lancamento;
import com.algaworks.algamoneyapi.repository.LancamentoRepository;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@GetMapping
	public List<Lancamento> listar() {
		return lancamentoRepository.findAll();
	}

	@PostMapping
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {

		Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);

		applicationEventPublisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Lancamento> atualizar(@Valid @RequestBody Lancamento lancamento, @PathVariable Long id,
			HttpServletResponse response) {

		Lancamento lancamentoSalvo = this.lancamentoRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));

		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "id");

		Lancamento lancamentoAtualizado = lancamentoRepository.save(lancamentoSalvo);
		applicationEventPublisher.publishEvent(new RecursoAtualizadoEvent(this, response));
		return ResponseEntity.status(HttpStatus.OK).body(lancamentoAtualizado);

	}

	@PutMapping("/{id}/datapagamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarAtivo(@RequestBody LocalDate dataPagamento, @PathVariable Long id,
			HttpServletResponse response) {

		Lancamento lancamentoSalvo = this.lancamentoRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));

		lancamentoSalvo.setDataPagamento(dataPagamento);
		lancamentoRepository.save(lancamentoSalvo);

		applicationEventPublisher.publishEvent(new RecursoAtualizadoEvent(this, response));

	}

	@GetMapping("/{id}")
	public ResponseEntity<Lancamento> buscarPeloId(@PathVariable Long id) {
		Lancamento lancamentoSalvo = lancamentoRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		return ResponseEntity.ok(lancamentoSalvo);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removerPeloId(@PathVariable Long id) {
		lancamentoRepository.deleteById(id);
	}
}