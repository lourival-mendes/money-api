package com.algaworks.algamoneyapi.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.algaworks.algamoneyapi.model.Lancamento;
import com.algaworks.algamoneyapi.resource.repository.filter.LancamentoFilter;

public class LacamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<Lancamento> filter(LancamentoFilter lancamentoFilter) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);

		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<Lancamento> query = manager.createQuery(criteria);

		return query.getResultList();
	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<Lancamento> root) {

		List<Predicate> predicates = new ArrayList<>();

		if (!lancamentoFilter.getDescricao().isBlank())
			predicates.add(builder.like(builder.lower(root.get("descricao")),
					"%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		/*
		 * if (lancamentoFilter.getDataVencimentoDe().equals(null)) predicates.add(e);
		 * 
		 * if (lancamentoFilter.getDataVecimentoAte().equals(null)) predicates.add(e);
		 */
		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
