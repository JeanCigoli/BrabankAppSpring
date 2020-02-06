package br.com.senai.jeancigoli.brabankapi.resources;

import java.net.URI;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.senai.jeancigoli.brabankapi.domain.Usuario;
import br.com.senai.jeancigoli.brabankapi.repository.UsuarioRepository;
import br.com.senai.jeancigoli.brabankapi.services.UsuariosService;
import br.com.senai.jeancigoli.brabankapi.services.exceptions.UsuarioNaoEncontradoException;


/*
 	O usuario service precisa ser bean
	Informa que aqui é a controller, processa e devolve a resposta 
*/

@RestController
/* Serve para informar que quando estiver '/usuarios' será rodado este método */
@RequestMapping("/usuarios")
public class UsuariosResource {
	
	/* 
	 	Verifica se esta classe está instaciada, senão instancia ela;
	 	Isso é chamado de injeção de dependencia
	*/
	@Autowired
	UsuariosService usuarioService;

	/* Esse método será só para quando chamar "/usuarios" */
	@GetMapping
	public List<Usuario> selectAllUsuario() {
		
		return usuarioService.listAll();
	
	}
	
	/* Isso é uma criação de um subrecurso, dentro de usuarios, trazendo o id */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Usuario> selectByIdUsuario(@PathVariable("id") Long codigo) {
		
		Usuario usuario = usuarioService.listById(codigo);
		return ResponseEntity.ok(usuario);
		
	}
	
	@GetMapping(value = "/email/{email}")
	public ResponseEntity<Usuario> selectByEmail(@PathVariable("email") String email){
		
		Usuario usuario = usuarioService.selectByEmail(email);
		return ResponseEntity.ok(usuario);
		
	}
	
	/* ----------------------- POST ------------------  */
	
	/* 
	 	Método para inserir um usuário 
	 	@PostMapping -> Mapeando que será post esse método 
	 	@RequestBody -> Serve para falar que o usuarios vai receber o body da requisição 
	*/
	@PostMapping
	public ResponseEntity<Void> insertUser(@RequestBody Usuario usuario) {
		
		try {
			usuario = usuarioService.insert(usuario);
		} catch (SQLIntegrityConstraintViolationException e) {
			/* Retorna que a requisição foi incorreta */
			return ResponseEntity.badRequest().build();
		}
		
		// Criar um URI com o location do dado salvo
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(usuario.id).toUri();
		
		return ResponseEntity.created(uri).build();
		
	}
	
	
	/* ----------------------- PUT ------------------  */
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> updateUser(@PathVariable("id") Long id, @RequestBody Usuario usuario) {
		
		usuario.id = id;
		usuarioService.edit(usuario);
		
		return ResponseEntity.ok().build();
		
	}
	
	
	/* ----------------------- DELETE ------------------  */
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {	
			
		usuarioService.deletar(id);
		return ResponseEntity.noContent().build();
		
	}
	

}
