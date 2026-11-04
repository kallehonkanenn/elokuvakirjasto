package fi.elokuvat.elokuvakirjasto.web;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fi.elokuvat.elokuvakirjasto.domain.Genre;
import fi.elokuvat.elokuvakirjasto.repository.GenreRepository;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class GenreController {

    private GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @GetMapping("/genrelist")
    public String genreList(Model model) {
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("genre", new Genre());
        return "genrelist";
    }

    @PostMapping("/savegenre")
    public String saveGenre(@Valid @ModelAttribute("genre") Genre genre, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("genres", genreRepository.findAll());
            return "genrelist";
        }
        genreRepository.save(genre);
        return "redirect:/genrelist";
    }

    @GetMapping("/deletegenre/{id}")
    public String deleteGenre(@PathVariable("id") Long id) {
        try {
            genreRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            return "redirect:/genrelist?error";
        }
        return "redirect:/genrelist";
    }

}
