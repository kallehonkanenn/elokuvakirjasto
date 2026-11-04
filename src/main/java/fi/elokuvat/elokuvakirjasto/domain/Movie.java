package fi.elokuvat.elokuvakirjasto.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title can be at most 200 characters")
    private String title;

    @Min(value = 1888, message = "Year must be 1888 or later")
    @Max(value = 2100, message = "Year must be 2100 or earlier")
    private int releaseYear;

    @Size(max = 1000, message = "Description can be at most 1000 characters")
    @Column(length = 1000)
    private String description;

    @NotNull(message = "Select a director")
    @ManyToOne
    @JoinColumn(name = "director_id")
    @JsonIgnoreProperties("movies")
    private Director director;

    @NotNull(message = "Select a genre")
    @ManyToOne
    @JoinColumn(name = "genre_id")
    @JsonIgnoreProperties("movies")
    private Genre genre;

    @ManyToMany
    @JoinTable(name = "movie_actor", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "actor_id"))
    @JsonIgnoreProperties("movies")
    private List<Actor> actors = new ArrayList<>();

    public Movie() {
    }

    public Movie(String title, int releaseYear, String description, Director director, Genre genre) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.description = description;
        this.director = director;
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    @Override
    public String toString() {
        return "Movie [id=" + id + ", title=" + title + ", releaseYear=" + releaseYear + ", description=" + description
                + ", director=" + director + ", genre=" + genre + "]";
    }

}
