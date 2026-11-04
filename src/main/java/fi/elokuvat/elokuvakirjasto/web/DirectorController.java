package fi.elokuvat.elokuvakirjasto.web;

import fi.elokuvat.elokuvakirjasto.repository.GenreRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fi.elokuvat.elokuvakirjasto.domain.Director;
import fi.elokuvat.elokuvakirjasto.repository.DirectorRepository;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class DirectorController {

    private final GenreRepository genreRepository;
    private DirectorRepository directorRepository;

    public DirectorController(DirectorRepository directorRepository, GenreRepository genreRepository) {
        this.directorRepository = directorRepository;
        this.genreRepository = genreRepository;
    }

    @GetMapping("/directorlist")
    public String directorList(Model model) {
        model.addAttribute("directors", directorRepository.findAll());
        model.addAttribute("director", new Director());
        return "directorlist";
    }

    @PostMapping("/savedirector")
    public String saveDirector(@Valid @ModelAttribute("director") Director director, BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("genres", genreRepository.findAll());
            return "genrelist";
        }
        directorRepository.save(director);
        return "redirect:/directorlist";
    }

    @GetMapping("/deletedirector/{id}")
    public String deleteDirector(@PathVariable("id") Long id) {
        try {
            directorRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            return "redirect:/directorlist?error";
        }
        return "redirect:/directorlist";
    }

}
