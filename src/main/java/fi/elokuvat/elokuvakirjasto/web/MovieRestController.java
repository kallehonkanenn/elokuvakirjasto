package fi.elokuvat.elokuvakirjasto.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.elokuvat.elokuvakirjasto.domain.Movie;
import fi.elokuvat.elokuvakirjasto.repository.MovieRepository;

@RestController
@RequestMapping("/rest")
public class MovieRestController {

    private MovieRepository movieRepository;

    public MovieRestController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/movies")
    public Iterable<Movie> getMovies() {
        return movieRepository.findAll();
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable("id") Long id) {
        return movieRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
