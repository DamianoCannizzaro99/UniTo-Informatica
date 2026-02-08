package javaserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javaserver.model.dto.MovieDTO;
import javaserver.service.MoviesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing Movies entities.
 */
@Tag(name = "Movies", description = "Movie management operations")
@RestController
@RequestMapping("/movies")
public class MoviesController {

    private final MoviesService moviesService;

    /**
     * Constructs a MoviesController with the specified MoviesService.
     *
     * @param moviesService the service for managing movies
     */
    @Autowired
    public MoviesController(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    /**
     * Finds movies by the specified ID.
     *
     * @param id the ID of the movie
     * @return a ResponseEntity containing a list of movies
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<MovieDTO> moviesById(@PathVariable Long id) {
        MovieDTO movie = moviesService.moviesById(id);
        if (movie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(movie);
    }

    /**
     * Finds the latest released movies.
     *
     * @return a ResponseEntity containing a list of latest released movies
     */
    @GetMapping("/latest-releases")
    public ResponseEntity<List<MovieDTO>> getLatestReleasedMovies() {
        LocalDate today = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 10);

        Page<MovieDTO> page = moviesService.findLatestReleasedMovies(today, pageable);

        if (page.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Finds the top-rated movies.
     *
     * @param page the page number for pagination
     * @param size the page size for pagination
     * @return a ResponseEntity containing a list of top-rated movies
     */
    @GetMapping("/TopRate")
    public ResponseEntity<List<MovieDTO>> getTopRatedMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        final Pageable pageable = PageRequest.of(page, size);
        Page<MovieDTO> moviePage = moviesService.findTopRatedMovies(pageable);
        if (moviePage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(moviePage.getContent());
    }

    /**
     * Searches for movies based on various criteria.
     *
     * @param title the title of the movie
     * @param genres the genres of the movie
     * @param duration the duration of the movieget
     * @param rating the rating of the movie
     * @param year the release year of the movie
     * @param page the page number for pagination
     * @param size the page size for pagination
     * @return a ResponseEntity containing a list of movies matching the criteria
     */
    @GetMapping("/search")
    public ResponseEntity<List<MovieDTO>> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genres,
            @RequestParam(required = false) Integer duration,
            @RequestParam(required = false) Double rating,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String studio,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MovieDTO> moviePage = moviesService.searchMovies(title, genres, duration, rating, year, studio, pageable);
        if (moviePage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(moviePage.getContent());
    }

    /**
     * Finds movies by the specified actor's name.
     *
     * @param actorName the name of the actor
     * @return a ResponseEntity containing a list of movies featuring the actor
     */
    @GetMapping("/name")
    public ResponseEntity<List<MovieDTO>> getAllMoviesbyNameActor(
            @RequestParam String actorName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MovieDTO> moviePage = moviesService.searchMovieFromNameActor(actorName, pageable);
        if (moviePage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(moviePage.getContent());
    }


}
