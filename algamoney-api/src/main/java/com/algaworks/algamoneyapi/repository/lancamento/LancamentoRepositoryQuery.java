package com.algaworks.algamoneyapi.repository.lancamento;

import java.util.List;

import com.algaworks.algamoneyapi.model.Lancamento;
import com.algaworks.algamoneyapi.resource.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {

	public List<Lancamento> filter(LancamentoFilter lancamentoFilter);

}
