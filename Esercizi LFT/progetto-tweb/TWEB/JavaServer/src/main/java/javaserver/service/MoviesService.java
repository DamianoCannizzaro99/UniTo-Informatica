package javaserver.service;

import javaserver.model.dto.MovieDTO;
import javaserver.repository.MoviesRepository;
import javaserver.util.MovieSortingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing movies.
 */
@Service
public class MoviesService {
    private final MoviesRepository moviesRepository;

    /**
     * Constructs a MoviesService with the specified MoviesRepository.
     *
     * @param moviesRepository the repository for managing movies
     */
    @Autowired
    public MoviesService(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    /**
     * Searches for movies based on various criteria.
     *
     * @param title the title of the movie (optional)
     * @param genres a list of genres to filter by (optional)
     * @param duration the duration of the movie in minutes (optional)
     * @param rating the minimum rating of the movie (optional)
     * @param year the release year of the movie (optional)
     * @param pageable pagination information
     * @return a page of MovieDTO objects matching the search criteria
     */
    public Page<MovieDTO> searchMovies(String title, String genres, Integer duration, Double rating, Integer year, String studio, Pageable pageable) {
        // Convert empty strings to null to ensure proper query handling
        String processedTitle = (title != null && title.trim().isEmpty()) ? null : title;
        String processedGenres = (genres != null && genres.trim().isEmpty()) ? null : genres;
        String processedStudio = (studio != null && studio.trim().isEmpty()) ? null : studio;
        
        Page<MovieDTO> moviePage = moviesRepository.searchMovies(processedTitle, processedGenres, duration, rating, year, processedStudio, pageable);
        
        // Apply completeness sorting to improve result quality
        List<MovieDTO> sortedMovies = MovieSortingUtils.sortByQuality(moviePage.getContent());
        
        // Return new page with sorted content
        return new PageImpl<>(sortedMovies, pageable, moviePage.getTotalElements());
    }

    /**
     * Retrieves movies by the specified ID.
     *
     * @param id the ID of the movie
     * @return a list of movies with the specified ID
     */
    public MovieDTO moviesById(Long id) {
        return moviesRepository.findMovieById(id);
    }

    /**
     * Finds the latest released movies up to the specified date.
     *
     * @param today the current date
     * @param pageable pagination information
     * @return a page of MovieDTO objects representing the latest released movies
     */
    public Page<MovieDTO> findLatestReleasedMovies(LocalDate today, Pageable pageable) {
        Page<MovieDTO> moviePage = moviesRepository.findLatestReleasedMovies(today, pageable);
        
        // Apply quality sorting (which now includes filtering invalid years)
        List<MovieDTO> sortedMovies = MovieSortingUtils.sortByQuality(moviePage.getContent());
        
        // Use the actual filtered count, not the original total
        return new PageImpl<>(sortedMovies, pageable, sortedMovies.size());
    }


    /**
     * Finds the top-rated movies.
     *
     * @param pageable pagination information
     * @return a page of MovieDTO objects representing the top-rated movies
     */
    public Page<MovieDTO> findTopRatedMovies(Pageable pageable) {
        Page<MovieDTO> moviePage = moviesRepository.findTopRatedMovies(pageable);
        
        // Apply quality sorting to improve result quality
        List<MovieDTO> sortedMovies = MovieSortingUtils.sortByQuality(moviePage.getContent());
        
        return new PageImpl<>(sortedMovies, pageable, moviePage.getTotalElements());
    }

    /**
     * Searches for movies based on the name of an actor.
     *
     * @param actorName the name of the actor
     * @param pageable pagination information
     * @return a page of MovieDTO objects featuring the specified actor
     */
    public Page<MovieDTO> searchMovieFromNameActor(String actorName, Pageable pageable) {
        Page<MovieDTO> moviePage = moviesRepository.searchMoviesByActorName(actorName, pageable);
        
        // Apply completeness sorting to improve result quality
        List<MovieDTO> sortedMovies = MovieSortingUtils.sortByQuality(moviePage.getContent());
        
        return new PageImpl<>(sortedMovies, pageable, moviePage.getTotalElements());
    }
}

