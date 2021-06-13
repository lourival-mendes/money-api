package br.com.lourivalmendes.money.api.repository.lancamento;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.lourivalmendes.money.api.dto.LancamentoEstatisticaCategoriaDTO;
import br.com.lourivalmendes.money.api.dto.LancamentoEstatisticaDiaDTO;
import br.com.lourivalmendes.money.api.dto.LancamentoEstatisticaPessoaDTO;
import br.com.lourivalmendes.money.api.model.Lancamento;
import br.com.lourivalmendes.money.api.repository.filter.LancamentoFilter;
import br.com.lourivalmendes.money.api.repository.projection.LancamentoResumo;

public interface LancamentoRepositoryQuery {

	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);

	public Page<LancamentoResumo> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);

	public List<LancamentoEstatisticaCategoriaDTO> listarEstatisticaCategoria(LocalDate mesReferencia);

	public List<LancamentoEstatisticaDiaDTO> listarEstatisticaDia(LocalDate mesReferencia);

	public List<LancamentoEstatisticaPessoaDTO> listarEstatisticaPessoa(LocalDate dataInicial, LocalDate dataFinal);

}
