package br.com.senai.jeancigoli.brabankapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.senai.jeancigoli.brabankapi.domain.Categoria;

/*
 	Mapeia a tabela usuário para manipular os registros;
 	passamos dois parametros
  	primeiro a entidade Usuario e o tipo di atributo identificador 
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	
	
}
