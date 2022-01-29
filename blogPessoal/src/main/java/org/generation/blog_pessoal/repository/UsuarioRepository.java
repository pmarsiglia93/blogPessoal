package org.generation.blog_pessoal.repository;

import java.util.Optional;

import org.generation.blog_pessoal.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

//extends --> herdar
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	public Optional<Usuario> findByUsuario(String usuario);
}
