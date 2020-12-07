package spring.restapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import spring.restapi.model.Telefone;

@Repository
public interface TelefoneRepository extends CrudRepository<Telefone, Long> {

}
