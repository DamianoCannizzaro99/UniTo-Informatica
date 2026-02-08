/**
 * index_Reviews.js - Handles all review-related functionality
 * Manages loading, displaying, and pagination of movie reviews
 */

// Reviews management variables
let allReviews = [];
let displayedReviews = 0;
const REVIEWS_PER_CHUNK = 10;
let isLoadingReviews = false;

/**
 * Initialize reviews for a specific movie
 * @param {string} movieId - The movie ID to load reviews for
 */
async function initializeReviews(movieId) {
    if (!movieId) {
        console.warn('[REVIEWS] No movie ID provided');
        return;
    }
    
    console.log(`[REVIEWS] Initializing reviews for movie ID: ${movieId}`);
    
    // Wait a bit to ensure DOM is ready
    setTimeout(() => {
        loadReviewsForMovie(movieId);
    }, 500);
}

/**
 * Load reviews for a specific movie
 * @param {string} movieId - The movie ID
 */
async function loadReviewsForMovie(movieId) {
    if (isLoadingReviews) return;
    
    isLoadingReviews = true;
    showReviewsLoadingState();
    
    try {
        // First get movie title and year
        const movieResponse = await axios.get(`http://localhost:8081/movies/id/${movieId}`);
        const movieTitle = movieResponse.data.title;
        const movieYear = movieResponse.data.relaseYear; // Note: typo in API "relaseYear"
        console.log(`[REVIEWS] Fetching reviews for: "${movieTitle}" (${movieYear})`);
        
        // Get reviews with exact title match
        const reviewsResponse = await axios.get(`http://localhost:3001/api/movieReviews?movieTitle=${encodeURIComponent(movieTitle)}`);
        let reviews = reviewsResponse.data.data || [];
        
        console.log(`[REVIEWS] Loaded ${reviews.length} total reviews`);
        
        // Filter reviews by movie release year on client side
        if (movieYear && reviews.length > 0) {
            const yearStartDate = new Date(movieYear, 0, 1); // January 1st of release year
            reviews = reviews.filter(review => {
                if (review.review_date) {
                    const reviewDate = new Date(review.review_date);
                    return reviewDate >= yearStartDate;
                }
                return true; // Keep reviews without dates
            });
            console.log(`[REVIEWS] After year filter (>= ${movieYear}): ${reviews.length} reviews`);
        }
        
        // Store the filtered reviews
        allReviews = reviews;
        
        // Update UI
        updateReviewsCountBadge();
        displayedReviews = 0;
        clearReviewsContent();
        displayNextReviewsChunk();
        
    } catch (error) {
        console.warn('[REVIEWS] Could not load reviews:', error);
        // Check if it's a 404 error (no reviews found)
        if (error.response && error.response.status === 404) {
            showNoReviewsFoundMessage();
        } else {
            showReviewsErrorMessage();
        }
    } finally {
        isLoadingReviews = false;
    }
}

/**
 * Display the next chunk of reviews
 */
function displayNextReviewsChunk() {
    const reviewsContent = document.getElementById('reviewsContent');
    const loadMoreContainer = document.getElementById('loadMoreContainer');
    const noMoreReviews = document.getElementById('noMoreReviews');
    
    // Check if elements exist before using them
    if (!reviewsContent) {
        console.warn('[REVIEWS] reviewsContent element not found');
        return;
    }
    
    if (allReviews.length === 0) {
        showNoReviewsFoundMessage();
        return;
    }
    
    // Get next chunk of reviews
    const nextChunk = allReviews.slice(displayedReviews, displayedReviews + REVIEWS_PER_CHUNK);
    
    // Render each review
    nextChunk.forEach(review => {
        const reviewCard = createReviewCardElement(review);
        if (reviewCard) {
            reviewsContent.appendChild(reviewCard);
        }
    });
    
    displayedReviews += nextChunk.length;
    
    // Show/hide load more button
    if (loadMoreContainer && noMoreReviews) {
        if (displayedReviews < allReviews.length) {
            loadMoreContainer.style.display = 'block';
            noMoreReviews.style.display = 'none';
        } else {
            loadMoreContainer.style.display = 'none';
            noMoreReviews.style.display = allReviews.length > REVIEWS_PER_CHUNK ? 'block' : 'none';
        }
    }
}

