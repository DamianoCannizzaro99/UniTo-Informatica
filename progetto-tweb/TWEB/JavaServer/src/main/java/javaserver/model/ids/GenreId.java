package javaserver.model.ids;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key class for Genres entity.
 * Represents the combination of movie_id and genre name.
 */
public class GenreId implements Serializable {
    private Long id; // movie_id
    private String genre;

    public GenreId() {}

    public GenreId(Long id, String genre) {
        this.id = id;
        this.genre = genre;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreId genreId = (GenreId) o;
        return Objects.equals(id, genreId.id) && Objects.equals(genre, genreId.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, genre);
    }
}
