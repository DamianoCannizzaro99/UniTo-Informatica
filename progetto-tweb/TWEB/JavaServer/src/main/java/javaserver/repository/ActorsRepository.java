package javaserver.repository;

import javaserver.model.entity.Actor;
import javaserver.model.ids.ActorId;
import javaserver.model.dto.ActorDTO;
import javaserver.model.dto.MovieDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing actors.
 */
@Repository
public interface ActorsRepository extends JpaRepository<Actor,ActorId> {

    /**
     * Finds movies by the specified actor name.
     *
     * @param actorName the name of the actor
     * @return a page of movies featuring the specified actor
     */
    @Query("""
    SELECT DISTINCT new javaserver.model.dto.MovieDTO(
        CAST(m.id AS INTEGER), m.name, m.date, m.tagline, m.description, 
        CAST(m.minute AS INTEGER), m.rating, p.link
    )
    FROM Actor a
    JOIN a.movie m
    LEFT JOIN m.poster p
    WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :actorName, '%'))
    ORDER BY m.rating DESC
    """)
    Page<MovieDTO> findMoviesByActorName(@Param("actorName") String actorName, Pageable pageable);

    /**
     * Finds actors by the specified movie ID.
     *
     * @param movieId the ID of the movie
     * @return a page of actors associated with the specified movie
     */
    @Query("""
    SELECT new javaserver.model.dto.ActorDTO(
        a.id, a.name, a.role, m.name
    )
    FROM Actor a
    JOIN a.movie m
    WHERE a.id = :movieId
    ORDER BY a.name
    """)
    Page<ActorDTO> findActorsByMovieId(@Param("movieId") Long movieId, Pageable pageable);
}