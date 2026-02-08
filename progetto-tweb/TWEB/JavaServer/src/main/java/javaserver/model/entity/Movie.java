package javaserver.model.entity;

import javaserver.model.entity.Countries;
import javaserver.model.entity.Crew;
import javaserver.model.entity.Languages;
import javaserver.model.entity.Studios;
import javaserver.model.entity.Themes;
import jakarta.persistence.*;
import java.util.List;

/**
 * Entity class representing a movie.
 */
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "date", nullable = true, columnDefinition = "INTEGER")
    private Integer date;

    @Column(name = "tagline", nullable = true, columnDefinition = "TEXT")
    private String tagline;

    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "minute", nullable = true, columnDefinition = "INTEGER")
    private Integer minute;

    @Column(name = "rating", nullable = true, columnDefinition = "REAL")
    private Double rating;

    @OneToMany(mappedBy = "movie")
    private List<Languages> languages;

    @OneToOne(mappedBy = "movie")
    private Posters poster;

    @OneToMany(mappedBy = "movie")
    private List<Actor> actors;

    @OneToMany(mappedBy = "movie")
    private List<Countries> countries;

    @OneToMany(mappedBy = "movie")
    private List<Crew> crews;

    @OneToMany(mappedBy = "movie")
    private List<Genres> genre;

    @OneToMany(mappedBy = "movie")
    private List<Studios> studio;

    @OneToMany(mappedBy = "movie")
    private List<Release> release;

    @OneToMany(mappedBy = "movie")
    private List<Themes> theme;

    /**
     * Default constructor.
     */
    public Movie() {}

    /**
     * Constructs a Movies entity with the specified parameters.
     *
     * @param id the ID of the movie
     * @param name the name of the movie
     * @param date the release date of the movie
     * @param tagline the tagline of the movie
     * @param description the description of the movie
     * @param minute the duration of the movie in minutes
     * @param rating the rating of the movie
     */
    public Movie(Long id, String name, Integer date, String tagline, String description, Integer minute, Double rating) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.tagline = tagline;
        this.description = description;
        this.minute = minute;
        this.rating = rating;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getDate() { return date; }
    public void setDate(Integer date) { this.date = date; }

    public String getTagline() { return tagline; }
    public void setTagline(String tagline) { this.tagline = tagline; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getMinute() { return minute; }
    public void setMinute(Integer minute) { this.minute = minute; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public List<Languages> getLanguages() { return languages; }
    public void setLanguages(List<Languages> languages) { this.languages = languages; }

    public Posters getPoster() { return poster; }
    public void setPoster(Posters poster) { this.poster = poster; }

    public List<Actor> getActors() { return actors; }
    public void setActors(List<Actor> actors) { this.actors = actors; }

    public List<Countries> getCountries() { return countries; }
    public void setCountries(List<Countries> countries) { this.countries = countries; }

    public List<Crew> getCrews() { return crews; }
    public void setCrews(List<Crew> crews) { this.crews = crews; }

    public List<Genres> getGenre() { return genre; }
    public void setGenre(List<Genres> genre) { this.genre = genre; }

    public List<Studios> getStudio() { return studio; }
    public void setStudio(List<Studios> studio) { this.studio = studio; }

    public List<Release> getRelease() { return release; }
    public void setRelease(List<Release> release) { this.release = release; }

    public List<Themes> getTheme() { return theme; }
    public void setTheme(List<Themes> theme) { this.theme = theme; }
}