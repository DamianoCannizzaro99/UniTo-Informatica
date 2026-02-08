package javaserver.controller;

import javaserver.model.dto.StudiosDTO;
import javaserver.service.StudiosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/studios")
public class StudiosController {

    private final StudiosService studiosService;

    /**
     * Constructor for StudiosController.
     *
     * @param studiosService the service for Studios entities
     */
    @Autowired
    public StudiosController(StudiosService studiosService) {
        this.studiosService = studiosService;
    }

    @GetMapping("/")
    public ResponseEntity<List<StudiosDTO>> getAllStudios() {
        List<StudiosDTO> studios = studiosService.getAllStudios();
        if (studios == null || studios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studios);
    }

    /**
     * Retrieves studios by movie name.
     *
     * @param movieId the name of the movie
     * @return a list of studios for the specified movie name
     */
    @GetMapping("/studios-by-movie/{movieId}")
    public ResponseEntity<List<StudiosDTO>> getStudioByMovie(@PathVariable Long movieId) {
        List<StudiosDTO> studios = studiosService.getStudiosByMovieId(movieId);
        if (studios == null || studios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studios);
    }

    /**
     * Retrieves the number of films produced by each studio.
     *
     * @param studioName the name of the studio
     * @return a list of studios with the number of films produced
     */
    @GetMapping("/num-films-by-studio/{studioName}")
    public ResponseEntity<Integer> getNumFilmsByStudio(@PathVariable String studioName) {
        Integer numFilms = studiosService.getNumFilmsByStudio(studioName);
        if (numFilms == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(numFilms);
    }

}
