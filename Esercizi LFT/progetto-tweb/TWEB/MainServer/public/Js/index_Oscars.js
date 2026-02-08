async function loadOscarsData(){
    try{
        const response = await axios.get(`http://localhost:3001/api/20dataOscar`);
        const oscars = response.data.data || response.data;
        console.log(oscars);

        // Group oscars by category, year, and ceremony
        const groupedOscars = groupOscarsByCategory(oscars);
        console.log('Grouped Oscars:', groupedOscars);

        // Clear container
        document.getElementById("OscarsContainer").innerHTML = "";

        // Create cards for each category group
        groupedOscars.forEach(categoryGroup => {
            const card = document.createElement("div");
            card.className = "col-lg-6 col-md-8 col-sm-12 mb-4";
            
            // Find the winner in this category
            const winner = categoryGroup.nominees.find(nominee => nominee.winner);
            const nominees = categoryGroup.nominees.filter(nominee => !nominee.winner);
            
            card.innerHTML = `
                <div class="card shadow-lg h-100 oscar-category-card">
                    <div class="card-header bg-warning text-dark">
                        <h5 class="card-title mb-0">
                            <i class="fas fa-trophy me-2"></i>
                            ${categoryGroup.category}
                        </h5>
                        <small class="text-muted">
                            ${categoryGroup.year_ceremony} • Ceremony #${categoryGroup.ceremony}
                        </small>
                    </div>
                    <div class="card-body">
                        ${winner ? `
                            <div class="winner-section">
                                <h6 class="text-success mb-2">
                                    <i class="fas fa-crown me-1"></i> WINNER
                                </h6>
                                <div class="winner-card">
                                    <strong class="text-success">${winner.film}</strong>
                                    <br>
                                    <span class="text-muted">${winner.name}</span>
                                    ${winner.year_film ? `<br><small class="text-muted">Released: ${winner.year_film}</small>` : ''}
                                </div>
                            </div>
                        ` : ''}
                        
                        ${nominees.length > 0 ? `
                            <div class="nominees-section">
                                <button class="btn btn-outline-primary btn-sm w-100" type="button" data-bs-toggle="collapse" data-bs-target="#nominees-${categoryGroup.category.replace(/\s+/g, '')}-${categoryGroup.year_ceremony}" aria-expanded="false">
                                    <i class="fas fa-star me-1"></i> View ${nominees.length} Nominee${nominees.length > 1 ? 's' : ''}
                                    <i class="fas fa-chevron-down ms-2"></i>
                                </button>
                                <div class="collapse mt-2" id="nominees-${categoryGroup.category.replace(/\s+/g, '')}-${categoryGroup.year_ceremony}">
                                    ${nominees.map(nominee => `
                                        <div class="nominee-item">
                                            <strong>${nominee.film}</strong>
                                            <br>
                                            <span class="text-muted">${nominee.name}</span>
                                            ${nominee.year_film ? `<br><small class="text-muted">Released: ${nominee.year_film}</small>` : ''}
                                        </div>
                                    `).join('')}
                                </div>
                            </div>
                        ` : ''}
                    </div>
                </div>
            `;
            
            document.getElementById("OscarsContainer").appendChild(card);
        });

    }catch(error){
        console.error("Errore nel recupero degli oscar: ", error);
        document.getElementById("OscarsContainer").innerHTML = `<div class="alert alert-danger" role="alert">Errore nel caricamento degli oscars. Riprova più tardi.</div>`;
    }

}

function groupOscarsByCategory(oscars) {
    const grouped = {};
    
    oscars.forEach(oscar => {
        // Create a unique key for each category-year-ceremony combination
        const key = `${oscar.category}-${oscar.year_ceremony}-${oscar.ceremony}`;
        
        if (!grouped[key]) {
            grouped[key] = {
                category: oscar.category,
                year_ceremony: oscar.year_ceremony,
                ceremony: oscar.ceremony,
                nominees: []
            };
        }
        
        grouped[key].nominees.push({
            film: oscar.film,
            name: oscar.name,
            year_film: oscar.year_film,
            winner: oscar.winner
        });
    });
    
    // Convert to array and sort by ceremony year (most recent first)
    return Object.values(grouped).sort((a, b) => b.year_ceremony - a.year_ceremony);
}

function scrollToChat() {
    document.getElementById("chatbox").scrollIntoView({ behavior: "smooth" });
}



function init() {
    loadOscarsData();
}

