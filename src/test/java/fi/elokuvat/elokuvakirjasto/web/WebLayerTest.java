package fi.elokuvat.elokuvakirjasto.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import fi.elokuvat.elokuvakirjasto.domain.Actor;
import fi.elokuvat.elokuvakirjasto.domain.Director;
import fi.elokuvat.elokuvakirjasto.domain.Genre;
import fi.elokuvat.elokuvakirjasto.domain.Movie;
import fi.elokuvat.elokuvakirjasto.repository.ActorRepository;
import fi.elokuvat.elokuvakirjasto.repository.DirectorRepository;
import fi.elokuvat.elokuvakirjasto.repository.GenreRepository;
import fi.elokuvat.elokuvakirjasto.repository.MovieRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class WebLayerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private DirectorRepository directorRepository;
    @Autowired
    private ActorRepository actorRepository;

    private Movie saveTestMovie() {
        Genre genre = genreRepository.save(new Genre("Test Genre"));
        Director director = directorRepository.save(new Director("Test Director"));
        Actor actor = actorRepository.save(new Actor("Test Actor"));
        Movie movie = new Movie("Test Movie", 2020, "Test description", director, genre);
        movie.getActors().add(actor);
        return movieRepository.save(movie);
    }

    @Test
    public void movielistIsPublicAndHidesAdminLinks() throws Exception {
        saveTestMovie();
        mockMvc.perform(get("/movielist"))
                .andExpect(status().isOk())
                .andExpect(view().name("movielist"))
                .andExpect(model().attributeExists("movies"))
                .andExpect(content().string(containsString("Test Movie")))
                .andExpect(content().string(containsString("Test Actor")))
                .andExpect(content().string(not(containsString("Add movie"))));
    }

    @Test
    public void searchByDirectorFindsMovieIgnoringCase() throws Exception {
        saveTestMovie();
        mockMvc.perform(get("/movielist").param("q", "test director").param("by", "director"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test Movie")));
    }

    @Test
    public void searchWithNoResultsShowsMessage() throws Exception {
        mockMvc.perform(get("/movielist").param("q", "zzzzzz").param("by", "title"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("No movies found.")));
    }

    @Test
    public void addMovieRequiresLogin() throws Exception {
        mockMvc.perform(get("/addmovie"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", endsWith("/login")));
    }

    @Test
    public void deleteMovieRequiresLogin() throws Exception {
        mockMvc.perform(get("/deletemovie/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", endsWith("/login")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void adminSeesAdminLinksInMovielist() throws Exception {
        mockMvc.perform(get("/movielist"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Add movie")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void addMovieFormLoadsWithSelectionLists() throws Exception {
        mockMvc.perform(get("/addmovie"))
                .andExpect(status().isOk())
                .andExpect(view().name("addmovie"))
                .andExpect(model().attributeExists("movie", "genres", "directors", "actors"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void editMovieFormLoadsMovieData() throws Exception {
        Movie movie = saveTestMovie();
        mockMvc.perform(get("/editmovie/" + movie.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("addmovie"))
                .andExpect(model().attribute("movie", hasProperty("title", is("Test Movie"))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void saveValidMovieRedirectsAndStoresMovie() throws Exception {
        Director director = directorRepository.save(new Director("Form Director"));
        Genre genre = genreRepository.save(new Genre("Form Genre"));

        mockMvc.perform(post("/savemovie").with(csrf())
                .param("title", "New Movie")
                .param("releaseYear", "2021")
                .param("description", "Created in test")
                .param("director", director.getId().toString())
                .param("genre", genre.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movielist"));

        assertThat(movieRepository.findByTitleContainingIgnoreCase("New Movie")).hasSize(1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void saveInvalidMovieReturnsFormWithErrors() throws Exception {
        mockMvc.perform(post("/savemovie").with(csrf())
                .param("title", "")
                .param("releaseYear", "1500"))
                .andExpect(status().isOk())
                .andExpect(view().name("addmovie"))
                .andExpect(model().attributeHasFieldErrors("movie", "title", "releaseYear", "director", "genre"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteMovieRedirectsAndRemovesMovie() throws Exception {
        Movie movie = saveTestMovie();
        mockMvc.perform(get("/deletemovie/" + movie.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movielist"));

        assertThat(movieRepository.findById(movie.getId())).isEmpty();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void genreListLoadsAndValidGenreIsSaved() throws Exception {
        mockMvc.perform(get("/genrelist"))
                .andExpect(status().isOk())
                .andExpect(view().name("genrelist"));

        mockMvc.perform(post("/savegenre").with(csrf()).param("name", "Comedy Test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/genrelist"));

        assertThat(genreRepository.findAll()).extracting(Genre::getName).contains("Comedy Test");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void blankGenreNameShowsValidationError() throws Exception {
        mockMvc.perform(post("/savegenre").with(csrf()).param("name", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("genrelist"))
                .andExpect(model().attributeHasFieldErrors("genre", "name"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void directorAndActorListsLoad() throws Exception {
        mockMvc.perform(get("/directorlist")).andExpect(status().isOk()).andExpect(view().name("directorlist"));
        mockMvc.perform(get("/actorlist")).andExpect(status().isOk()).andExpect(view().name("actorlist"));
    }

    @Test
    public void restMoviesReturnsJsonList() throws Exception {
        saveTestMovie();
        mockMvc.perform(get("/rest/movies"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Test Movie")));
    }

    @Test
    public void restMovieByIdReturnsMovie() throws Exception {
        Movie movie = saveTestMovie();
        mockMvc.perform(get("/rest/movies/" + movie.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Test Movie")));
    }

    @Test
    public void restUnknownMovieReturns404() throws Exception {
        mockMvc.perform(get("/rest/movies/999999"))
                .andExpect(status().isNotFound());
    }
}