package javaserver.model.entity;

import jakarta.persistence.*;
import javaserver.model.ids.StudiosId;

@Entity
@Table(name = "studios")
@IdClass(StudiosId.class)
public class Studios {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "INTEGER")
    private Long id;

    @Id
    @Column(name = "studio", nullable = false, columnDefinition = "TEXT")
    private String studio;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private Movie movie;

    /**
     * Default constructor for the Studios class.
     */
    public Studios() {}

    /**
     * Constructor for the Studios class.
     *
     * @param id the unique identifier for the studio
     * @param studio the name of the studio
     */
    public Studios(Long id, String studio) {
        this.id = id;
        this.studio = studio;
    }


    /**
     * Gets the unique identifier for the studio.
     *
     * @return the unique identifier for the studio
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the studio.
     *
     * @param id the unique identifier for the studio
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the studio.
     *
     * @return the name of the studio
     */
    public String getStudio() {
        return studio;}

    /**
     * Sets the name of the studio.
     *
     * @param studio the name of the studio
     */
    public void setStudio(String studio) {
        this.studio = studio;}

    /**
     * Gets the movie associated with the studio.
     *
     * @return the movie associated with the studio
     */
    public Movie getMovie() {
        return movie;}

    /**
     * Sets the movie associated with the studio.
     *
     * @param movie the movie associated with the studio
     */
    public void setMovie(Movie movie) {
        this.movie = movie;}


}
