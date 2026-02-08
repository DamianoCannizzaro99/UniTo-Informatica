package javaserver.util;

import javaserver.model.dto.MovieDTO;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Utility class for sorting movies based on various criteria.
 */
public class MovieSortingUtils {

    private static final int MAX_VALID_YEAR = 2025;
    private static final int MIN_VALID_YEAR = 1888; // First known film

    /**
     * Sorts movies by completeness (number of non-null fields) in descending order.
     * Movies with more complete data appear first.
     * 
     * @param movies list of movies to sort
     * @return sorted list with most complete movies first
     */
    public static List<MovieDTO> sortByCompleteness(List<MovieDTO> movies) {
        return movies.stream()
            .sorted(Comparator.comparingDouble(MovieSortingUtils::calculateCompleteness).reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Sorts movies by a composite quality score that balances rating, completeness, and recency.
     * This provides a comprehensive "quality" sorting with equal priority for all factors.
     * Only includes movies with minimum quality standards.
     * 
     * @param movies list of movies to sort
     * @return sorted list optimized for user experience
     */
    public static List<MovieDTO> sortByQuality(List<MovieDTO> movies) {
        return movies.stream()
            .filter(movie -> movie.getTitle() != null && !movie.getTitle().trim().isEmpty()) // Basic filter: must have title
            .sorted((movie1, movie2) -> {
                double score1 = calculateCompleteness(movie1);
                double score2 = calculateCompleteness(movie2);
                return Double.compare(score2, score1); // Higher score first (score2 - score1)
            })
            .collect(Collectors.toList());
    }
    

    /**
     * Calculates completeness score for a movie based on non-null fields.
     * Higher score means more complete data.
     * 
     * @param movie the movie to evaluate
     * @return completeness score (0-8)
     */
    private static double calculateCompleteness(MovieDTO movie) {
        double score = 0;
        
        // Core fields
        if (movie.getTitle() != null && !movie.getTitle().trim().isEmpty()) score += 1;
        if (movie.getDescription() != null && !movie.getDescription().trim().isEmpty()) score += 0.5;
        if (isValidYear(movie.getRelaseYear())) score += (double)movie.getRelaseYear() / 10; // Scale year to 0-10
        if (movie.getRuntime() != null && movie.getRuntime() > 0) score += 0.5;
        // Quality indicators (higher weight)
        if (movie.getRating() != null ) {
            if (movie.getRating() > 0.5) score += 0.5;
            else if (movie.getRating() > 2) score += 10;
            else score += 0.25; // Minimum threshold for rating
        }else score -= 10; // Penalize if no rating
        if (movie.getPosterLink() != null && !movie.getPosterLink().trim().isEmpty()) score += 1.5;
        
        // Optional fields
        if (movie.getTagline() != null && !movie.getTagline().trim().isEmpty()) score += 0.5;

        // returns score (min -10, max 24)
        return score;
    }

    
    /**
     * Validates if a year is within acceptable range for movies.
     * 
     * @param year the year to validate
     * @return true if year is between 1888 and 2025 (inclusive)
     */
    private static boolean isValidYear(Integer year) {
        return year != null && year >= MIN_VALID_YEAR && year <= MAX_VALID_YEAR;
    }

}
