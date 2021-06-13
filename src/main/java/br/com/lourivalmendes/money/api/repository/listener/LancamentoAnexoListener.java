package br.com.lourivalmendes.money.api.repository.listener;

import org.springframework.util.StringUtils;

import br.com.lourivalmendes.money.api.MoneyApiApplication;
import br.com.lourivalmendes.money.api.model.Lancamento;
import br.com.lourivalmendes.money.api.storage.S3;

public class LancamentoAnexoListener {

	// @PostLoad
	public void PostLoad(Lancamento lancamento) {

		if (StringUtils.hasText(lancamento.getAnexo())) {

			S3 s3 = MoneyApiApplication.getBean(S3.class);
			lancamento.setUrlAnexo(s3.configurarUrl(lancamento.getAnexo()));

		}

	}

}
