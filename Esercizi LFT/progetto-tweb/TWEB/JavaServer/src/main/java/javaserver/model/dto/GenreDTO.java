package javaserver.model.dto;

/**
 * DTO for genre-related API responses.
 * Used to transfer genre and movie information.
 */
public class GenreDTO {

    private Long movieId;
    private String movieTitle;
    private String genreName;


    /**
     * Constructor for repository queries.
     *
     * @param movieId the movie ID
     * @param movieTitle the movie title
     * @param genreName the genre name
     */
    public GenreDTO(Long movieId, String movieTitle, String genreName) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.genreName = genreName;
    }

    // Getters and setters
    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
