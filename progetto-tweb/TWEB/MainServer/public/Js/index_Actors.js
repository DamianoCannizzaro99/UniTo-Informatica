async function loadActorImage(actorName) {
    const actorImage = document.getElementById('actorImage');
    const imageLoader = document.getElementById('imageLoader');
    
    try {
        const response = await fetch(`/api/actor-image/${encodeURIComponent(actorName)}`);
        const data = await response.json();
        
        if (data.success && data.imageUrl) {
            actorImage.src = data.imageUrl;
            actorImage.style.display = 'block';
            imageLoader.style.display = 'none';
        } else {
            // If no image found, show placeholder
            actorImage.src = '/images/placeholder-actor.svg';
            actorImage.style.display = 'block';
            imageLoader.style.display = 'none';
        }
    } catch (error) {
        console.error('Error loading actor image:', error);
        // Show placeholder on error
        actorImage.src = '/images/placeholder-actor.svg';
        actorImage.style.display = 'block';
        imageLoader.style.display = 'none';
    }
}

async function init() {
    console.log("Inizializzazione in corso...");
    const movieContainer = document.getElementById("movies-container");
    const params = new URLSearchParams(window.location.search);
    const actorName = params.get("name");
    
    // Load actor image
    if (actorName) {
        loadActorImage(actorName);
    }
    try {
        let response = "";
        // Axios GET request
        if (!actorName) {
            console.error("Nessun nome specificato nella query string");
        } else {
           response = await axios.get(`http://localhost:8081/movies/name`, {
                params: { actorName }
            });
        }


        // Checks if data exists
        let actors = response.data;

        if (!actors || actors.length === 0) {
            movieContainer.innerHTML = "<p class='text-danger'>Nessun film trovato con questo attore.</p>";
            return;
        }

        movieContainer.innerHTML = "";

        // Filter movies to only show those with good data quality
        const qualityMovies = actors.filter(actor => 
            actor.title && 
            actor.title.trim() !== '' && 
            (actor.posterLink || actor.rating > 0)
        );
        
        if (qualityMovies.length === 0) {
            movieContainer.innerHTML = "<p class='text-warning'>No quality movies found for this actor.</p>";
            return;
        }
        
        qualityMovies.forEach(actor => {
            const card = document.createElement("div");
            card.classList.add("col-md-4", "mb-4");
            let posterUrl = (actor.posterLink && actor.posterLink.trim() !== '') ? actor.posterLink : "img/not_found.png";
            let rating = actor.rating !== null ? actor.rating : 0;
            let stars = generateStars(rating);
            let year = actor.relaseYear ? `(${actor.relaseYear})` : '';
            let description = actor.description ? 
                (actor.description.length > 100 ? actor.description.substring(0, 100) + '...' : actor.description) : 
                'Click for more details';
            
            card.innerHTML = `
                <div class="card shadow-lg h-100">
                    <img src="${posterUrl}" class="card-img-top" alt="${actor.title}">
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">${actor.title} ${year}</h5>
                        <p class="text-muted small flex-grow-1">${description}</p>
                        <div class="mt-auto">
                            <p class="mb-2">${stars} ${rating.toFixed(1)}</p>
                            <a href="/FilmScheda?id=${actor.id}" class="btn btn-primary w-100">View Details</a>
                        </div>
                    </div>
                </div>`;

            movieContainer.appendChild(card);
        });

        console.log("Film caricati con successo!");
    } catch (error) {
        // Axios errors handler
        console.error("Errore nel caricamento dei film:", error);

        // Show error message in DOM
        if (error.response) {
            // Response received with error (e.g. 404, 500)
            console.error("Errore nella risposta del server:", error.response.status);
            movieContainer.innerHTML = `<p class='text-danger'>Errore nel caricamento dei film (status: ${error.response.status}).</p>`;
        } else if (error.request) {
            // Request went ok but no response
            console.error("Errore nella richiesta:", error.request);
            movieContainer.innerHTML = "<p class='text-danger'>Errore nella richiesta al server.</p>";
        } else {
            //generic errors handler
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

