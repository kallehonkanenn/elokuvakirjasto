package fi.elokuvat.elokuvakirjasto.repository;

import org.springframework.data.repository.CrudRepository;

import fi.elokuvat.elokuvakirjasto.domain.Actor;

public interface ActorRepository extends CrudRepository<Actor, Long> {

}
