const modelRottenTomatoes =require('../Model/rottenTomatoesReviews')
const modelTheOscarAwards =require('../Model/theOscarAwards')


exports.getAllDataRottenTomatoes = async (req,res) => {
    try{
        console.log('Fetching Rotten Tomatoes data...');
        const CollectionRottenTomatoes = await modelRottenTomatoes.find().limit(100);
        
        if (!CollectionRottenTomatoes || CollectionRottenTomatoes.length === 0) {
            return res.status(404).json({
                success: false,
                message: 'No Rotten Tomatoes reviews found',
                data: []
            });
        }
        
        console.log(`Found ${CollectionRottenTomatoes.length} Rotten Tomatoes reviews`);
        res.json({
            success: true,
            count: CollectionRottenTomatoes.length,
            data:{
                rottenTomatoesReviews : CollectionRottenTomatoes,
            },
        });
    }catch (error){
        console.error('Error fetching Rotten Tomatoes data:', error);
        res.status(500).json({
            success: false,
            message: 'Error fetching Rotten Tomatoes reviews',
            error: error.message,
            stack: process.env.NODE_ENV === 'development' ? error.stack : undefined
        });
    }
}
exports.getAllDataTheOscarAwards = async (req,res) => {
    try{
        console.log('Fetching Oscar Awards data...');
        const CollectionTheOscarAwards = await modelTheOscarAwards.find().limit(100);
        
        if (!CollectionTheOscarAwards || CollectionTheOscarAwards.length === 0) {
            return res.status(404).json({
                success: false,
                message: 'No Oscar Awards found',
                data: []
            });
        }
        
        console.log(`Found ${CollectionTheOscarAwards.length} Oscar Awards`);
        res.json({
            success: true,
            count: CollectionTheOscarAwards.length,
            data:{
                theOscarAwards : CollectionTheOscarAwards,
            },
        });
    }catch (error){
        console.error('Error fetching Oscar Awards data:', error);
        res.status(500).json({
            success: false,
            message: 'Error fetching Oscar Awards',
            error: error.message,
            stack: process.env.NODE_ENV === 'development' ? error.stack : undefined
        });
    }
}


//@returns the most recent 100 Oscar Awards
exports.get100OscarAwards = async (req, res) => {
    try {
        const oscars = await modelTheOscarAwards
            .find()
            .sort({ year_ceremony: -1 }) // Ordina per cerimonia, dalla più recente alla più vecchia
            .limit(100); // Limita il numero di risultati

        if (oscars.length === 0) {
            return res.status(404).json({ success: false, message: "Nessun Oscar trovato" });
        }
        res.json({
            success: true,
            data: oscars,
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: "Errore nel recupero dei dati degli Oscar",
            error: error.message,
        });
    }
};



//@returns oscars filtered by year (simplified version)
exports.getOscars = async (req, res) => {
    try {
        const { year_ceremony } = req.query;
        
        // Build search filter object
        let searchFilter = {};
        let searchDescription = 'Recent Oscars';
        
        // Film search functionality
        const { film } = req.query;
        if (film && film.trim() !== '') {
            if (film.trim().length < 2) {
                return res.status(400).json({ 
                    success: false, 
                    message: "Il termine di ricerca del film deve contenere almeno 2 caratteri" 
                });
            }
            searchFilter.film = new RegExp(film.trim(), 'i');
            searchDescription = searchDescription.replace('Recent Oscars', `Film: "${film.trim()}"`);
        }
        
        // Add year filter if provided
        if (year_ceremony && year_ceremony.trim() !== '') {
            const year = parseInt(year_ceremony.trim());
            if (isNaN(year) || year < 1900 || year > new Date().getFullYear() + 5) {
                return res.status(400).json({ 
                    success: false, 
                    message: "Anno della cerimonia non valido" 
                });
            }
            searchFilter.year_ceremony = year;
            searchDescription = `Oscars ${year}`;
        }
        
        console.log(`Filtering Oscars: ${searchDescription}`);
        
        const oscars = await modelTheOscarAwards
            .find(searchFilter)
            .sort({ year_ceremony: -1, ceremony: -1 }) // Sort by ceremony year, most recent first
            .limit(200); // Reasonable limit to avoid overload
            
        if (oscars.length === 0) {
            return res.status(404).json({ 
                success: false, 
                message: `Nessun Oscar trovato per: ${searchDescription}` 
            });
        }
        
        console.log(`Found ${oscars.length} Oscar(s) for: ${searchDescription}`);
        res.json({
            success: true,
            count: oscars.length,
            searchTerm: searchDescription,
            filters: { year_ceremony },
            data: oscars,
        });
    } catch (error) {
        console.error('Error filtering Oscars:', error);
        res.status(500).json({
            success: false,
            message: "Errore nel recupero dei dati degli Oscar",
            error: error.message,
            stack: process.env.NODE_ENV === 'development' ? error.stack : undefined
        });
    }
};

