package javaserver.model.entity;

import jakarta.persistence.*;
import javaserver.model.ids.ThemesId;

@Entity
@Table(name = "themes")
@IdClass(ThemesId.class)
public class Themes {
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "INTEGER")
    private Long id;

    @Id
    @Column(name = "theme", nullable = false, columnDefinition = "TEXT")
    private String theme;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private Movie movie;

    /**
     * Constructs a Themes entity with the specified id and theme.
     *
     * @param id the id of the theme
     * @param theme the theme name
     */
    public Themes(Long id, String theme) {
        this.id = id;
        this.theme = theme;
    }

    /**
     * Gets the id of the theme.
     *
     * @return the id of the theme
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id of the theme.
     *
     * @param id the id of the theme
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the theme name.
     *
     * @return the theme name
     */
    public String getTheme() {
        return theme;
    }

    /**
     * Sets the theme name.
     *
     * @param theme the theme name
     */
    public void setTheme(String theme) {
        this.theme = theme;
    }
}