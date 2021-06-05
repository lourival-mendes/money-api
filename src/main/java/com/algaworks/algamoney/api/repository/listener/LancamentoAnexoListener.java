package com.algaworks.algamoney.api.repository.listener;

import org.springframework.util.StringUtils;

import com.algaworks.algamoney.api.AlgamoneyApiApplication;
import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.storage.S3;

public class LancamentoAnexoListener {

	// @PostLoad
	public void PostLoad(Lancamento lancamento) {

		if (StringUtils.hasText(lancamento.getAnexo())) {

			S3 s3 = AlgamoneyApiApplication.getBean(S3.class);
			lancamento.setUrlAnexo(s3.configurarUrl(lancamento.getAnexo()));

		}

	}

}
