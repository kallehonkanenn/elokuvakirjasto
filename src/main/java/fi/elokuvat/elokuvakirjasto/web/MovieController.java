package fi.elokuvat.elokuvakirjasto.web;

import fi.elokuvat.elokuvakirjasto.repository.ActorRepository;
import fi.elokuvat.elokuvakirjasto.repository.DirectorRepository;
import fi.elokuvat.elokuvakirjasto.repository.GenreRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import fi.elokuvat.elokuvakirjasto.domain.Movie;
import fi.elokuvat.elokuvakirjasto.repository.MovieRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class MovieController {

    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final GenreRepository genreRepository;
    private MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository, GenreRepository genreRepository,
            DirectorRepository directorRepository, ActorRepository actorRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.directorRepository = directorRepository;
        this.actorRepository = actorRepository;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/movielist";
    }

    @GetMapping("/movielist")
    public String movieList(@RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "by", defaultValue = "title") String by,
            Model model, Principal principal) {

        Iterable<Movie> movies;
        if (q == null || q.isBlank()) {
            movies = movieRepository.findAll();
        } else {
            movies = switch (by) {
                case "director" -> movieRepository.findByDirectorNameContainingIgnoreCase(q);
                case "genre" -> movieRepository.findByGenreNameContainingIgnoreCase(q);
                case "actor" -> movieRepository.findDistinctByActorsNameContainingIgnoreCase(q);
                default -> movieRepository.findByTitleContainingIgnoreCase(q);
            };
        }

        model.addAttribute("movies", movies);
        model.addAttribute("q", q);
        model.addAttribute("by", by);
        model.addAttribute("loggedIn", principal != null);
        return "movielist";
    }

    @GetMapping("/addmovie")
    public String addMovie(Model model) {
        model.addAttribute("movie", new Movie());
        addFormLists(model);
        return "addmovie";
    }

    @GetMapping("/editmovie/{id}")
    public String editMovie(@PathVariable("id") Long id, Model model) {
        model.addAttribute("movie", movieRepository.findById(id).orElse(new Movie()));
        addFormLists(model);
        return "addmovie";
    }

    @PostMapping("/savemovie")
    public String saveMovie(@Valid Movie movie, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            addFormLists(model);
            return "addmovie";
        }
        movieRepository.save(movie);
        return "redirect:/movielist";
    }

    @GetMapping("/deletemovie/{id}")
    public String deleteMovie(@PathVariable("id") Long id) {
        movieRepository.deleteById(id);
        return "redirect:/movielist";
    }

    private void addFormLists(Model model) {
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("directors", directorRepository.findAll());
        model.addAttribute("actors", actorRepository.findAll());
    }

}
