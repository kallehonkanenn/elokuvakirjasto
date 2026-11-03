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
    public String movieList(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
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
    public String saveMovie(Movie movie) {
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
