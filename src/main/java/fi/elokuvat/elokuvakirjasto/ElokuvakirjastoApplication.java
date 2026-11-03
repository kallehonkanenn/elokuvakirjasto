package fi.elokuvat.elokuvakirjasto;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fi.elokuvat.elokuvakirjasto.domain.Actor;
import fi.elokuvat.elokuvakirjasto.domain.Director;
import fi.elokuvat.elokuvakirjasto.domain.Genre;
import fi.elokuvat.elokuvakirjasto.domain.Movie;
import fi.elokuvat.elokuvakirjasto.repository.ActorRepository;
import fi.elokuvat.elokuvakirjasto.repository.DirectorRepository;
import fi.elokuvat.elokuvakirjasto.repository.GenreRepository;
import fi.elokuvat.elokuvakirjasto.repository.MovieRepository;

@SpringBootApplication
public class ElokuvakirjastoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElokuvakirjastoApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(MovieRepository movieRepository, GenreRepository genreRepository,
			DirectorRepository directorRepository, ActorRepository actorRepository) {
		return (args) -> {
			Genre scifi = genreRepository.save(new Genre("Sci-Fi"));
			Genre crime = genreRepository.save(new Genre("Crime"));
			Genre action = genreRepository.save(new Genre("Action"));

			Director nolan = directorRepository.save(new Director("Christopher Nolan"));
			Director tarantino = directorRepository.save(new Director("Quentin Tarantino"));

			Actor dicaprio = actorRepository.save(new Actor("Leonardo DiCaprio"));
			Actor hardy = actorRepository.save(new Actor("Tom Hardy"));
			Actor travolta = actorRepository.save(new Actor("John Travolta"));
			Actor jackson = actorRepository.save(new Actor("Samuel L. Jackson"));
			Actor bale = actorRepository.save(new Actor("Christian Bale"));
			Actor ledger = actorRepository.save(new Actor("Heath Ledger"));

			Movie inception = new Movie("Inception", 2010, "A thief enters dreams to steal secrets.", nolan, scifi);
			inception.getActors().add(dicaprio);
			inception.getActors().add(hardy);
			movieRepository.save(inception);

			Movie pulp = new Movie("Pulp Fiction", 1994, "Intertwined stories of crime in Los Angeles.", tarantino,
					crime);
			pulp.getActors().add(travolta);
			pulp.getActors().add(jackson);
			movieRepository.save(pulp);

			Movie batman = new Movie("The Dark Knight", 2008, "Batman faces the Joker in Gotham.", nolan, action);
			batman.getActors().add(bale);
			batman.getActors().add(ledger);
			movieRepository.save(batman);

		};
	}

}
