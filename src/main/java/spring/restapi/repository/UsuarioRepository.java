package spring.restapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import spring.restapi.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long>{
	@Query("select u from  Usuario u where u.login = ?1")
	Usuario findUserByLogin(String login);
	
	@Query("select u from  Usuario u where u.nome like  %?1%")
	List<Usuario> findUserByNome(String nome);
}
