const axios = require('axios');

// TMDB API configuration
const TMDB_API_KEY = process.env.TMDB_API_KEY || '56fc6ee23ea5fc95e3d5f00fb5b3b259';
const TMDB_BASE_URL = 'https://api.themoviedb.org/3';
const TMDB_IMAGE_BASE_URL = 'https://image.tmdb.org/t/p';

/**
 * Get actor profile image URL from TMDB
 * @param {string} actorName - The name of the actor
 * @returns {Promise<string|null>} - Image URL or null if not found
 */
async function getActorImageUrl(actorName) {
    try {
        // Search for the actor
        const searchResponse = await axios.get(`${TMDB_BASE_URL}/search/person`, {
            params: {
                api_key: TMDB_API_KEY,
                query: actorName
            }
        });

        if (searchResponse.data.results.length === 0) {
            return null;
        }

        // Get the first result (most relevant)
        const actor = searchResponse.data.results[0];
        
        // Return the image URL if available
        if (actor.profile_path) {
            return `${TMDB_IMAGE_BASE_URL}/w500${actor.profile_path}`; // w500 = 500px width for higher quality
        }

        return null;
    } catch (error) {
        console.error('Error fetching actor image:', error.message);
        return null;
    }
}

module.exports = {
    getActorImageUrl
};
