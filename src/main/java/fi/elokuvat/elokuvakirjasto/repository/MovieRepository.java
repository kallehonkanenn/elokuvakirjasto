package fi.elokuvat.elokuvakirjasto.repository;

import org.springframework.data.repository.CrudRepository;

import fi.elokuvat.elokuvakirjasto.domain.Movie;

public interface MovieRepository extends CrudRepository<Movie, Long> {

}
