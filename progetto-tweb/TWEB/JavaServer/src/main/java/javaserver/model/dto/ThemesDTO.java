package javaserver.model.dto;

public class ThemesDTO {

    private Long id; // Foreign key referring to Movies.id
    private String theme;


    public ThemesDTO(Long id, String theme) {
        this.id = id;
        this.theme = theme;
    }

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
