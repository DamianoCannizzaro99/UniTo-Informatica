package javaserver.model.dto;

public class ReleasesDTO {

    private String country;
    private String date;
    private String type;
    private String rating;
    private String movieName;

    public ReleasesDTO(String country, String date, String type, String rating, String movieName) {
        this.country = country;
        this.date = date;
        this.type = type;
        this.rating = rating;
        this.movieName = movieName;
    }

    // Getters and Setters
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }
}
