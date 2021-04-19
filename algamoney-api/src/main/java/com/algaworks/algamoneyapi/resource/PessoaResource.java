package com.algaworks.algamoneyapi.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.algaworks.algamoneyapi.model.Pessoa;
import com.algaworks.algamoneyapi.repository.PessoaRepository;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public List<Pessoa> listar() {
		return pessoaRepository.findAll();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Pessoa> criar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {

		Pessoa pessoaCriada = pessoaRepository.save(pessoa);
		applicationEventPublisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaCriada.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaCriada);

	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Pessoa> atualizar(@Valid @RequestBody Pessoa pessoa, @PathVariable Long id,
			HttpServletResponse response) {

		Pessoa pessoaSalva = pessoaRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));

		BeanUtils.copyProperties(pessoa, pessoaSalva, "id");

		Pessoa pessoaAtualizada = pessoaRepository.save(pessoaSalva);
		applicationEventPublisher.publishEvent(new RecursoAtualizadoEvent(this, response));
		return ResponseEntity.status(HttpStatus.OK).body(pessoaAtualizada);

	}

	@PutMapping("/{id}/ativo")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarAtivo(@RequestBody Boolean ativo, @PathVariable Long id, HttpServletResponse response) {

		Pessoa pessoaSalva = pessoaRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));

		pessoaSalva.setAtivo(ativo);
		pessoaRepository.save(pessoaSalva);

		applicationEventPublisher.publishEvent(new RecursoAtualizadoEvent(this, response));

	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public ResponseEntity<Pessoa> buscarPeloId(@PathVariable Long id) {
		Pessoa pessoaSalva = pessoaRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
		return pessoaSalva.equals(null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(pessoaSalva);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and #oauth2.hasScope('write')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removerPeloId(@PathVariable Long id) {
		pessoaRepository.deleteById(id);
	}

}