function toggleFilters() {
    var filters = document.getElementById("filters");
    if (filters.classList.contains("filters-visible")) {
        filters.classList.remove("filters-visible");
        filters.classList.add("filters-hidden");
    } else {
        filters.classList.remove("filters-hidden");
        filters.classList.add("filters-visible");
    }
}

async function loadMovieDetails() {
    try {
        const response = await axios.get("http://localhost:8081/movies/TopRate");
        const movies = response.data;
        console.log(movies);
        let cardsHTML = "";
        // Filter movies to only show those with good data quality
        const qualityMovies = movies.filter(movie => 
            movie.title && 
            movie.title.trim() !== '' && 
            (movie.posterLink || movie.rating > 0)
        );
        
        qualityMovies.forEach(movie => {
            let posterUrl = (movie.posterLink && movie.posterLink.trim() !== '') ? movie.posterLink : "img/not_found.png";
            let rating = movie.rating !== null ? movie.rating : 0;
            let stars = generateStars(rating);
            let year = movie.relaseYear ? `(${movie.relaseYear})` : '';
            let description = movie.description ? 
                (movie.description.length > 100 ? movie.description.substring(0, 100) + '...' : movie.description) : 
                'Click for more details';
            
            cardsHTML += `
                <div class="col-md-4 mb-4">
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
                    </div>
                </div>`;
        });

        document.getElementById("movies-container").innerHTML = `<div class="row">${cardsHTML}</div>`;

    } catch (error) {
        console.error("Errore nel recupero dei film:", error);
        // Mostra un messaggio di errore all'utente
        document.getElementById("movies-container").innerHTML = `<div class="alert alert-danger" role="alert">Errore nel caricamento dei film. Riprova più tardi.</div>`;
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
document.addEventListener("DOMContentLoaded", loadMovieDetails);
function init() {
    loadMovieDetails();
}

