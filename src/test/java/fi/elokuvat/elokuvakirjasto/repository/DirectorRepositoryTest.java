package fi.elokuvat.elokuvakirjasto.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import fi.elokuvat.elokuvakirjasto.domain.Director;

@DataJpaTest
public class DirectorRepositoryTest {

    @Autowired
    private DirectorRepository directorRepository;

    @Test
    public void createDirector() {
        Director director = directorRepository.save(new Director("Test Director"));

        assertThat(director.getId()).isNotNull();
        assertThat(directorRepository.findById(director.getId()).get().getName()).isEqualTo("Test Director");
    }

    @Test
    public void findAllContainsSavedDirector() {
        directorRepository.save(new Director("Searchable Director"));

        assertThat(directorRepository.findAll()).extracting(Director::getName).contains("Searchable Director");
    }

    @Test
    public void deleteDirector() {
        long countBefore = directorRepository.count();
        Director director = directorRepository.save(new Director("Delete Me"));

        directorRepository.deleteById(director.getId());

        assertThat(directorRepository.findById(director.getId())).isEmpty();
        assertThat(directorRepository.count()).isEqualTo(countBefore);
    }
}