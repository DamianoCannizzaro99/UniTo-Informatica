package javaserver.model.ids;

import java.io.Serializable;

public class ThemesId implements Serializable {

    private Long id; // Foreign key referring to Movies.id
    private String theme;

    /* * Default constructor for JPA
     */
    public ThemesId(Long id, String theme) {
        this.id = id;
        this.theme = theme;
    }

    // getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTheme() {
        return theme;
    }
    public void setTheme(String theme) {
        this.theme = theme;
    }

}