/**
 * Create a review card DOM element
 * @param {Object} review - Review data object
 * @returns {HTMLElement|null} - Review card element or null if template not found
 */
function createReviewCardElement(review) {
    const template = document.getElementById('reviewCardTemplate');
    if (!template) {
        console.warn('[REVIEWS] Review card template not found, using fallback');
        return createFallbackReviewCard(review);
    }
    
    // Prepare review data
    const criticName = review.critic_name || 'Anonymous Critic';
    const publisherName = review.publisher_name || '';
    const reviewDate = review.review_date ? formatReviewDate(new Date(review.review_date)) : 'Unknown date';
    const reviewContent = review.review_content || 'No review content available.';
    const reviewType = review.review_type || 'N/A';
    const isTopCritic = review.top_critic;
    const isFresh = reviewType === 'Fresh';
    
    // Replace template placeholders
    let cardHtml = template.innerHTML
        .replace('{THUMB_CLASS}', isFresh ? 'fa-thumbs-up' : 'fa-thumbs-down')
        .replace('{THUMB_COLOR}', isFresh ? 'text-success' : 'text-danger')
        .replace('{CRITIC_NAME}', escapeHtmlContent(criticName))
        .replace('{TOP_CRITIC_BADGE}', isTopCritic ? '<span class="badge bg-warning ms-2">Top Critic</span>' : '')
        .replace('{MOVIE_TITLE}', escapeHtmlContent(review.movie_title || 'Unknown Movie'))
        .replace('{PUBLISHER_INFO}', publisherName ? `<i class="fas fa-newspaper me-1"></i>${escapeHtmlContent(publisherName)}` : '')
        .replace('{DATE_INFO}', publisherName && reviewDate !== 'Unknown date' ? ` • <i class="fas fa-calendar me-1"></i>${reviewDate}` : reviewDate !== 'Unknown date' ? `<i class="fas fa-calendar me-1"></i>${reviewDate}` : '')
        .replace('{BADGE_CLASS}', isFresh ? 'bg-success' : 'bg-danger')
        .replace('{REVIEW_TYPE}', escapeHtmlContent(reviewType))
        .replace('{REVIEW_CONTENT}', reviewContent === 'No review content available.' ? '<em class="text-muted">No review content available.</em>' : escapeHtmlContent(reviewContent));
    
    // Create DOM element
    const div = document.createElement('div');
    div.innerHTML = cardHtml;
    return div.firstElementChild;
}

/**
 * Create a fallback review card if template is not available
 * @param {Object} review - Review data object
 * @returns {HTMLElement} - Fallback review card element
 */
function createFallbackReviewCard(review) {
    const card = document.createElement('div');
    card.className = 'card mb-3 shadow-sm';
    
    const criticName = review.critic_name || 'Anonymous Critic';
    const publisherName = review.publisher_name || '';
    const reviewDate = review.review_date ? formatReviewDate(new Date(review.review_date)) : 'Unknown date';
    const reviewContent = review.review_content || 'No review content available.';
    const reviewType = review.review_type || 'N/A';
    const isTopCritic = review.top_critic;
    const isFresh = reviewType === 'Fresh';
    
    card.innerHTML = `
        <div class="card-body">
            <div class="d-flex align-items-start">
                <div class="review-icon me-3">
                    <i class="fas ${isFresh ? 'fa-thumbs-up text-success' : 'fa-thumbs-down text-danger'}" style="font-size: 1.5em;"></i>
                </div>
                <div class="flex-grow-1">
                    <div class="d-flex justify-content-between align-items-start mb-3">
                        <div>
                            <h5 class="mb-1 text-primary">
                                ${escapeHtmlContent(criticName)}
                                ${isTopCritic ? '<span class="badge bg-warning ms-2">Top Critic</span>' : ''}
                            </h5>
                            <div class="text-muted mb-2">
                                <strong class="text-info">Movie:</strong> ${escapeHtmlContent(review.movie_title || 'Unknown Movie')}<br>
                                ${publisherName ? `<i class="fas fa-newspaper me-1"></i>${escapeHtmlContent(publisherName)}` : ''}
                                ${publisherName && reviewDate !== 'Unknown date' ? ' • ' : ''}
                                ${reviewDate !== 'Unknown date' ? `<i class="fas fa-calendar me-1"></i>${reviewDate}` : ''}
                            </div>
                        </div>
                        <span class="badge ${isFresh ? 'bg-success' : 'bg-danger'} fs-6">
                            ${escapeHtmlContent(reviewType)}
                        </span>
                    </div>
                    <div class="review-content">
                        <p class="mb-0 text-dark lh-base">
                            ${reviewContent === 'No review content available.' ? '<em class="text-muted">No review content available.</em>' : escapeHtmlContent(reviewContent)}
                        </p>
                    </div>
                </div>
            </div>
        </div>
    `;
    return card;
}

