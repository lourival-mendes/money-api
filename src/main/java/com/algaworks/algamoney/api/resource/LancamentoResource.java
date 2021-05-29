package com.algaworks.algamoney.api.resource;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.algamoney.api.dto.AnexoDTO;
import com.algaworks.algamoney.api.dto.LancamentoEstatisticaCategoriaDTO;
import com.algaworks.algamoney.api.dto.LancamentoEstatisticaDiaDTO;
import com.algaworks.algamoney.api.dto.LancamentoEstatisticaPessoaDTO;
import com.algaworks.algamoney.api.event.RecursoAtualizadoEvent;
import com.algaworks.algamoney.api.event.RecursoCriadoEvent;
import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.repository.LancamentoRepository;
import com.algaworks.algamoney.api.repository.filter.LancamentoFilter;
import com.algaworks.algamoney.api.repository.projection.LancamentoResumo;
import com.algaworks.algamoney.api.service.LancamentoService;
import com.algaworks.algamoney.api.storage.S3;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private S3 s3;

	@PostMapping("/anexo")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public AnexoDTO uploadAnexo(@RequestParam MultipartFile anexo) throws IOException {

		String nome = s3.salvarTemporariamente(anexo);
		return new AnexoDTO(nome, s3.configurarUrl(nome));

	}

	@GetMapping("/estatisticas/por-categoria/{mesReferecia}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<LancamentoEstatisticaCategoriaDTO> listarEstatisticaCategoria(
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate mesReferecia) {
		return lancamentoRepository.listarEstatisticaCategoria(mesReferecia);
	}

	@GetMapping("/estatisticas/por-dia/{mesReferecia}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<LancamentoEstatisticaDiaDTO> listarEstatisticaDia(
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate mesReferecia) {
		return lancamentoRepository.listarEstatisticaDia(mesReferecia);
	}

	@GetMapping("/estatisticas/por-pessoa")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<LancamentoEstatisticaPessoaDTO> listarEstatisticaPessoa(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataInicial,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataFinal) {
		return lancamentoRepository.listarEstatisticaPessoa(dataInicial, dataFinal);
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.filtrar(lancamentoFilter, pageable);
	}

	@GetMapping(params = "resumo")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<LancamentoResumo> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.resumir(lancamentoFilter, pageable);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {

		Lancamento lancamentoSalvo = lancamentoService.save(lancamento);

		applicationEventPublisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> atualizar(@Valid @RequestBody Lancamento lancamento, @PathVariable Long id,
			HttpServletResponse response) {

		Lancamento lancamentoAtualizado = lancamentoService.update(id, lancamento);

		applicationEventPublisher.publishEvent(new RecursoAtualizadoEvent(this, response));

		return ResponseEntity.status(HttpStatus.OK).body(lancamentoAtualizado);

	}

	@PutMapping("/{id}/datapagamento")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarAtivo(@RequestBody LocalDate dataPagamento, @PathVariable Long id,
			HttpServletResponse response) {

		Lancamento lancamentoSalvo = lancamentoRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));

		lancamentoSalvo.setDataPagamento(dataPagamento);
		lancamentoRepository.save(lancamentoSalvo);

		applicationEventPublisher.publishEvent(new RecursoAtualizadoEvent(this, response));

	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Lancamento> buscarPeloId(@PathVariable Long id) {
		Lancamento lancamentoSalvo = lancamentoRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		return ResponseEntity.ok(lancamentoSalvo);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removerPeloId(@PathVariable Long id) {
		lancamentoRepository.deleteById(id);
	}

}