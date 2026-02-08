package javaserver.model.entity;

import jakarta.persistence.*;
import javaserver.model.ids.PostersId;

/**
 * Entity class for Posters.
 */
@Entity
@Table(name= "posters")
@IdClass(PostersId.class)
public class Posters {

    @Id
    @Column(name= "id", nullable= false)
    private Long id; // movie_id
    
    @Id
    @Column(name= "link", nullable= false, columnDefinition= "TEXT")
    private String link;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    private Movie movie;

    // Constructor
    public Posters() {}

    public Posters(Long id, String link) {
        this.id = id;
        this.link = link;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    
    public Movie getMovie() {
        return movie;
    }
    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
