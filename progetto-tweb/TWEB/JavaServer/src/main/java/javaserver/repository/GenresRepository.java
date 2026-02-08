package javaserver.repository;

import javaserver.model.entity.Genres;
import javaserver.model.ids.GenreId;
import javaserver.model.dto.GenreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenresRepository extends JpaRepository<Genres, GenreId> {
    
    // Note: Use /movies/search?genres=Action for searching movies by genre
    // This repository focuses on genre metadata
    
    /**
     * Finds all genres for a specific movie.
     *
     * @param movieId the ID of the movie
     * @param pageable pagination information
     * @return a page of GenreDTO containing genres for the specified movie
     */
    @Query("""
    SELECT new javaserver.model.dto.GenreDTO(
        g.id, m.name, g.genre
    )
    FROM Genres g
    JOIN g.movie m
    WHERE m.id = :movieId
    ORDER BY g.genre
    """)
    Page<GenreDTO> findGenresByMovieId(@Param("movieId") Long movieId, Pageable pageable);
    
    /**
     * Finds all distinct genre names.
     *
     * @return a list of genre names
     */
    @Query("SELECT DISTINCT g.genre FROM Genres g ORDER BY g.genre")
    List<String> findAllGenreNames();
    
    /**
     * Finds all genre-movie relationships.
     *
     * @param pageable pagination information
     * @return a page of all GenreDTO relationships
     */
    @Query("""
    SELECT new javaserver.model.dto.GenreDTO(
        g.id, m.name, g.genre
    )
    FROM Genres g
    JOIN g.movie m
    ORDER BY m.name, g.genre
    """)
    Page<GenreDTO> findAllGenreRelationships(Pageable pageable);
}
