package br.com.lourivalmendes.money.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.lourivalmendes.money.api.model.Categoria;
import br.com.lourivalmendes.money.api.repository.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	public Categoria update(Long id, Categoria categoria) {

		Categoria categoriaSalva = categoriaRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));

		BeanUtils.copyProperties(categoria, categoriaSalva, "id");

		categoriaRepository.save(categoriaSalva);

		return categoriaRepository.save(categoriaSalva);

	}

}