/**
 * Setup event listeners for reviews
 */
function setupReviewsEventListeners() {
    // Use event delegation for load more button
    document.addEventListener('click', (e) => {
        if (e.target && e.target.id === 'loadMoreBtn') {
            e.preventDefault();
            displayNextReviewsChunk();
        }
    });
}

/**
 * Update the reviews count badge
 */
function updateReviewsCountBadge() {
    const badge = document.getElementById('reviewsCountBadge');
    if (badge) {
        badge.textContent = allReviews.length;
        badge.className = allReviews.length > 0 ? 'badge bg-success ms-auto' : 'badge bg-secondary ms-auto';
    }
}

/**
 * Clear the reviews container
 */
function clearReviewsContent() {
    const reviewsContent = document.getElementById('reviewsContent');
    if (reviewsContent) {
        reviewsContent.innerHTML = '';
    }
}

/**
 * Show loading state for reviews
 */
function showReviewsLoadingState() {
    const reviewsContent = document.getElementById('reviewsContent');
    if (reviewsContent) {
        reviewsContent.innerHTML = `
            <div class="text-center py-4">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Loading reviews...</span>
                </div>
                <p class="mt-2 text-muted">Loading reviews...</p>
            </div>
        `;
    }
}

/**
 * Show no reviews found message
 */
function showNoReviewsFoundMessage() {
    const reviewsContent = document.getElementById('reviewsContent');
    const loadMoreContainer = document.getElementById('loadMoreContainer');
    const noMoreReviews = document.getElementById('noMoreReviews');
    
    if (reviewsContent) {
        reviewsContent.innerHTML = `
            <div class="alert alert-info">
                <i class="fas fa-info-circle me-2"></i>
                Nessuna recensione disponibile per questo film.
            </div>
        `;
    }
    
    if (loadMoreContainer) loadMoreContainer.style.display = 'none';
    if (noMoreReviews) noMoreReviews.style.display = 'none';
}

/**
 * Show error message for reviews
 */
function showReviewsErrorMessage() {
    const reviewsContent = document.getElementById('reviewsContent');
    if (reviewsContent) {
        reviewsContent.innerHTML = `
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-triangle me-2"></i>
                Error loading reviews. Please try again later.
            </div>
        `;
    }
    
    // Update count badge to show 0
    const badge = document.getElementById('reviewsCountBadge');
    if (badge) {
        badge.textContent = '0';
        badge.className = 'badge bg-secondary ms-auto';
    }
}

/**
 * Format a date object to a readable string
 * @param {Date} date - Date object
 * @returns {string} - Formatted date string
 */
function formatReviewDate(date) {
    if (!date || isNaN(date.getTime())) {
        return 'Unknown date';
    }
    
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

/**
 * Escape HTML characters to prevent XSS
 * @param {string} text - Text to escape
 * @returns {string} - Escaped text
 */
function escapeHtmlContent(text) {
    if (!text) return '';
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.toString().replace(/[&<>"']/g, function(m) { return map[m]; });
}

/**
 * Get the current number of displayed reviews
 * @returns {number} - Number of displayed reviews
 */
function getDisplayedReviewsCount() {
    return displayedReviews;
}

/**
 * Get the total number of reviews
 * @returns {number} - Total number of reviews
 */
function getTotalReviewsCount() {
    return allReviews.length;
}

/**
 * Check if there are more reviews to load
 * @returns {boolean} - True if there are more reviews
 */
function hasMoreReviewsToLoad() {
    return displayedReviews < allReviews.length;
}
