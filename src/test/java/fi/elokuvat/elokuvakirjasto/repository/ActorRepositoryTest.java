package fi.elokuvat.elokuvakirjasto.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import fi.elokuvat.elokuvakirjasto.domain.Actor;

@DataJpaTest
public class ActorRepositoryTest {

    @Autowired
    private ActorRepository actorRepository;

    @Test
    public void createActor() {
        Actor actor = actorRepository.save(new Actor("Test Actor"));

        assertThat(actor.getId()).isNotNull();
        assertThat(actorRepository.findById(actor.getId()).get().getName()).isEqualTo("Test Actor");
    }

    @Test
    public void findAllContainsSavedActor() {
        actorRepository.save(new Actor("Searchable Actor"));

        assertThat(actorRepository.findAll()).extracting(Actor::getName).contains("Searchable Actor");
    }

    @Test
    public void deleteActor() {
        long countBefore = actorRepository.count();
        Actor actor = actorRepository.save(new Actor("Delete Me"));

        actorRepository.deleteById(actor.getId());

        assertThat(actorRepository.findById(actor.getId())).isEmpty();
        assertThat(actorRepository.count()).isEqualTo(countBefore);
    }
}