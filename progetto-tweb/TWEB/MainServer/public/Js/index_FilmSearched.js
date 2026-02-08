async function init() {
    console.log("Inizializzazione in corso...");
    const movieContainer = document.getElementById("movies-container");

    // Get query string parameters
    const queryString = window.location.search;

    try {
        // Debug: show the query string
        console.log("Query string:", queryString);

        // Axios GET request
        let response = await axios.get(`http://localhost:8081/movies/search${queryString}`);

        // Checks data
        let movies = response.data;

        if (!movies || movies.length === 0) {
            movieContainer.innerHTML = "<p class='text-danger'>Nessun film trovato.</p>";
            return;
        }

        movieContainer.innerHTML = ""; // Clean container before getting another movie

        // Filter movies to only show those with good data quality
        const qualityMovies = movies.filter(movie => 
            movie.title && 
            movie.title.trim() !== '' && 
            (movie.posterLink || movie.rating > 0)
        );
        
        if (qualityMovies.length === 0) {
            movieContainer.innerHTML = "<p class='text-warning'>No quality movies found matching your criteria.</p>";
            return;
        }
        
        qualityMovies.forEach(movie => {
            const card = document.createElement("div");
            card.classList.add("col-md-4", "mb-4");
            let posterUrl = (movie.posterLink && movie.posterLink.trim() !== '') ? movie.posterLink : "img/not_found.png";
            let rating = movie.rating !== null ? movie.rating : 0;
            let stars = generateStars(rating);
            let year = movie.relaseYear ? `(${movie.relaseYear})` : '';
            let description = movie.description ? 
                (movie.description.length > 100 ? movie.description.substring(0, 100) + '...' : movie.description) : 
                'Click for more details';
            
            card.innerHTML = `
                <div class="card shadow-lg h-100">
                    <img src="${posterUrl}" class="card-img-top" alt="${movie.title}">
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">${movie.title} ${year}</h5>
                        <p class="text-muted small flex-grow-1">${description}</p>
                        <div class="mt-auto">
                            <p class="mb-2">${stars} ${rating.toFixed(1)}</p>
                            <a href="/FilmScheda?id=${movie.id}" class="btn btn-primary w-100">View Details</a>
                        </div>
                    </div>
                </div>`;

            movieContainer.appendChild(card);
        });

        console.log("Film caricati con successo!");
    } catch (error) {
        // axios error handler
        console.error("Errore nel caricamento dei film:", error);

        // Show error in the DOM
        if (error.response) {
            // error code given by server
            console.error("Errore nella risposta del server:", error.response.status);
            movieContainer.innerHTML = `<p class='text-danger'>Errore nel caricamento dei film (status: ${error.response.status}).</p>`;
        } else if (error.request) {
            // Request was made but no response received
            console.error("Errore nella richiesta:", error.request);
            movieContainer.innerHTML = "<p class='text-danger'>Errore nella richiesta al server.</p>";
        } else {
            // Generic error handler
            console.error("Errore generale:", error.message);
            movieContainer.innerHTML = "<p class='text-danger'>Errore nel caricamento dei film.</p>";
        }
    }
}
function generateStars(rating) {
    let stars = "";
    const fullStars = Math.floor(rating); // Number of full stars
    const halfStar = rating % 1 !== 0; // Check if there's a half star
    const emptyStars = 5 - fullStars - (halfStar ? 1 : 0); // Number of empty stars

    // Add full stars
    for (let i = 0; i < fullStars; i++) {
        stars += '<i class="fas fa-star text-warning"></i>';
    }

    // Add half star if needed
    if (halfStar) {
        stars += '<i class="fas fa-star-half-alt text-warning"></i>';
    }

    // Add empty stars
    for (let i = 0; i < emptyStars; i++) {
        stars += '<i class="far fa-star text-warning"></i>';
    }

    return stars;
}

