package javaserver.repository;

import javaserver.model.dto.StudiosDTO;
import javaserver.model.entity.Studios;
import javaserver.model.ids.StudiosId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudiosRepository extends JpaRepository<Studios, StudiosId> {


    /**
     * Retrieves all studios.
     *
     * @return a list of all StudiosDTO
     */
    @Query("SELECT new javaserver.model.dto.StudiosDTO(s.id, s.studio) FROM Studios s")
    List<StudiosDTO> findAllStudios();


    /**
     * Finds studios by movie name.
     *
     * @param movieId the name of the movie
     * @return a list of StudiosDTO for the specified movie name
     */
    @Query("SELECT new javaserver.model.dto.StudiosDTO(s.id, s.studio) " +
           "FROM Studios s JOIN s.movie m " +
           "WHERE m.id = :movieId")
    List<StudiosDTO> findStudiosByMovieId(@Param("movieId")Long movieId);


    /**
     * Counts the number of films produced by each studio.
     *
     * @param studioName the name of the studio
     * @return the number of films produced by the specified studio
     */
    @Query("SELECT COUNT(DISTINCT s.id) FROM Studios s WHERE LOWER(s.studio) = LOWER(:studioName)")
    Integer countFilmsByStudio(@Param("studioName") String studioName);

}
