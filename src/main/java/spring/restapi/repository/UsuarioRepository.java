package spring.restapi.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import spring.restapi.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long>{
	@Query("select u from  Usuario u where u.login = ?1")
	Usuario findUserByLogin(String login);
	
	@Query("select u from  Usuario u where u.nome like  %?1%")
	List<Usuario> findUserByNome(String nome);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true,value="update usuario set token = ?1 where login = ?2")
	void atualizaTokenUser(String token ,String login);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true,value = "insert into usuarios_role (usuario_id, role_id) values (?1, (select id from role where nome_role = 'ROLE_USER'));")
	void insereAcessoRolePadrao(Long idUser);
}
