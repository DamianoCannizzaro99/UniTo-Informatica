package javaserver.model.dto;

public class MovieDTO {

    private Long id;
    private String Title;
    private Integer relaseYear;
    private String tagline;
    private String description;
    private Integer runtime;
    private Double rating;
    private String posterLink;

    public MovieDTO(Integer id, String title, Integer relaseYear, String tagline, String description, Integer runtime, Double rating, String posterLink) {
        this.id = Long.valueOf(id);
        this.Title = title;
        this.relaseYear = relaseYear;
        this.tagline = tagline;
        this.description = description;
        this.runtime = runtime;
        this.rating = rating;
        this.posterLink = posterLink;
    }

    // Getters and Setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getTitle() {return Title;}
    public void setTitle(String title) {Title = title;}

    public Integer getRelaseYear() {return relaseYear;}
    public void setRelaseYear(Integer relaseYear) {this.relaseYear = relaseYear;}

    public String getTagline() {return tagline;}
    public void setTagline(String tagline) {this.tagline = tagline;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public Integer getRuntime() {return runtime;}
    public void setRuntime(Integer runtime) {this.runtime = runtime;}

    public Double getRating() {return rating;}
    public void setRating(Double rating) {this.rating = rating;}

    public String getPosterLink() {return posterLink;}
    public void setPosterLink(String posterLink) {this.posterLink = posterLink;}
}
