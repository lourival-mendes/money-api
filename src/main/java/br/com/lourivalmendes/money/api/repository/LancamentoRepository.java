package br.com.lourivalmendes.money.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lourivalmendes.money.api.model.Lancamento;
import br.com.lourivalmendes.money.api.model.Pessoa;
import br.com.lourivalmendes.money.api.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

	List<Lancamento> findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate data);

	List<Lancamento> findByPessoa(Pessoa pessoa);

}
