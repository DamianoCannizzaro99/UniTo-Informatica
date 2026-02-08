package javaserver.service;

import javaserver.model.dto.ReleasesDTO;
import javaserver.repository.ReleasesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReleasesService {

    private final ReleasesRepository releasesRepository;

    /**
     * Constructor for ReleasesService.
     *
     * @param releasesRepository the repository for Releases entities
     */
    @Autowired
    public ReleasesService(ReleasesRepository releasesRepository) {
        this.releasesRepository = releasesRepository;
    }


    /**
     * Retrieves releases by movie name.
     *
     * @param movieName the name of the movie
     * @return a list of releases for the specified movie name
     */
    public List<ReleasesDTO> getReleasesByMovieName(String movieName) {
        return releasesRepository.findReleasesByMovieName(movieName);
    }

    /**
     * Counts movies by country.
     *
     * @param country the country of the releases
     * @return the number of movies released in the specified country
     */
    public Long getCountMoviesByCountry(String country) {
        return releasesRepository.countMoviesByCountry(country);
    }


    // COMMENTED OUT: not needed
    /*
     * Retrieves releases by country.
     *
     * @param country the country of the releases
     * @return a list of releases for the specified country
     */
    /*
    public List<ReleasesDTO> getReleasesByCountry(String country) {
        return releasesRepository.findReleasesByCountry(country);
    }
    */


    // COMMENTED OUT: Not needed
    /**
     * Retrieves all releases.
     *
     * @return a list of all releases
     */
    /*
    public List<ReleasesDTO> getAllReleases() {
        return releasesRepository.findAllReleases();
    }
    */

}
