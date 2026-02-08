package javaserver.model.entity;

import jakarta.persistence.*;
import javaserver.model.ids.ActorId;

/**
 * Entity class representing an actor.
 */
@Entity
@Table(name = "actors")
@IdClass(ActorId.class)
public class Actor {
    @Id
    @Column(name = "id", nullable = false)
    private Long id; // This is the movie_id

    @Id
    @Column(name = "name", nullable = true, columnDefinition = "TEXT")
    private String name;

    @Id
    @Column(name = "role", nullable = true, columnDefinition = "TEXT")
    private String role;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    private Movie movie;

    /**
     * Default constructor.
     */
    public Actor() {
    }

    /**
     * Constructs an Actors with the specified id, name, and role.
     *
     * @param id   the id of the actor
     * @param name the name of the actor
     * @param role the role of the actor
     */
    public Actor(Long id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    /**
     * Returns the id of the actor.
     *
     * @return the id of the actor
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id of the actor.
     *
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name of the actor.
     *
     * @return the name of the actor
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the actor.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the role of the actor.
     *
     * @return the role of the actor
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the actor.
     *
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns the movie associated with the actor.
     *
     * @return the movie associated with the actor
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * Sets the movie associated with the actor.
     *
     * @param movie the movie to set
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

}
