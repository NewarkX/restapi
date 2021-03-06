	package spring.restapi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
public class Usuario implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Column(unique = true)
	private String login;
	private String senha;
	private String nome;
	@OneToMany(mappedBy = "usuario",orphanRemoval = true,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<Telefone> telefones;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "usuarios_role",
			uniqueConstraints = @UniqueConstraint ( // uniqueConstraints: Define a área de criação de constraints(dentro da anotação @Table), podendo ter uma ou mais;
				columnNames = {"usuario_id","role_id"}, //columnNames: Define quais campos formaram a regra de unico, no exemplo é o atributo CNPJ, mas poderia ser 2 atributos e a sintaxe seria : columnNames = {“CNPJ”, “TIPOPESSOA”}. Obs: é o atributo da classe, não o nome do campo no banco de dados
				name = "unique_role_user"), // name: O nome da UniqueConstraint no banco de dados.
				joinColumns = @JoinColumn(
					name = "usuario_id",
					referencedColumnName = "id",
					table = "usuario",
					unique = false,
					foreignKey = @ForeignKey (
						name = "usuario_fk",	
						value = ConstraintMode.CONSTRAINT)),
				inverseJoinColumns = @JoinColumn(
					name = "role_id",
					unique = false,
					referencedColumnName = "id" ,
					updatable = false,
					table = "role",
					foreignKey = @ForeignKey(
						name = "role_fk",
						value = ConstraintMode.CONSTRAINT ))	
	)
	private List<Role> roles = new ArrayList<Role>();
	
	private String token = "";
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	public List<Telefone> getTelefones() {
		return telefones;
	}
	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	/* sao os acessos do usuario */
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}
	@JsonIgnore
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.senha;
	}
	@JsonIgnore
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.login;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	
}
