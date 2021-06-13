package br.com.lourivalmendes.money.api.repository.lancamento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.lourivalmendes.money.api.dto.LancamentoEstatisticaCategoriaDTO;
import br.com.lourivalmendes.money.api.dto.LancamentoEstatisticaDiaDTO;
import br.com.lourivalmendes.money.api.dto.LancamentoEstatisticaPessoaDTO;
import br.com.lourivalmendes.money.api.model.Categoria_;
import br.com.lourivalmendes.money.api.model.Lancamento;
import br.com.lourivalmendes.money.api.model.Lancamento_;
import br.com.lourivalmendes.money.api.model.Pessoa_;
import br.com.lourivalmendes.money.api.repository.filter.LancamentoFilter;
import br.com.lourivalmendes.money.api.repository.projection.LancamentoResumo;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);

		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<Lancamento> query = manager.createQuery(criteria);

		adicionarRestricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	private Long total(LancamentoFilter lancamentoFilter) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		criteria.select(builder.count(root));

		return manager.createQuery(criteria).getSingleResult();
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {

		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);

	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<Lancamento> root) {

		List<Predicate> predicates = new ArrayList<>();

		if (lancamentoFilter.getDescricao() != null)
			predicates.add(builder.like(builder.lower(root.get(Lancamento_.descricao)),
					"%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));

		if (lancamentoFilter.getDataVencimentoDe() != null)
			predicates.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento),
					lancamentoFilter.getDataVencimentoDe()));

		if (lancamentoFilter.getDataVencimentoAte() != null)
			predicates.add(builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento),
					lancamentoFilter.getDataVencimentoAte()));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public Page<LancamentoResumo> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<LancamentoResumo> criteria = builder.createQuery(LancamentoResumo.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);

		criteria.select(builder.construct(LancamentoResumo.class, root.get(Lancamento_.id),
				root.get(Lancamento_.descricao), root.get(Lancamento_.dataVencimento),
				root.get(Lancamento_.dataPagamento), root.get(Lancamento_.valor), root.get(Lancamento_.tipo),
				root.get(Lancamento_.categoria).get(Categoria_.nome), root.get(Lancamento_.pessoa).get(Pessoa_.nome)));

		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<LancamentoResumo> query = manager.createQuery(criteria);

		adicionarRestricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	@Override
	public List<LancamentoEstatisticaCategoriaDTO> listarEstatisticaCategoria(LocalDate mesReferencia) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery<LancamentoEstatisticaCategoriaDTO> criteriaQuery = criteriaBuilder
				.createQuery(LancamentoEstatisticaCategoriaDTO.class);

		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaCategoriaDTO.class,
				root.get(Lancamento_.categoria), criteriaBuilder.sum(root.get(Lancamento_.valor))));

		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());

		criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), primeiroDia),
				criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), ultimoDia));

		criteriaQuery.groupBy(root.get(Lancamento_.categoria));

		TypedQuery<LancamentoEstatisticaCategoriaDTO> typedQuery = manager.createQuery(criteriaQuery);

		return typedQuery.getResultList();
	}

	@Override
	public List<LancamentoEstatisticaDiaDTO> listarEstatisticaDia(LocalDate mesReferencia) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery<LancamentoEstatisticaDiaDTO> criteriaQuery = criteriaBuilder
				.createQuery(LancamentoEstatisticaDiaDTO.class);

		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaDiaDTO.class, root.get(Lancamento_.tipo),
				root.get(Lancamento_.dataVencimento), criteriaBuilder.sum(root.get(Lancamento_.valor))));

		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());

		criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), primeiroDia),
				criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), ultimoDia));

		criteriaQuery.groupBy(root.get(Lancamento_.tipo), root.get(Lancamento_.dataVencimento));

		TypedQuery<LancamentoEstatisticaDiaDTO> typedQuery = manager.createQuery(criteriaQuery);

		return typedQuery.getResultList();
	}

	@Override
	public List<LancamentoEstatisticaPessoaDTO> listarEstatisticaPessoa(LocalDate dataInicial, LocalDate dataFinal) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery<LancamentoEstatisticaPessoaDTO> criteriaQuery = criteriaBuilder
				.createQuery(LancamentoEstatisticaPessoaDTO.class);

		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaPessoaDTO.class, root.get(Lancamento_.tipo),
				root.get(Lancamento_.pessoa), criteriaBuilder.sum(root.get(Lancamento_.valor))));

		criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), dataInicial),
				criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), dataFinal));

		criteriaQuery.groupBy(root.get(Lancamento_.tipo), root.get(Lancamento_.pessoa));

		TypedQuery<LancamentoEstatisticaPessoaDTO> typedQuery = manager.createQuery(criteriaQuery);

		return typedQuery.getResultList();
	}

}
