async function searchOscars() {
    try {
        let title = document.getElementById("searchTitle").value;
        let year = document.getElementById("searchYear").value;
        
        // Build query parameters
        let queryParams = new URLSearchParams();
        if (title && title.trim() !== '') {
            queryParams.append('film', title.trim());
        }
        if (year && year.trim() !== '') {
            queryParams.append('year_ceremony', year.trim());
        }
        
        // If no filters are selected, show all Oscars
        if (queryParams.toString() === '') {
            window.location.href = '/oscars';
            return;
        }
        
        console.log('Search parameters:', queryParams.toString());
        window.location.href = `/oscarsSearched?${queryParams.toString()}`;
    } catch (error) {
        console.error("Errore nella ricerca degli Oscar:", error);
    }
}

function clearSearch() {
    document.getElementById("searchTitle").value = '';
    document.getElementById("searchYear").value = '';
    // Redirect to show all Oscars
    window.location.href = '/oscars';
}

async function populateYearDropdown() {
    try {
        // Get available years from MongoDB server
        const response = await axios.get('http://localhost:3001/api/oscarYears');
        const years = response.data.data || response.data;
        
        const yearSelect = document.getElementById('searchYear');
        
        // Clear existing options (keep "All Years")
        const allYearsOption = yearSelect.querySelector('option[value=""]');
        yearSelect.innerHTML = '';
        yearSelect.appendChild(allYearsOption);
        
        // Add year options
        years.forEach(year => {
            const option = document.createElement('option');
            option.value = year;
            option.textContent = year;
            yearSelect.appendChild(option);
        });
        
        // Pre-select filters from URL if present
        const urlParams = new URLSearchParams(window.location.search);
        const selectedYear = urlParams.get('year_ceremony');
        const selectedFilm = urlParams.get('film');
        
        if (selectedYear) {
            yearSelect.value = selectedYear;
        }
        if (selectedFilm) {
            document.getElementById('searchTitle').value = selectedFilm;
        }
        
    } catch (error) {
        console.error('Error loading years:', error);
        // If years can't be loaded, populate with common recent years as fallback
        const yearSelect = document.getElementById('searchYear');
        const currentYear = new Date().getFullYear();
        for (let year = currentYear; year >= currentYear - 10; year--) {
            const option = document.createElement('option');
            option.value = year;
            option.textContent = year;
            yearSelect.appendChild(option);
        }
    }
}

// Initialize dropdown when page loads
document.addEventListener('DOMContentLoaded', populateYearDropdown);

