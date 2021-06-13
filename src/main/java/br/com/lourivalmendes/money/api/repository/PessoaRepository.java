package br.com.lourivalmendes.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lourivalmendes.money.api.model.Pessoa;
import br.com.lourivalmendes.money.api.repository.pessoa.PessoaRepositoryQuery;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQuery {

}
