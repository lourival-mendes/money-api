package br.com.lourivalmendes.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lourivalmendes.money.api.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
