package fi.elokuvat.elokuvakirjasto.web;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fi.elokuvat.elokuvakirjasto.domain.Director;
import fi.elokuvat.elokuvakirjasto.repository.DirectorRepository;

@Controller
public class DirectorController {

    private DirectorRepository directorRepository;

    public DirectorController(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    @GetMapping("/directorlist")
    public String directorList(Model model) {
        model.addAttribute("directors", directorRepository.findAll());
        model.addAttribute("director", new Director());
        return "directorlist";
    }

    @PostMapping("/savedirector")
    public String saveDirector(Director director) {
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
