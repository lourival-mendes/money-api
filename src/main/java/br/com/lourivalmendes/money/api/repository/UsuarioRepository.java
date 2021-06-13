package br.com.lourivalmendes.money.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lourivalmendes.money.api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByEmail(String email);

	List<Usuario> findByPermissoesDescricao(String permissaoDescricao);

}
