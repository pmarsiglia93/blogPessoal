package org.generation.blog_pessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.generation.blog_pessoal.model.UserLogin;
import org.generation.blog_pessoal.model.Usuario;
import org.generation.blog_pessoal.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

	@Service
	public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	
	/*
	 * public Optional<Usuario> cadastrarUsuario(Usuario usuario) {
	 
		Optional<Usuario> optional = repository.findByUsuario(usuario.getUsuario());
		if(optional.isPresent()) {
			Optional.empty();
		}			
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		String senhaEncoder = encoder.encode(usuario.getSenha());
		usuario.setSenha(senhaEncoder);
		
		return Optional.ofNullable(repository.save(usuario));
	}
	*/
	
	public ResponseEntity<Usuario> cadastrarUsuario(Usuario usuario){
		Optional<Usuario> optional = repository.findByUsuario(usuario.getUsuario());
		if(optional.isPresent()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String senhaEncoder = encoder.encode(usuario.getSenha());
		usuario.setSenha(senhaEncoder);
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(usuario));
		}	
	
	public Optional<UserLogin> Logar(Optional<UserLogin> user){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Optional<Usuario> usuario = repository.findByUsuario(user.get().getUsuario());
		
		if(usuario.isPresent()) {
			if(encoder.matches(user.get().getSenha(), usuario.get().getSenha())) {
				
				String auth = user.get().getUsuario() + ":" + user.get().getSenha();
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);
				
				user.get().setToken(authHeader);
				user.get().setNome(usuario.get().getNome());
				
				return user;
			}
		}
		
		return null;
	}
}
