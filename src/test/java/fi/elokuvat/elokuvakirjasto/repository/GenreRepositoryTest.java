package fi.elokuvat.elokuvakirjasto.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import fi.elokuvat.elokuvakirjasto.domain.Genre;

@DataJpaTest
public class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void createGenre() {
        Genre genre = genreRepository.save(new Genre("Test Genre"));

        assertThat(genre.getId()).isNotNull();
        assertThat(genreRepository.findById(genre.getId()).get().getName()).isEqualTo("Test Genre");
    }

    @Test
    public void findAllContainsSavedGenre() {
        genreRepository.save(new Genre("Searchable Genre"));

        assertThat(genreRepository.findAll()).extracting(Genre::getName).contains("Searchable Genre");
    }

    @Test
    public void deleteGenre() {
        long countBefore = genreRepository.count();
        Genre genre = genreRepository.save(new Genre("Delete Me"));

        genreRepository.deleteById(genre.getId());

        assertThat(genreRepository.findById(genre.getId())).isEmpty();
        assertThat(genreRepository.count()).isEqualTo(countBefore);
    }
}