package org.generation.blog_pessoal.controller;


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

import java.util.List;

import org.generation.blog_pessoal.model.Postagem;
import org.generation.blog_pessoal.repository.PostagemRepository;

@RestController
@RequestMapping("/postagens")
@CrossOrigin("*")
public class PostagemController {
	
	@Autowired
	private PostagemRepository repository;
	
	@GetMapping // findAll --> com a capacidade de trazer todas as postagens.
	public ResponseEntity<List<Postagem>> GetAll(){
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id}") //findById --> com a capacidade de trazer uma única postagem por Id.
	public ResponseEntity<Postagem> GetById(@PathVariable long id){ //PathVariable --> Pega o valor que vem pela URL
		return repository.findById(id)
				.map(resp -> ResponseEntity.ok(resp)) // Resposta Positiva
				.orElse(ResponseEntity.notFound().build()); //404 Not Found
		
	}
	
	@GetMapping("/titulo/{titulo}") //para evitar duplicidade e o back-end não confundir as rotas
	public ResponseEntity<List<Postagem>> GetByTitulo(@PathVariable String titulo){ // getByTitulo
		return ResponseEntity.ok(repository.findAllByTituloContainingIgnoreCase(titulo)); // endPoint com a função de trazer uma postagem.
	} //end-point para GET sub rota para evitar duplicidade, utilizamos o metodo dentro do repository para trazer todos os valores.
	
	@PostMapping // postPostagem --> endPoint com função de gravar uma nova postagem no banco de dados.
	public ResponseEntity<Postagem> post (@RequestBody Postagem postagem){
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(postagem));
	}
	
	@PutMapping // putPostagem --> endPoint com função de atualizar os dados da postagem.
	public ResponseEntity<Postagem> put (@RequestBody Postagem postagem){
		return ResponseEntity.status(HttpStatus.OK).body(repository.save(postagem));
		
	}
	
	@DeleteMapping("/{id}") // deletePostagem --> endPoint com a função de apagar uma postagem do banco de dados.
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}		
	
}
