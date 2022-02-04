package org.generation.blog_pessoal.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsuarioModelTest {
	
public Usuario usuario;
	
	@Autowired
	private  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();
	
	@BeforeEach
	public void start() {
		usuario = new Usuario(0L, "PauloTech", "Paulo Tester", "123456789");
	}

	@Test
	void testValidaAtributos() {
		Set<ConstraintViolation<Usuario>> valida = validator.validate(usuario);
		System.out.println(valida.toString());
		assertTrue(valida.isEmpty());
	}
	
	@Test
	void testValidaAtributosNulos() {
		Usuario usuarioErro = new Usuario();
		usuarioErro.setUsuario("Gusobel");
		Set<ConstraintViolation<Usuario>> error = validator.validate(usuarioErro);
		System.out.println(error.toString());
		assertFalse(error.isEmpty());
	}

}
