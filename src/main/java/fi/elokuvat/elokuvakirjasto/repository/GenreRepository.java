package fi.elokuvat.elokuvakirjasto.repository;

import org.springframework.data.repository.CrudRepository;

import fi.elokuvat.elokuvakirjasto.domain.Genre;

public interface GenreRepository extends CrudRepository<Genre, Long> {

}
