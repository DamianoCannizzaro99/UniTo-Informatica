
let currentPage = 0;
const moviesPerPage = 9;
let allMovies = [];
let selectedGenres = new Set(); // Track selected genres

// Mostra/nasconde la barra dei filtri
function toggleFilters() {
    let filters = document.getElementById("filters");
    if (filters.classList.contains("filters-visible")) {
        filters.classList.remove("filters-visible");
        filters.classList.add("filters-hidden");
    } else {
        filters.classList.remove("filters-hidden");
        filters.classList.add("filters-visible");
    }
}

// Update the selected genres text
function updateSelectedGenresText() {
    const selectedGenresText = document.getElementById("selectedGenresText");
    if (selectedGenres.size === 0) {
        selectedGenresText.textContent = "Tutti i generi";
    } else if (selectedGenres.size === 1) {
        selectedGenresText.textContent = Array.from(selectedGenres)[0];
    } else {
        selectedGenresText.textContent = `${selectedGenres.size} generi selezionati`;
    }
}

// Handle genre checkbox changes
function handleGenreChange(checkbox, genreName) {
    const allGenresCheckbox = document.getElementById("allGenres");
    
    if (checkbox.id === "allGenres") {
        // If "All genres" is checked, uncheck all others and clear selection
        if (checkbox.checked) {
            selectedGenres.clear();
            document.querySelectorAll('.genre-checkbox').forEach(cb => {
                cb.checked = false;
            });
        }
    } else {
        // Handle individual genre selection
        if (checkbox.checked) {
            selectedGenres.add(genreName);
            allGenresCheckbox.checked = false;
        } else {
            selectedGenres.delete(genreName);
            // If no genres selected, check "All genres"
            if (selectedGenres.size === 0) {
                allGenresCheckbox.checked = true;
            }
        }
    }
    
    updateSelectedGenresText();
}

document.addEventListener("DOMContentLoaded", async function () {
    const genreDropdownMenu = document.getElementById("genreDropdownMenu");

    try {
        let response = await fetch("http://localhost:8081/genres/names");
        let genres = await response.json();
        console.log(genres);

        // Add genre checkboxes to dropdown
        genres.forEach((genreName, index) => {
            const li = document.createElement("li");
            li.innerHTML = `
                <div class="px-3 py-2">
                    <div class="form-check">
                        <input class="form-check-input genre-checkbox" type="checkbox" value="${genreName}" id="genre_${index}" onchange="handleGenreChange(this, '${genreName}')">
                        <label class="form-check-label" for="genre_${index}">
                            ${genreName}
                        </label>
                    </div>
                </div>
            `;
            genreDropdownMenu.appendChild(li);
        });
    } catch (error) {
        console.error("Errore nel caricamento dei generi:", error);
    }
});

async function searchMovies() {
    try {
        let title = document.getElementById("searchTitle").value;
        let duration = document.getElementById("searchDuration").value;
        let rating = document.getElementById("searchRating").value;
        let year = document.getElementById("searchYear").value;
        
        // Convert selected genres Set to comma-separated string
        let genresString = selectedGenres.size > 0 ? Array.from(selectedGenres).join(',') : '';

        let queryParams = [];
        if (title) queryParams.push(`title=${encodeURIComponent(title)}`);
        if (genresString) queryParams.push(`genres=${encodeURIComponent(genresString)}`);
        if (duration) queryParams.push(`duration=${encodeURIComponent(duration)}`);
        if (rating) queryParams.push(`rating=${encodeURIComponent(rating)}`);
        if (year) queryParams.push(`year=${encodeURIComponent(year)}`);

        let queryString = queryParams.length > 0 ? `?${queryParams.join("&")}` : "";

        window.location.href = `/FilmSearched${queryString}`;

    } catch (error) {
        console.error("Errore nella ricerca dei film:", error);
    }
}
