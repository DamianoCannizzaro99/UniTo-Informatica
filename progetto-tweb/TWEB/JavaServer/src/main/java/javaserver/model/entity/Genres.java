package javaserver.model.entity;

import jakarta.persistence.*;
import javaserver.model.ids.GenreId;

/**
 * Entity class representing a genre relationship.
 * Each row represents a genre for a specific movie.
 */
@Entity
@Table(name= "genres")
@IdClass(GenreId.class)
public class Genres {
    
    @Id
    @Column(name = "id", nullable = false)
    private Long id; // movie_id

    @Id
    @Column(name= "genre", nullable= false, columnDefinition= "TEXT")
    private String genre;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    private Movie movie;

    /**
     * Default constructor.
     */
    public Genres() {}

    /**
     * Constructs a Genres with the specified movie ID and genre.
     *
     * @param id the movie ID
     * @param genre the name of the genre
     */
    public Genres(Long id, String genre) {
        this.id = id;
        this.genre = genre;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}