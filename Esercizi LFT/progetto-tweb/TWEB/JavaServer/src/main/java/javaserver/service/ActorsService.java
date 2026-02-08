package javaserver.service;

import javaserver.model.dto.ActorDTO;
import javaserver.repository.MoviesRepository;
import javaserver.repository.ActorsRepository;
import javaserver.model.dto.MovieDTO;
import javaserver.util.MovieSortingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ActorsService {

    private final ActorsRepository actorsRepository;
    private final MoviesRepository moviesRepository;

    @Autowired
    public ActorsService(ActorsRepository actorsRepository, MoviesRepository moviesRepository) {
        this.actorsRepository = actorsRepository;
        this.moviesRepository = moviesRepository;
    }

    /**
     * Retrieves movies by the specified actor name.
     *
     * @param actorName the name of the actor
     * @param pageable pagination information
     * @return a page of movies featuring the specified actor
     */
    public Page<MovieDTO> findMoviesByActorName(String actorName, Pageable pageable) {
        Page<MovieDTO> moviePage = actorsRepository.findMoviesByActorName(actorName, pageable);
        
        // Apply completeness sorting to improve result quality
        List<MovieDTO> sortedMovies = MovieSortingUtils.sortByQuality(moviePage.getContent());
        
        return new PageImpl<>(sortedMovies, pageable, moviePage.getTotalElements());
    }

    /**
     * Retrieves actors by the specified movie name.
     *
     * @param movieName the name of the movie
     * @param pageable pagination information
     * @return a page of actors in the specified movie
     */
    public Page<ActorDTO> getActorsByMovieName(String movieName, Pageable pageable) {
        // First try exact match, then partial match
        Optional<Long> movieIdOpt = moviesRepository.findMovieIdByExactName(movieName);
        if (movieIdOpt.isEmpty()) {
            movieIdOpt = moviesRepository.findMovieIdByName(movieName);
        }
        
        if (movieIdOpt.isEmpty()) {
            return Page.empty(); // Return an empty page if no movie found
        }
        return actorsRepository.findActorsByMovieId(movieIdOpt.get(), pageable);
    }
    
    /**
     * Finds all movies with the exact same name.
     *
     * @param movieName the name of the movie
     * @return a list of all movies with that exact name
     */
    public List<MovieDTO> findAllMoviesWithName(String movieName) {
        List<MovieDTO> movies = moviesRepository.findAllMoviesByExactName(movieName);
        
        // Apply completeness sorting to improve result quality
        return MovieSortingUtils.sortByQuality(movies);
    }
    
    /**
     * Retrieves actors by movie ID.
     *
     * @param movieId the ID of the movie
     * @param pageable pagination information
     * @return a page of actors in the specified movie
     */
    public Page<ActorDTO> getActorsByMovieId(Long movieId, Pageable pageable) {
        return actorsRepository.findActorsByMovieId(movieId, pageable);
    }

}
