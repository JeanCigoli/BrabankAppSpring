package br.com.senai.jeancigoli.brabankapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.senai.jeancigoli.brabankapi.domain.Categoria;
import br.com.senai.jeancigoli.brabankapi.repository.CategoriaRepository;
import br.com.senai.jeancigoli.brabankapi.services.exceptions.CategoriaNaoEncontradaException;

@Service
public class CategoriasService {

	@Autowired
	CategoriaRepository categoriaRepository;
	
	public List<Categoria> listAll(){
		return categoriaRepository.findAll();
	}
	
	public Categoria listById(Long id) {
		Categoria categoria = categoriaRepository.findById(id).orElse(null);
		
		if(categoria == null) {
			throw new CategoriaNaoEncontradaException("O categoria não pode ser localizada!");
		}
		
		return categoria;
	}
	
	public Categoria insert(Categoria categoria) {
		
		categoria.id = null;
		return categoriaRepository.save(categoria);
		
	}
	
	public void edit(Categoria categoria) {
		
		verificarExistencia(categoria);
		categoriaRepository.save(categoria);
		
	}
	
	private void verificarExistencia(Categoria categoria) {
		listById(categoria.id);
	}
	
	public void deletar(Long id) {
		
		try {	
			categoriaRepository.deleteById(id);	
		} catch (EmptyResultDataAccessException e) {	
			throw new CategoriaNaoEncontradaException("O categoria não pode ser localizada!");
		}
	}
	
}
