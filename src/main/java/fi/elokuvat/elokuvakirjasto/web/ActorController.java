package fi.elokuvat.elokuvakirjasto.web;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fi.elokuvat.elokuvakirjasto.domain.Actor;
import fi.elokuvat.elokuvakirjasto.repository.ActorRepository;

@Controller
public class ActorController {

    private ActorRepository actorRepository;

    public ActorController(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @GetMapping("/actorlist")
    public String actorList(Model model) {
        model.addAttribute("actors", actorRepository.findAll());
        model.addAttribute("actor", new Actor());
        return "actorlist";
    }

    @PostMapping("/saveactor")
    public String saveActor(Actor actor) {
        actorRepository.save(actor);
        return "redirect:/actorlist";
    }

    @GetMapping("/deleteactor/{id}")
    public String deleteActor(@PathVariable("id") Long id) {
        try {
            actorRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            return "redirect:/actorlist?error";
        }
        return "redirect:/actorlist";
    }

}