//@returns available ceremony years for filter dropdown
exports.getOscarYears = async (req, res) => {
    try {
        console.log('Fetching available Oscar ceremony years...');
        
        // Get distinct ceremony years from the collection
        const years = await modelTheOscarAwards
            .distinct('year_ceremony')
            .sort();
            
        if (years.length === 0) {
            return res.status(404).json({ 
                success: false, 
                message: "Nessun anno trovato" 
            });
        }
        
        // Sort years in descending order (newest first)
        const sortedYears = years.sort((a, b) => b - a);
        
        console.log(`Found ${sortedYears.length} ceremony years`);
        res.json({
            success: true,
            count: sortedYears.length,
            data: sortedYears,
        });
    } catch (error) {
        console.error('Error fetching Oscar years:', error);
        res.status(500).json({
            success: false,
            message: "Errore nel recupero degli anni delle cerimonie",
            error: error.message,
        });
    }
};

//@returns reviews for a specific movie by title
exports.getReviewsByMovieTitle = async (req, res) => {
    try {
        const { movieTitle, page = 1, limit = 20, reviewType, topCritic } = req.query;
        
        if (!movieTitle || movieTitle.trim() === '') {
            return res.status(400).json({
                success: false,
                message: "Movie title is required"
            });
        }
        
        const trimmedTitle = movieTitle.trim();
        const pageNum = Math.max(1, parseInt(page) || 1);
        const limitNum = Math.min(100, Math.max(1, parseInt(limit) || 20));
        const skip = (pageNum - 1) * limitNum;
        
        console.log(`[REVIEWS] Searching for: "${trimmedTitle}", page: ${pageNum}, limit: ${limitNum}`);
        
        // Build filter with optional criteria
        const filter = {
            movie_title: new RegExp(trimmedTitle, 'i')
        };
        
        // Add review type filter if specified
        if (reviewType && ['Fresh', 'Rotten'].includes(reviewType)) {
            filter.review_type = reviewType;
        }
        
        // Add top critic filter if specified
        if (topCritic !== undefined) {
            filter.top_critic = topCritic === 'true';
        }
        
        // Get total count for pagination
        const totalCount = await modelRottenTomatoes.countDocuments(filter);
        
        // Use case-insensitive regex for better matching
        const reviews = await modelRottenTomatoes
            .find(filter)
            .sort({ review_date: -1 }) // Sort newest to oldest (more relevant)
            .skip(skip)
            .limit(limitNum);
            
        console.log(`[REVIEWS] Found ${reviews.length} reviews for: "${trimmedTitle}"`);
        
        if (reviews.length === 0) {
            return res.status(404).json({
                success: false,
                message: `No reviews found for movie: ${trimmedTitle}`,
                searchedTitle: trimmedTitle
            });
        }
        
        res.json({
            success: true,
            count: reviews.length,
            totalCount: totalCount,
            page: pageNum,
            totalPages: Math.ceil(totalCount / limitNum),
            hasNextPage: pageNum < Math.ceil(totalCount / limitNum),
            hasPrevPage: pageNum > 1,
            movieTitle: trimmedTitle,
            searchedTitle: trimmedTitle,
            filters: {
                reviewType: reviewType || null,
                topCritic: topCritic || null
            },
            data: reviews
        });
    } catch (error) {
        console.error('[REVIEWS] Error fetching movie reviews:', error);
        res.status(500).json({
            success: false,
            message: "Error fetching movie reviews",
            error: error.message
        });
    }
};
