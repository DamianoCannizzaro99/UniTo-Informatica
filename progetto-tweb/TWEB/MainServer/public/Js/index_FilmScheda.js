
async function fetchFilmData() {
    try {
        const movieId = getMovieIdFromUrl();
        if (!movieId) {
            throw new Error('ID del film non trovato nell\'URL.');
        }

        // Essential data - if this fails, we can't show the page
        let film = null;
        try {
            const response = await axios.get(`http://localhost:8081/movies/id/${movieId}`);
            film = response.data;
        } catch (error) {
            console.error('Errore nel caricamento dei dati essenziali del film:', error);
            throw error; // This is mandatory, so we throw if it fails
        }

        // Optional data - if these fail, we'll show defaults
        let countries = [];
        try {
            const responseDataCountries = await axios.get(`http://localhost:8081/countries/id/${movieId}`);
            countries = responseDataCountries.data;
        } catch (error) {
            console.warn(`Nessun paese trovato per il film con ID ${movieId}:`, error);
        }

        let DataCrews = [];
        try {
            const responseDataCrews = await axios.get(`http://localhost:8081/crew/id/${movieId}`);
            DataCrews = responseDataCrews.data;
        } catch (error) {
            console.warn(`Nessun crew trovato per il film con ID ${movieId}:`, error);
        }

        let actors = [];
        try {
            const responseDataActors = await axios.get(`http://localhost:8081/actors/by-movie-id/${movieId}?size=250`);
            actors = responseDataActors.data;
        } catch (error) {
            console.warn(`Nessun attore trovato per il film con ID ${movieId}:`, error);
        }

        let studios = [];
        try {
            const responseDataStudios = await axios.get(`http://localhost:8081/api/studios/studios-by-movie/${movieId}`);
            studios = responseDataStudios.data;
        } catch (error) {
            console.warn(`Nessuno studio trovato per il film con ID ${movieId}:`, error);
        }

        let languages = [];
        try {
            const responseDataLanguages = await axios.get(`http://localhost:8081/languages/id/${movieId}`);
            languages = responseDataLanguages.data;
        } catch (error) {
            console.warn(`Nessuna lingua trovata per il film con ID ${movieId}:`, error);
        }

        let genres = [];
        try {
            const responseDataGenres = await axios.get(`http://localhost:8081/genres/by-movie/${movieId}`);
            genres = responseDataGenres.data;
        } catch (error) {
            console.warn(`Nessun genere trovato per il film con ID ${movieId}:`, error);
        }

        let DataThemes = [];
        try {
            const responseDataThemes = await axios.get(`http://localhost:8081/themes/id/${movieId}`);
            DataThemes = responseDataThemes.data;
        } catch (error) {
            console.warn(`Nessun tema trovato per il film con ID ${movieId}:`, error);
        }

        // Basic movie info from MovieDTO
        document.getElementById("filmRelease").innerText = film.relaseYear || 'N/A';
        document.getElementById('filmTitle').innerText = film.title;
        document.getElementById('filmPoster').src = film.posterLink || 'img/not_found.png';
        document.getElementById('filmDuration').innerText = (film.runtime || 0) + " minuti";
        document.getElementById('filmRating').innerText = (film.rating || 0).toFixed(2);
        
        // Log the movie title being used for debugging
        console.log(`[FRONTEND DEBUG] Movie title from API: "${film.title}"`);
        console.log(`[FRONTEND DEBUG] Movie ID: ${movieId}`);
        console.log(`[FRONTEND DEBUG] Full movie data:`, film);

        let rating = film.rating !== null ? film.rating : 0;
        let stars = generateStars(rating);
        document.getElementById("startRating").innerHTML = stars;
        document.getElementById('filmDescription').innerText = film.description || 'Inseriremo la descrizione il prima possibile. Scusate il disagio.';


        document.getElementById('filmActors').innerHTML = createCollapsibleList(actors, 'actors', (actor) => 
            `<a href="/Actors?name=${actor.name}" class="text-primary">${actor.name}</a> <span class="text-muted">as ${actor.role}</span>`
        );
        
        // Create smooth collapsible crew section  
        document.getElementById('crewDescription').innerHTML = createCollapsibleList(DataCrews, 'crew', (crew) => 
            `${crew.name} <span class="text-muted">as ${crew.role}</span>`
        );



        const filmGenres = genres.map(genre => genre.genreName).join(', ');
        document.getElementById('FilmGenre').innerText = filmGenres || 'No inoformation available for genres';


        const filmLanguages = languages.map(language => language.language).join(', ');
        document.getElementById('filmLanguage').innerText = filmLanguages || 'No information available for languages';

        const filmStudios = studios.map(studio => studio.studio).join(', ');
        document.getElementById('filmStudio').innerText = filmStudios || 'No information available for studios';

        const country = countries.map(country => `${country.country}`).join(', ');
        document.getElementById('filmCountry').innerText = country || 'No country info available';

        const themes = DataThemes.length > 0 ? DataThemes.map(theme => theme.theme).join(', ') : "N.A.";
        document.getElementById('filmTheme').innerText = themes;

    } catch (error) {
        console.error('Errore nel caricamento dei dati del film:', error);
        // Show error message to user
        document.getElementById('filmTitle').innerText = 'Errore nel caricamento del film';
        document.getElementById('filmDescription').innerText = 'Si è verificato un errore durante il caricamento dei dati del film. Riprova più tardi.';
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

function getMovieIdFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('id');
}

function createCollapsibleList(items, type, formatter) {
    if (!items || items.length === 0) {
        return `<span class="text-muted">No ${type} info available</span>`;
    }
    
    const maxVisible = 3; // Show first 3 items
    const visibleItems = items.slice(0, maxVisible);
    const hiddenItems = items.slice(maxVisible);
    
    let html = '<div class="collapsible-list">';
    
    // Always visible items
    visibleItems.forEach(item => {
        html += `<div class="mb-1">${formatter(item)}</div>`;
    });
    
    // Hidden items (if any)
    if (hiddenItems.length > 0) {
        html += `<div class="collapse" id="${type}Collapse">`;
        hiddenItems.forEach(item => {
            html += `<div class="mb-1">${formatter(item)}</div>`;
        });
        html += '</div>';
        
        // Show more/less button
        html += `
            <button class="btn btn-link p-0 mt-2 text-decoration-none" 
                    onclick="toggleCollapse('${type}')" 
                    id="${type}ToggleBtn">
                <i class="fas fa-chevron-down me-1"></i>
                <span id="${type}ToggleText">Show ${hiddenItems.length} more</span>
            </button>`;
    }
    
    html += '</div>';
    return html;
}

function toggleCollapse(type) {
    const collapseDiv = document.getElementById(`${type}Collapse`);
    const toggleBtn = document.getElementById(`${type}ToggleBtn`);
    const toggleText = document.getElementById(`${type}ToggleText`);
    const icon = toggleBtn.querySelector('i');
    
    if (collapseDiv.classList.contains('show')) {
        // Currently expanded, collapse it
        collapseDiv.classList.remove('show');
        icon.className = 'fas fa-chevron-down me-1';
        const hiddenCount = collapseDiv.children.length;
        toggleText.textContent = `Show ${hiddenCount} more`;
    } else {
        // Currently collapsed, expand it
        collapseDiv.classList.add('show');
        icon.className = 'fas fa-chevron-up me-1';
        toggleText.textContent = 'Show less';
    }
}


function init() {
    fetchFilmData();
    
    // Setup reviews event listeners (from index_Reviews.js)
    if (typeof setupReviewsEventListeners === 'function') {
        setupReviewsEventListeners();
    }
    
    // Initialize reviews after a short delay to let the page load first
    setTimeout(() => {
        const movieId = getMovieIdFromUrl();
        if (movieId && typeof initializeReviews === 'function') {
            initializeReviews(movieId);
        }
    }, 500);
}
