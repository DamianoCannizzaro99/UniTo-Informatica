package javaserver.model.dto;

public class PostersDTO {

    private Long movieId;
    private String posterUrl;



    /**
     * Parameterized constructor for PostersDTO.
     *
     * @param movieId the ID of the movie
     * @param posterUrl the URL of the poster
     */
    public PostersDTO( Long movieId, String posterUrl) {
        this.movieId = movieId;
        this.posterUrl = posterUrl;
    }

    // Getters and Setters

    public Long getMovieId() {
        return movieId;
    }
    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getPosterUrl() {
        return posterUrl;
    }
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }




}
