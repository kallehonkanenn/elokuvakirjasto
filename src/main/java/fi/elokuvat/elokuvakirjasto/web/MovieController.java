package fi.elokuvat.elokuvakirjasto.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import fi.elokuvat.elokuvakirjasto.repository.MovieRepository;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MovieController {

    private MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
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

}
