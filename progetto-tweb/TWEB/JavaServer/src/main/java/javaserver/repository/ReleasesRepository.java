package javaserver.repository;

import javaserver.model.entity.Release;
import javaserver.model.ids.ReleaseId;
import javaserver.model.dto.ReleasesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReleasesRepository extends JpaRepository<Release, ReleaseId> {

    /**
     * Finds releases by movie name.
     *
     * @param movieName the name of the movie
     * @return a list of ReleasesDTO for the specified movie name
     */
    @Query("SELECT new javaserver.model.dto.ReleasesDTO(r.country, STR(r.date), r.type, r.rating, m.name) " +
           "FROM Release r JOIN r.movie m " +
           "WHERE LOWER(m.name) = LOWER(:movieName)")
    List<ReleasesDTO> findReleasesByMovieName(@Param("movieName") String movieName);

    /**
     * Counts movies by country.
     *
     * @param country the country of the releases
     * @return the number of movies released in the specified country
     */
    @Query("SELECT COUNT(DISTINCT m.id) FROM Release r JOIN r.movie m WHERE LOWER(r.country) LIKE LOWER(CONCAT('%', :country, '%'))")
    Long countMoviesByCountry(@Param("country") String country);


    // COMMENTED OUT
    /*
     * Finds releases by country.
     *
     * @param country the country of the releases
     *
     * @return a list of ReleasesDTO for the specified country
     */
    /*
    @Query("SELECT new javaserver.model.dto.ReleasesDTO(r.country, STR(r.date), r.type, r.rating, m.name) " +
           "FROM Release r JOIN r.movie m " +
           "WHERE LOWER(r.country) LIKE LOWER(CONCAT('%', :country, '%'))")
    List<ReleasesDTO> findReleasesByCountry(@Param("country") String country);
    */


    // COMMENTED OUT: NOT NEEDED
    /**
     * Retrieves all releases.
     *
     * @return a list of all ReleasesDTO
     */
    /*
    @Query("SELECT new javaserver.model.dto.ReleasesDTO(r.country, STR(r.date), r.type, r.rating, m.name) " +
           "FROM Release r JOIN r.movie m")
    List<ReleasesDTO> findAllReleases();
    */
}
