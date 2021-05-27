package com.algaworks.algamoney.api.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.algaworks.algamoney.api.mail.Mailer;
import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.model.Usuario;
import com.algaworks.algamoney.api.repository.LancamentoRepository;
import com.algaworks.algamoney.api.repository.PessoaRepository;
import com.algaworks.algamoney.api.repository.UsuarioRepository;
import com.algaworks.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private Mailer mailer;

	@Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
	public void avisarSobreLancamentosVencidos() {

		List<Lancamento> lancamentosVencidos = lancamentoRepository
				.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());

		if (!lancamentosVencidos.isEmpty()) {

			List<Usuario> usuarios = usuarioRepository.findByPermissoesDescricao("ROLE_PESQUISAR_LANCAMENTO");

			Map<String, Object> variaveis = new HashMap<>();
			variaveis.put("lancamentos", lancamentosVencidos);

			String template = "mail/aviso-lancamentos-vencidos";

			this.mailer.avisarSobreLancamentosVencidos(lancamentosVencidos, usuarios, template);
		} else {
			System.out.println("Não há lançamentos vencidos. Nenhum e-mail foi enviado.");
		}

	}

	public Lancamento save(Lancamento lancamento) {

		Pessoa pessoaLancamento = lancamento.getPessoa();
		Optional<Pessoa> pessoaSalva = pessoaRepository.findById(pessoaLancamento.getId());

		if (pessoaSalva.isEmpty() || pessoaSalva.get().isInativo())
			throw new PessoaInexistenteOuInativaException();

		return lancamentoRepository.save(lancamento);

	}

}
