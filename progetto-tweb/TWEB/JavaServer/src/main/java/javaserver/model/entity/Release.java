package javaserver.model.entity;

import jakarta.persistence.*;
import javaserver.model.ids.ReleaseId;

/**
 * Entity class for Releases.
 */
@Entity
@Table(name= "releases")
@IdClass(ReleaseId.class)
public class Release {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Id
    @Column(name = "country", nullable = false, columnDefinition = "TEXT")
    private String country;
    @Id
    @Column(name = "date", nullable = false, columnDefinition = "TEXT")
    private String date;
    @Id
    @Column(name = "type", nullable = false, columnDefinition = "TEXT")
    private String type;
    @Column(name = "rating", nullable = true, columnDefinition = "TEXT")
    private String rating;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    private Movie movie; // Many-to-one relationship with Movies

    /**
     * Default constructor for Releases.
     */
    public Release() {
    }

    /**
     * Constructor for Releases with parameters.
     *
     * @param id the ID of the release
     * @param country the country of the release
     * @param date the date of the release
     * @param type the type of the release
     * @param rating the rating of the release
     */
    public Release(Long id, String country, String date, String type, String rating) {
        this.id = id;
        this.country = country;
        this.date = date;
        this.type = type;
        this.rating = rating;
    }

    // Getters and Setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getCountry() {return country;}
    public void setCountry(String country) {this.country = country;}

    public String getReleaseDate() {return date;}
    public void setReleaseDate(String date) {this.date = date;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public String getRating() {return rating;}
    public void setRating(String rating) {this.rating = rating;}

    public Movie getMovie() {return movie;}
    public void setMovie(Movie movie) {this.movie = movie;}
}
