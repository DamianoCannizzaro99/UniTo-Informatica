package javaserver.model.dto;

public class ActorDTO {
    private Long id;
    private String name;
    private String role;
    private String movieTitle;


    public ActorDTO(Long id, String name, String role, String movieTitle) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.movieTitle = movieTitle;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
}