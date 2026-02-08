package javaserver.repository;

import javaserver.model.entity.Movie;
import javaserver.model.dto.MovieDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MoviesRepository extends JpaRepository<Movie, Long> {

    @Query("""
    SELECT new javaserver.model.dto.MovieDTO(
        CAST(m.id AS INTEGER), m.name, m.date, m.tagline, m.description, 
        CAST(m.minute AS INTEGER), m.rating, p.link
    )
    FROM Movie m
    LEFT JOIN m.poster p
    WHERE m.id = :id
    """)
    MovieDTO findMovieById(@Param("id") Long id);

    @Query("""
    SELECT new javaserver.model.dto.MovieDTO(
        CAST(m.id AS INTEGER), m.name, m.date, m.tagline, m.description,
        CAST(m.minute AS INTEGER), m.rating, p.link
    )
    FROM Movie m
    LEFT JOIN m.poster p
    WHERE m.date IS NOT NULL AND m.date >= 1888 AND m.date < 2025
    ORDER BY m.date DESC
""")
    Page<MovieDTO> findLatestReleasedMovies(@Param("today") LocalDate today, Pageable pageable);

    @Query("SELECT m.id FROM Movie m WHERE LOWER(m.name) = LOWER(:movieName) ORDER BY CASE WHEN m.rating IS NOT NULL THEN 1 ELSE 0 END DESC, m.rating DESC NULLS LAST, m.id ASC LIMIT 1")
    Optional<Long> findMovieIdByExactName(@Param("movieName") String movieName);
    
    @Query("SELECT m.id FROM Movie m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :movieName, '%')) ORDER BY CASE WHEN m.rating IS NOT NULL THEN 1 ELSE 0 END DESC, m.rating DESC NULLS LAST, m.id ASC LIMIT 1")
    Optional<Long> findMovieIdByName(@Param("movieName") String movieName);
    
    @Query("""
    SELECT new javaserver.model.dto.MovieDTO(
        CAST(m.id AS INTEGER), m.name, m.date, m.tagline, m.description, 
        CAST(m.minute AS INTEGER), m.rating, p.link
    )
    FROM Movie m
    LEFT JOIN m.poster p
    WHERE LOWER(m.name) = LOWER(:movieName)
    ORDER BY CASE WHEN m.rating IS NOT NULL THEN 1 ELSE 0 END DESC, m.rating DESC NULLS LAST, m.id ASC
    """)
    List<MovieDTO> findAllMoviesByExactName(@Param("movieName") String movieName);

    @Query("""
    SELECT new javaserver.model.dto.MovieDTO(
        CAST(m.id AS INTEGER), m.name, m.date, m.tagline, m.description, 
        CAST(m.minute AS INTEGER), m.rating, p.link
    )
    FROM Movie m
    LEFT JOIN m.poster p
    WHERE m.rating IS NOT NULL
    ORDER BY m.rating DESC
    """)
    Page<MovieDTO> findTopRatedMovies(Pageable pageable);


    @Query("""
    SELECT DISTINCT new javaserver.model.dto.MovieDTO(
        CAST(m.id AS INTEGER), m.name, m.date, m.tagline, m.description, 
        CAST(m.minute AS INTEGER), m.rating, p.link
    )
    FROM Movie m
    LEFT JOIN m.poster p
    LEFT JOIN m.genre g
    LEFT JOIN m.studio s
    WHERE (:title IS NULL OR :title = '' OR LOWER(m.name) LIKE LOWER(CONCAT('%', :title, '%')))
    AND (m.date IS NOT NULL AND m.date >= 1888 AND m.date <= 2024)
    AND (:duration IS NULL OR m.minute >= :duration)
    AND (:rating IS NULL OR m.rating >= :rating)
    AND (:year IS NULL OR m.date = :year)
    AND (:genres IS NULL OR :genres = '' OR LOCATE(CONCAT(',', g.genre, ','), CONCAT(',', :genres, ',')) > 0)
    AND (:studio IS NULL OR :studio = '' OR LOWER(s.studio) LIKE LOWER(CONCAT('%', :studio, '%')))
    """)
    Page<MovieDTO> searchMovies(@Param("title") String title, 
                               @Param("genres") String genres,
                               @Param("duration") Integer duration,
                               @Param("rating") Double rating,
                               @Param("year") Integer year,
                               @Param("studio") String studio,
                               Pageable pageable);


    @Query("""
    SELECT DISTINCT new javaserver.model.dto.MovieDTO(
        CAST(m.id AS INTEGER), m.name, m.date, m.tagline, m.description, 
        CAST(m.minute AS INTEGER), m.rating, p.link
    )
    FROM Movie m
    LEFT JOIN m.poster p
    JOIN m.actors a
    WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :actorName, '%'))
    ORDER BY m.rating DESC
    """)
    Page<MovieDTO> searchMoviesByActorName(@Param("actorName") String actorName, Pageable pageable);
}
