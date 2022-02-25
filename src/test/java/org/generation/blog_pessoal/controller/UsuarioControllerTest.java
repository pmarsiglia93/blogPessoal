package org.generation.blog_pessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.generation.blog_pessoal.model.Usuario;
import org.generation.blog_pessoal.service.UsuarioService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {
	
	// Injeta um objeto de classe TestRestTemplate, responsável por fazer requisição HTTP (semelhante ao Postman)
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UsuarioService usuarioService;
	
	//Método 01 - Cadastrar Usuário
	
	@Test
	@Order(1)
	@Disabled
	@DisplayName("Cadastrar Um Usuário")
	public void deveCriarUmUsuario() {
		
		//Cria um objeto da Classe Usuario e insere dentro de um Objeto da Classe HttpEntity (Entidade HTTP)
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, "Paulo Antunes", "paulo_antunes@email.com.br", "13465278"));
				
		//Cria um Objeto da Classe ResponseEntity (resposta), que receberá a Resposta da Requisição que será enviada pelo Objeto da Classe TestRestTemplate.		 
		//Na requisição HTTP será enviada a URL do recurso (/usuarios/cadastrar), o verbo (POST), a entidade HTTP criada acima (requisicao) e a Classe de retornos da Resposta (Usuario).		
		ResponseEntity<Usuario> resposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		
		//Verifica se a requisição retornou o Status Code CREATED (201), Se for verdadeira, o teste passa, se não, o teste falha.
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());
	}
	
	//Método 02 - Não deve permitir duplicação do Usuário
	
	@Test
	@Order(2)
	@Disabled
	@DisplayName("Não deve permitir duplicação do Usuário")
	public void naoDeveDuplicarUsuario() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L,"Maria da Silva", "maria_silva@email.com.br", "13465278"));		
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,"Maria da Silva", "maria_silva@email.com.br", "13465278"));		
						
		ResponseEntity<Usuario> resposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);		
		
		assertEquals(HttpStatus.UNAUTHORIZED, resposta.getStatusCode());
	}
	
	//Método 03 - Alterar um usuário
	
	@Test
	@Order(3)
	@Disabled
	@DisplayName("Alterar um Usuário")
	public void deveAtualizarUmUsuario(){
		
		ResponseEntity<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, "Juliana Andrews", "juliana_andrews@email.com.br", "juliana123"));
		
		Usuario usuarioUpdate = new Usuario(usuarioCadastrado.getBody().getId(),"Juliana Andrews Ramos", "juliana_andrews@email.com.br", "juliana123");
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);
		
		ResponseEntity<Usuario> resposta = testRestTemplate
				.withBasicAuth("juliana_andrews@email.com.br", "juliana123")
				.exchange("/usuarios/edit", HttpMethod.PUT, requisicao, Usuario.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome()); 
		
	}
	
	@Test
	@Order(4)
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosUsuarios() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123"));
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123"));
		
		@SuppressWarnings("rawtypes")
		ResponseEntity<List> resposta = testRestTemplate
				.withBasicAuth("sabrina_sanches@email.com.br", "sabrina123")
				.exchange("/usuarios/all", HttpMethod.GET, null, List.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals(2, resposta.getBody().size());
		
	}

}
