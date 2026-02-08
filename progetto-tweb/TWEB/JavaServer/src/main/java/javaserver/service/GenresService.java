package javaserver.service;

import javaserver.model.dto.GenreDTO;
import javaserver.repository.GenresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenresService {
    private final GenresRepository genresRepository;

    /**
     * Constructs a GenresService with the specified GenresRepostory.
     *
     * @param genresRepository the repository for managing genres
     */
    @Autowired
    public GenresService(GenresRepository genresRepository) {
        this.genresRepository = genresRepository;
    }


    /**
     * Retrieves all genres associated with a specific movie ID.
     *
     * @param movieId the ID of the movie
     * @param pageable pagination information
     * @return a page of GenreDTO objects associated with the specified movie ID
     */
    public Page<GenreDTO> findGenresByMovieId(Long movieId, Pageable pageable) {
        return genresRepository.findGenresByMovieId(movieId, pageable);
    }

    /**
     * Retrieves all distinct genre names.
     *
     * @return a list of distinct genre names
     */
    public List<String> getAllGenreNames() {
        return genresRepository.findAllGenreNames();
    }

    /**
     * Retrieves all genre-movie relationships.
     *
     * @param pageable pagination information
     * @return a page of GenreDTO objects representing all genre-movie relationships
     */
    public Page<GenreDTO> getAllGenreRelationships(Pageable pageable) {
        return genresRepository.findAllGenreRelationships(pageable);
    }
}
