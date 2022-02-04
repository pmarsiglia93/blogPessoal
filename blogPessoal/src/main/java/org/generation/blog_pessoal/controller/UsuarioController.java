package org.generation.blog_pessoal.controller;

import java.util.List;
import java.util.Optional;

import org.generation.blog_pessoal.model.UserLogin;
import org.generation.blog_pessoal.model.Usuario;
import org.generation.blog_pessoal.repository.UsuarioRepository;
import org.generation.blog_pessoal.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository repository;
	
	@PostMapping("/logar")
	public ResponseEntity<UserLogin> Authentication(@RequestBody Optional<UserLogin> user){
		return usuarioService.Logar(user).map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
	
	/*@PostMapping("/cadastrar")
	public ResponseEntity<Usuario> Post(@RequestBody Usuario usuario){
		return usuarioService.cadastrarUsuario(usuario)
		.map(resp -> ResponseEntity.status(HttpStatus.CREATED).body(resp))
		.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
	}
	*/
			
	@PostMapping("/cadastrar")
    public ResponseEntity<Usuario> Post(@RequestBody Usuario usuario){
        return usuarioService.cadastrarUsuario(usuario);
    }

	
	@GetMapping("/all")
	public ResponseEntity<List<Usuario>> findAllUsuario(){
		List<Usuario> List = repository.findAll();
		if(List.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(List);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> findById(@PathVariable long id) {
		return repository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping("/save")
	public ResponseEntity<Usuario> saveUser(@RequestBody Usuario newUser){
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(newUser));
	}
	
	@PutMapping("/edit")
	public ResponseEntity<Usuario> editUser(@RequestBody Usuario newUser){
		return ResponseEntity.status(HttpStatus.OK).body(repository.save(newUser));
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteUser(@PathVariable long id) {
		repository.deleteById(id);
	}

}
