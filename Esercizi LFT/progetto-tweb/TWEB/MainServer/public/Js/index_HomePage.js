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

async function loadFilmsWithCarousel() {
    try {
        const response = await axios.get('http://localhost:8081/movies/latest-releases');
        const carouselInner = document.getElementById("carousel-inner");
        const carouselIndicators = document.getElementById("carousel-indicators");

        // Empty containers
        carouselInner.innerHTML = "";
        carouselIndicators.innerHTML = "";

        response.data.forEach((movie, index) => {
            let isActive = index === 0 ? "active" : "";
            let posterUrl = movie.posterLink || "img/not_found.png";
            let description = movie.description || "Inseriremo la descrizione il prima possibile. Scusate il disagio.";
            // Side by side slides
            let item = `
                <div class="carousel-item ${isActive}">
                    <div class="carousel-poster">
                        <img src="${posterUrl}" alt="${movie.title}">
                    </div>
                    <div class="carousel-content">
                        <h1>${movie.title}</h1>
                        <div class="carousel-description">
                            <p>${description}</p>
                        </div>
                        <a href="/FilmScheda?id=${movie.id}" class="btn">Scopri di più</a>
                    </div>
                </div>`;
            carouselInner.innerHTML += item;

            // Creazione degli indicatori
            let indicator = `<button type="button" data-bs-target="#carouselExampleCaptions" 
                                data-bs-slide-to="${index}" class="${isActive}" 
                                aria-label="Slide ${index + 1}"></button>`;
            carouselIndicators.innerHTML += indicator;
        });

    } catch (error) {
        console.error("Errore nel recupero dei film:", error);
    }
}

async function loadMovieDetails() {
    // Check if movies are already loaded from server
    const moviesContainer = document.getElementById("movies-container");
    if (!moviesContainer || moviesContainer.children.length > 0) {
        // Movies already loaded from server, no need to fetch
        return;
    }
    
    // Fallback: Load movies via JavaScript if not loaded from server
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

        moviesContainer.innerHTML = `<div class="row">${cardsHTML}</div>`;

    } catch (error) {
        console.error("Errore nel recupero dei film:", error);
        // Mostra un messaggio di errore all'utente
        moviesContainer.innerHTML = `<div class="alert alert-danger" role="alert">Errore nel caricamento dei film. Riprova più tardi.</div>`;
    }
}

function generateStars(rating) {
    let stars = "";
    const fullStars = Math.floor(rating); // Numero di stelle piene
    const halfStar = rating % 1 !== 0; // Verifica se c'è una mezza stella
    const emptyStars = 5 - fullStars - (halfStar ? 1 : 0); // Numero di stelle vuote

    // Aggiungi stelle piene
    for (let i = 0; i < fullStars; i++) {
        stars += '<i class="fas fa-star text-warning"></i>';
    }

    // Aggiungi mezza stella (se necessario)
    if (halfStar) {
        stars += '<i class="fas fa-star-half-alt text-warning"></i>';
    }

    // Aggiungi stelle vuote
    for (let i = 0; i < emptyStars; i++) {
        stars += '<i class="far fa-star text-warning"></i>';
    }

    return stars;
}
document.addEventListener("DOMContentLoaded", loadMovieDetails);
function init() {
    loadFilmsWithCarousel();
    loadMovieDetails();
}

