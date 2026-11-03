package fi.elokuvat.elokuvakirjasto.repository;

import org.springframework.data.repository.CrudRepository;

import fi.elokuvat.elokuvakirjasto.domain.Director;

public interface DirectorRepository extends CrudRepository<Director, Long> {

}
