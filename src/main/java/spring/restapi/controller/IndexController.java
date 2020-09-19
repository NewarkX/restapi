package spring.restapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



import spring.restapi.model.Usuario;
import spring.restapi.model.UsuarioDTO;
import spring.restapi.repository.UsuarioRepository;

@RestController
@RequestMapping(value="/usuario")
public class IndexController {
	
	@Autowired
	private UsuarioRepository ur;
	
	@GetMapping(value="/",produces = "application/json")
	@CacheEvict(value="cacheusuarios",allEntries = true)
	@CachePut("cacheusuarios")
	public ResponseEntity<List<Usuario>> listar() throws InterruptedException{
		List<Usuario> list = (List<Usuario>) ur.findAll();
		
		//Thread.sleep(6000); // segura o codigo por 6 segundos simulando um processo lento
		
		return new ResponseEntity<List<Usuario>>(list,HttpStatus.OK);
	}
	
	
	@GetMapping(value="/usuarioPorNome/{nome}",produces = "application/json")
	@CachePut("cacheusuarios")
	public ResponseEntity<List<Usuario>> usuarioPorNome(@PathVariable("nome")String nome) throws InterruptedException{
		List<Usuario> list = (List<Usuario>) ur.findUserByNome(nome);	
		return new ResponseEntity<List<Usuario>>(list,HttpStatus.OK);
	}
	
	
	/* para retornar so os dados do DTO
	@GetMapping(value = "/{id}",produces = "application/json")
	@CacheEvict(value="cacheuser",allEntries = true)
	@CachePut("cacheuser")
	public ResponseEntity<UsuarioDTO> listarid(@PathVariable (value="id") Long id) {
		Optional<Usuario> usuario = ur.findById(id);
		return new ResponseEntity<UsuarioDTO>(new UsuarioDTO(usuario.get()),HttpStatus.OK);
	}
	*/
	
	@GetMapping(value = "/{id}", produces = "application/json")
	@CachePut("cacheuser")
	public ResponseEntity init(@PathVariable (value = "id") Long id) {
		
		Optional usuario = ur.findById(id);
		
		return new ResponseEntity(usuario.get(), HttpStatus.OK);
	}
	
	@PostMapping(value="/",produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody  Usuario usuario){
		for(int pos = 0;pos < usuario.getTelefones().size();pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhacriptografada);
		Usuario usuariosalvo = ur.save(usuario);
		return new ResponseEntity<Usuario>(usuariosalvo,HttpStatus.OK);
	}
	
	@PutMapping(value="/",produces = "application/json")
	public ResponseEntity<Usuario> atualizar(@RequestBody  Usuario usuario){
		for(int pos = 0;pos < usuario.getTelefones().size();pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		
		Usuario userTemp = ur.findById(usuario.getId()).get();
		
		if(!userTemp.getSenha().equals(usuario.getSenha())) { //se as senhas dos usuarios forem diferentes criptografa
			String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhacriptografada);
		}
		
		Usuario usuariosalvo = ur.save(usuario);
		return new ResponseEntity<Usuario>(usuariosalvo,HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}",produces = "application/json")
	public String deletar(@PathVariable (value="id") Long id) {
		ur.deleteById(id);
		return "ok";
	}
	
	
	
	/*//retornando json
	@GetMapping(value = "/",produces = "application/json")
	public ResponseEntity init() {
		return new ResponseEntity<>("Olá SpringBoot",HttpStatus.OK);
	}
	
	//recebendo nome por parametro ex ?nome=bruno
	@GetMapping(value = "/",produces = "application/json")
	public ResponseEntity buscarnome(@RequestParam (value = "nome",defaultValue="Nome não informado") String nome) {
		return new ResponseEntity<>("Olá SpringBoot seu nome é : " + nome, HttpStatus.OK);
	}
	*/
	/*
	//retornando uma lista de  usuario em json
	@GetMapping(value = "/",produces = "application/json")
	public ResponseEntity init() {
		
		Usuario  u = new Usuario();
		u.setId(50L);
		u.setLogin("brunowmanha@gmail.com");
		u.setNome("bruno");
		u.setSenha("123");
		//retornando so um objeto
		//return ResponseEntity.ok(u);
		
		Usuario  u2 = new Usuario();
		u2.setId(150L);
		u2.setLogin("123brunowmanha@gmail.com");
		u2.setNome("bruno123");
		u2.setSenha("123345");
		
		
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios.add(u);
		usuarios.add(u2);
		
		return new ResponseEntity(usuarios,HttpStatus.OK); 
	}
	*/
}
