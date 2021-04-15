package com.algaworks.algamoneyapi.model.metaModel;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.algaworks.algamoneyapi.model.Endereco;
import com.algaworks.algamoneyapi.model.Pessoa;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Pessoa.class)
public abstract class Pessoa_ {

	public static volatile SingularAttribute<Pessoa, Boolean> ativo;
	public static volatile SingularAttribute<Pessoa, Endereco> endereco;
	public static volatile SingularAttribute<Pessoa, String> nome;
	public static volatile SingularAttribute<Pessoa, Long> id;

	public static final String ATIVO = "ativo";
	public static final String ENDERECO = "endereco";
	public static final String NOME = "nome";
	public static final String ID = "id";

}
