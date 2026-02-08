package javaserver.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import javaserver.model.dto.ReleasesDTO;
import javaserver.service.ReleasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Releases", description = "Movie release management operations")
@RestController
@RequestMapping("/releases")
public class ReleasesController {


    private ReleasesService releasesService;

    /**
     * Constructor for ReleasesController.
     *
     * @param releasesService the service for Releases entities
     */
    @Autowired
    public ReleasesController(ReleasesService releasesService) {
        this.releasesService = releasesService;}


    //COMMENTED OUT: not needed
    /**
     * Retrieves all releases.
     *
     * @return a list of all releases
     */
    /*
    @GetMapping("/")
    public ResponseEntity<List<ReleasesDTO>> getAllReleases() {
         List<ReleasesDTO> releases = releasesService.getAllReleases();
        if (releases == null || releases.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if no releases found
        return ResponseEntity.ok(releases); // Return 200 OK with the list of releases
        }
    */

    // COMMENTED OUT: not needed
    /*
     * Retrieves releases by country.
     *
     * @param country the country of the releases
     * @return a list of releases for the specified country
     */
    /*
    @GetMapping("/releases-by-country/{country}")
    public ResponseEntity<List<ReleasesDTO>> getReleasesByCountry(@PathVariable String country) {
        List<ReleasesDTO> releases = releasesService.getReleasesByCountry(country);
        if (releases == null || releases.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if no releases found
        return ResponseEntity.ok(releases); // Return 200 OK with the list of releases
    }
    */

    /**
     * Counts movies by country.
     *
     * @param country the country of the releases
     * @return the number of movies released in the specified country
     */
    @GetMapping("/count-movies-by-country/{country}")
    public ResponseEntity<Long> countMoviesByCountry(@PathVariable String country) {
        Long releasesCount = releasesService.getCountMoviesByCountry(country);
        if (releasesCount == null || releasesCount == 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if no releases found
        return ResponseEntity.ok(releasesCount); // Return 200 OK with the count of releases
    }

    /**
     * Retrieves releases by movie name.
     *
     * @param movieName the name of the movie
     * @return a list of releases for the specified movie
     */
    @GetMapping("/releases-by-movie/{movieName}")
    public ResponseEntity<List<ReleasesDTO>> getReleasesByMovieName(@PathVariable String movieName) {
        List<ReleasesDTO> releases = releasesService.getReleasesByMovieName(movieName);
        if (releases == null || releases.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if no releases found
        return ResponseEntity.ok(releases); // Return 200 OK with the list of releases
    }

}
