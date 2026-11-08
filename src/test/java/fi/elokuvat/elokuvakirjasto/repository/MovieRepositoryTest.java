package fi.elokuvat.elokuvakirjasto.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import fi.elokuvat.elokuvakirjasto.domain.Actor;
import fi.elokuvat.elokuvakirjasto.domain.Director;
import fi.elokuvat.elokuvakirjasto.domain.Genre;
import fi.elokuvat.elokuvakirjasto.domain.Movie;

@DataJpaTest
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private DirectorRepository directorRepository;
    @Autowired
    private ActorRepository actorRepository;

    private Genre genre;
    private Director director;

    @BeforeEach
    public void setUp() {
        genre = genreRepository.save(new Genre("Test Genre"));
        director = directorRepository.save(new Director("Test Director"));
    }

    @Test
    public void createMovie() {
        Movie movie = movieRepository.save(new Movie("Test Movie", 2020, "Description", director, genre));

        assertThat(movie.getId()).isNotNull();
        assertThat(movieRepository.findById(movie.getId())).isPresent();
    }

    @Test
    public void findByTitleContainingIgnoreCase() {
        movieRepository.save(new Movie("Test Movie One", 2020, "Description", director, genre));
        movieRepository.save(new Movie("Another Film", 2021, "Description", director, genre));

        List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase("TEST MOVIE");

        assertThat(movies).hasSize(1);
        assertThat(movies.get(0).getTitle()).isEqualTo("Test Movie One");
    }

    @Test
    public void findByDirectorNameContainingIgnoreCase() {
        Director other = directorRepository.save(new Director("Other Person"));
        movieRepository.save(new Movie("Film A", 2020, "Description", director, genre));
        movieRepository.save(new Movie("Film B", 2021, "Description", director, genre));
        movieRepository.save(new Movie("Film C", 2022, "Description", other, genre));

        assertThat(movieRepository.findByDirectorNameContainingIgnoreCase("test director")).hasSize(2);
    }

    @Test
    public void findByGenreNameContainingIgnoreCase() {
        Genre otherGenre = genreRepository.save(new Genre("Other Genre"));
        movieRepository.save(new Movie("Film A", 2020, "Description", director, genre));
        movieRepository.save(new Movie("Film B", 2021, "Description", director, otherGenre));

        List<Movie> movies = movieRepository.findByGenreNameContainingIgnoreCase("test genre");

        assertThat(movies).hasSize(1);
        assertThat(movies.get(0).getTitle()).isEqualTo("Film A");
    }

    @Test
    public void findDistinctByActorsNameReturnsMovieOnlyOnce() {
        Actor a = actorRepository.save(new Actor("Test Actor A"));
        Actor b = actorRepository.save(new Actor("Test Actor B"));
        Movie movie = new Movie("Two Actors Film", 2020, "Description", director, genre);
        movie.getActors().add(a);
        movie.getActors().add(b);
        movieRepository.save(movie);

        assertThat(movieRepository.findDistinctByActorsNameContainingIgnoreCase("test actor")).hasSize(1);
    }

    @Test
    public void searchReturnsEmptyListWhenNothingMatches() {
        assertThat(movieRepository.findByTitleContainingIgnoreCase("no such movie xyz")).isEmpty();
    }

    @Test
    public void deleteMovie() {
        long countBefore = movieRepository.count();
        Movie movie = movieRepository.save(new Movie("Delete Me", 2020, "Description", director, genre));
        assertThat(movieRepository.count()).isEqualTo(countBefore + 1);

        movieRepository.deleteById(movie.getId());

        assertThat(movieRepository.findById(movie.getId())).isEmpty();
        assertThat(movieRepository.count()).isEqualTo(countBefore);
    }
}