package javaserver.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import javaserver.model.dto.GenreDTO;
import javaserver.service.GenresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * REST controller for managing genres.
 */
@Tag(name = "Genres", description = "Movie genre management operations")
@RestController
@RequestMapping("/genres")
public class GenresController {

    private final GenresService genresService;

    /**
     * Constructs a GenreController with the specified GenreService.
     *
     * @param genresService the service for managing genres
     */
    @Autowired
    public GenresController(GenresService genresService) {
        this.genresService = genresService;
    }

    /**
     * Retrieves all genres for a specified movie ID.
     *
     * @param movieId the ID of the movie
     * @param page the page number for pagination
     * @param size the page size for pagination
     * @return a ResponseEntity containing the list of genres
     */
    @GetMapping("/by-movie/{movieId}")
    public ResponseEntity<List<GenreDTO>> getGenresByMovieId(
            @PathVariable Long movieId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GenreDTO> genrePage = genresService.findGenresByMovieId(movieId, pageable);
        if (genrePage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(genrePage.getContent());
    }

    /**
     * Retrieves all distinct genre names.
     *
     * @return a ResponseEntity containing the list of genre names
     */
    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllGenreNames() {
        List<String> genres = genresService.getAllGenreNames();
        if (genres == null || genres.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(genres);
    }

    /**
     * Retrieves all genre-movie relationships.
     *
     * @param page the page number for pagination
     * @param size the page size for pagination
     * @return a ResponseEntity containing all genre-movie relationships
     */
    @GetMapping("/all")
    public ResponseEntity<List<GenreDTO>> getAllGenreRelationships(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GenreDTO> genrePage = genresService.getAllGenreRelationships(pageable);
        if (genrePage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(genrePage.getContent());
    }

}
