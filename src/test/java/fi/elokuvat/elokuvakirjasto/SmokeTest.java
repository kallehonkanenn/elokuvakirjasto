package fi.elokuvat.elokuvakirjasto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fi.elokuvat.elokuvakirjasto.web.ActorController;
import fi.elokuvat.elokuvakirjasto.web.DirectorController;
import fi.elokuvat.elokuvakirjasto.web.GenreController;
import fi.elokuvat.elokuvakirjasto.web.MovieController;
import fi.elokuvat.elokuvakirjasto.web.MovieRestController;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private MovieController movieController;
    @Autowired
    private MovieRestController movieRestController;
    @Autowired
    private GenreController genreController;
    @Autowired
    private DirectorController directorController;
    @Autowired
    private ActorController actorController;

    @Test
    public void contextLoads() {

    }

    @Test
    public void controllersAreLoaded() {
        assertThat(movieController).isNotNull();
        assertThat(movieRestController).isNotNull();
        assertThat(genreController).isNotNull();
        assertThat(directorController).isNotNull();
        assertThat(actorController).isNotNull();
    }
}
