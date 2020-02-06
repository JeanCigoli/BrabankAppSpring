package br.com.senai.jeancigoli.brabankapi.services;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.senai.jeancigoli.brabankapi.domain.Usuario;
import br.com.senai.jeancigoli.brabankapi.repository.UsuarioRepository;
import br.com.senai.jeancigoli.brabankapi.services.exceptions.UsuarioNaoEncontradoException;

@Service
public class UsuariosService {

	@Autowired
	UsuarioRepository usuarioRepository;
	
	public List<Usuario> listAll(){
		return usuarioRepository.findAll();
	}
	
	public Usuario listById(Long id) {
		Usuario usuario = usuarioRepository.findById(id).orElse(null);
		
		if(usuario == null) {
			throw new UsuarioNaoEncontradoException("O usuário não pode ser localizado!");
		}
		
		return usuario;
	}
	
	public Usuario insert(Usuario usuario) throws SQLIntegrityConstraintViolationException {
		usuario.id = null;
		return usuarioRepository.save(usuario);
		
	}
	
	public void edit(Usuario usuario) {
		
		verificarExistencia(usuario);
		usuarioRepository.save(usuario);
		
	}
	
	private void verificarExistencia(Usuario usuario) {
		listById(usuario.id);
	}
	
	public void deletar(Long id) {
		
		try {	
			usuarioRepository.deleteById(id);	
		} catch (EmptyResultDataAccessException e) {	
			throw new UsuarioNaoEncontradoException("O usuário não pode ser localizado!");
		}
	}
	
	public Usuario selectByEmail (String email) {
		
		Usuario usuario = usuarioRepository.findByEmail(email);
		
		if(usuario == null) {
			throw new UsuarioNaoEncontradoException("O usuário não pode ser localizado!");
		}
		
		return usuario;
	}
	
}
