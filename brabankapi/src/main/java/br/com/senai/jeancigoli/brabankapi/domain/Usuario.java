package br.com.senai.jeancigoli.brabankapi.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.UniqueElements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/* Informa que está classe é uma Entidade */
@Entity
public class Usuario {

	/* Devemos falar qual é a chave primaria da tabela, senão ocorre erro */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	
	public String nome;
	public String cpf;
	
	@Column(unique = true)
	public String email;
	
	/* Faz com que o json não inclua este campo, caso seja nulo */
	@JsonInclude(Include.NON_NULL)
	public String sexo;
	
	public String senha;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "usuario")
	@JsonIgnore
	public List<Categoria> categorias;
	
}
