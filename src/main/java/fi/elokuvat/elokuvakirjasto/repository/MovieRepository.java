package fi.elokuvat.elokuvakirjasto.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fi.elokuvat.elokuvakirjasto.domain.Movie;

public interface MovieRepository extends CrudRepository<Movie, Long> {

    List<Movie> findByTitleContainingIgnoreCase(String title);

    List<Movie> findByDirectorNameContainingIgnoreCase(String name);

    List<Movie> findByGenreNameContainingIgnoreCase(String name);

    List<Movie> findDistinctByActorsNameContainingIgnoreCase(String name);

}
