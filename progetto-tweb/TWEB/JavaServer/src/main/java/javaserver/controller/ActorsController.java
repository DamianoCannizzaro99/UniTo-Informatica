package javaserver.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import javaserver.model.dto.ActorDTO;
import javaserver.model.dto.MovieDTO;
import javaserver.service.ActorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing actors.
 */
@Tag(name = "Actors", description = "Actor management operations")
@RestController
@RequestMapping("/actors")
public class ActorsController {
    private final ActorsService actorsService;

    /**
     * Constructs an ActorsController with the specified ActorsService.
     *
     * @param actorsService the service for managing actors
     */
    @Autowired
    public ActorsController(ActorsService actorsService) {
        this.actorsService = actorsService;
    }

    /**
     * Retrieves movies by the specified actor name.
     *
     * 
     * @param actorName the name of the actor
     * @param page the page number for pagination
     * @param size the page size for pagination
     * @return a ResponseEntity containing the list of movies featuring the specified actor
     */
    @GetMapping("/by-actor-name/{actorName}")
    public ResponseEntity<List<MovieDTO>> getMoviesByActorName(
            @PathVariable String actorName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MovieDTO> moviePage = actorsService.findMoviesByActorName(actorName, pageable);

        if (moviePage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(moviePage.getContent());
    }

    /**
     * Retrieves actors by the specified movie name.
     *
     * @param movieName the name of the movie
     * @param page the page number for pagination
     * @param size the page size for pagination
     * @return a ResponseEntity containing the list of actors in the specified movie
     */
    @GetMapping("/by-movie/{movieName}")
    public ResponseEntity<List<ActorDTO>> getActorsByMovieName(
            @PathVariable String movieName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ActorDTO> actorPage = actorsService.getActorsByMovieName(movieName, pageable);
        
        if (actorPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        return ResponseEntity.ok(actorPage.getContent());
    }
    
    /**
     * Shows all movies with the exact same name to help users choose the right one.
     *
     * @param movieName the name of the movie
     * @return a ResponseEntity containing all movies with that exact name
     */
    @GetMapping("/movies-with-name/{movieName}")
    public ResponseEntity<List<MovieDTO>> getMoviesWithSameName(
            @PathVariable String movieName
    ) {
        List<MovieDTO> movies = actorsService.findAllMoviesWithName(movieName);
        
        if (movies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        return ResponseEntity.ok(movies);
    }
    
    /**
     * Retrieves actors by movie ID (when user knows the specific movie they want).
     *
     * @param movieId the ID of the specific movie
     * @param page the page number for pagination
     * @param size the page size for pagination
     * @return a ResponseEntity containing the list of actors in the specified movie
     */
    @GetMapping("/by-movie-id/{movieId}")
    public ResponseEntity<List<ActorDTO>> getActorsByMovieId(
            @PathVariable Long movieId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ActorDTO> actorPage = actorsService.getActorsByMovieId(movieId, pageable);
        
        if (actorPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        return ResponseEntity.ok(actorPage.getContent());
    }
}
