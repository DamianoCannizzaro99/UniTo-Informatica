package javaserver.model.entity;

import jakarta.persistence.*;
import javaserver.model.ids.LanguagesId;

/**
 * Entity class representing a language.
 */
@Entity
@Table(name = "languages")
@IdClass(LanguagesId.class)
public class Languages {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "INTEGER")
    private Long id;

    @Id
    @Column(name = "type", nullable = false, columnDefinition = "TEXT")
    private String type;

    @Id
    @Column(name = "language", nullable = false, columnDefinition = "TEXT")
    private String language;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private Movie movie;

    /**
     * Default constructor.
     */
    public Languages() {}

    /**
     * Constructs a Languages with the specified ID, type, and language.
     *
     * @param id the ID of the language
     * @param type the type of the language
     * @param language the name of the language
     */
    public Languages(Long id, String type, String language) {
        this.id = id;
        this.type = type;
        this.language = language;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}