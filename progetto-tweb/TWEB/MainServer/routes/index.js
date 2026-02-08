var express = require('express');
var router = express.Router();
const axios = require('axios');
const PaginationUtils = require('../Utils/paginationUtils');
const { getActorImageUrl } = require('../services/actorImageService');
/* GET home page. */
router.get('/', async function (req, res, next) {
  try {
    // Validate pagination parameters
    const { currentPage, pageSize } = PaginationUtils.validateParams(req.query.page, 12);
    const apiPage = PaginationUtils.toApiPage(currentPage);
    
    // Fetch top-rated movies with pagination
    const topRatedResponse = await axios.get(`http://localhost:8081/movies/TopRate?page=${apiPage}&size=${pageSize}`);
    const topRatedMovies = topRatedResponse.data;
    
    // Create pagination info
    const pagination = PaginationUtils.createPaginationInfo(currentPage, pageSize, topRatedMovies);
    
    // Add page range for better navigation
    const pageRange = PaginationUtils.calculatePageRange(currentPage, null, pageSize);
    pagination.pageRange = pageRange;
    
    res.render('Pages/HomePage.hbs', {
      title: 'CineMagazine', 
      showSearch: true,
      topRatedMovies: topRatedMovies,
      pagination: pagination,
      searchParams: req.query
    });
    
  } catch (error) {
    console.error("Error loading homepage data:", error);
    res.render('Pages/HomePage.hbs', {
      title: 'CineMagazine', 
      showSearch: true,
      topRatedMovies: [],
      pagination: null,
      error: "Error loading movies. Please try again later."
    });
  }
});

/* GET film. */
router.get('/Films', function(req, res, next) {
  res.render('Pages/Films.hbs', { title: 'Films', showSearch: true });
});

router.get('/Oscars', function(req, res, next) {
  res.render('Pages/Oscars.hbs', { title: 'Oscars', showSearch: false });
});

router.get('/Login', function(req, res, next) {
  res.render('Pages/Login.hbs', { title: 'Login', showSearch: false });
});

router.get('/FilmScheda', async function(req, res, next) {
  try {
    const movieId = req.query.id;
    
    // Reviews loaded client-side via JavaScript
    res.render('Pages/FilmScheda.hbs', { 
      title: 'Film', 
      showSearch: false,
      reviews: [], // Reviews loaded via JavaScript
      reviewsCount: 0 // Updated via JavaScript
    });
  } catch (error) {
    console.error('Error loading film page:', error);
    res.render('Pages/FilmScheda.hbs', { 
      title: 'Film', 
      showSearch: false,
      reviews: [],
      reviewsCount: 0
    });
  }
})

router.get('/FilmSearched', async function(req, res, next) {
  try {
    // Validate pagination parameters
    const { currentPage, pageSize } = PaginationUtils.validateParams(req.query.page, 20);
    const apiPage = PaginationUtils.toApiPage(currentPage);
    
    // Build query string with pagination
    let queryParams = Object.entries(req.query)
        .filter(([key]) => !['prioritizeGenres'].includes(key)) // Remove custom parameters
        .map(([key, value]) => `${key}=${encodeURIComponent(value)}`);
    
    // Add pagination parameters
    queryParams.push(`page=${apiPage}`);
    queryParams.push(`size=${pageSize}`);
    
    const queryString = queryParams.join("&");
        
    let response = await axios.get(`http://localhost:8081/movies/search?${queryString}`);
    let movies = response.data;
    
    // Create filter summary for display
    const filterSummary = createFilterSummary(req.query);
    
    // Create pagination info using utility
    const pagination = PaginationUtils.createPaginationInfo(currentPage, pageSize, movies);
    
    // Add page range for better navigation
    const pageRange = PaginationUtils.calculatePageRange(currentPage, null, pageSize);
    pagination.pageRange = pageRange;
    
    res.render('Pages/FilmSearched', {
      title: 'Results',
      showSearch: false,
      movies: movies,
      filterSummary: filterSummary,
      pagination: pagination,
      searchParams: req.query // Pass original search params for pagination links
    });

  } catch (error) {
    // Verifica che la risposta non sia già stata inviata prima di inviarne un'altra
    if (!res.headersSent) {
      console.error("Errore nel caricamento dei film:", error);
      // In caso di errore, passa una lista vuota di film e un messaggio di errore
      res.render('Pages/FilmSearched', {
        title: 'Results',
        showSearch: false,
        movies: [],
        filterSummary: null,
        pagination: null,
        error: "Errore nel caricamento dei film."
      });
    }
  }
});

// Function to create a readable filter summary
function createFilterSummary(queryParams) {
  const filters = [];
  
  if (queryParams.title) {
    filters.push(`Title: "${queryParams.title}"`);
  }
  
  if (queryParams.genres) {
    const genreList = queryParams.genres.split(',').map(g => g.trim()).join(', ');
    filters.push(`Genres: ${genreList}`);
  }
  
  if (queryParams.duration) {
    filters.push(`Min Duration: ${queryParams.duration} minutes`);
  }
  
  if (queryParams.rating) {
    filters.push(`Min Rating: ${queryParams.rating}`);
  }
  
  if (queryParams.year) {
    filters.push(`Year: ${queryParams.year}`);
  }
  
  return filters.length > 0 ? filters : null;
}

router.get('/Actors',  async function(req, res, next) {
  const actorName = req.query.name;

  // Verifica che il parametro actorName sia presente
  if (!actorName) {
    return res.render('Pages/Actors', {
      title: 'Results',
      showSearch: false,
      movies: [],
      error: "Nessun attore specificato."
    });
  }

  try {
    // Validate pagination parameters
    const { currentPage, pageSize } = PaginationUtils.validateParams(req.query.page, 20);
    const apiPage = PaginationUtils.toApiPage(currentPage);
    
    // Fai la richiesta al server esterno utilizzando 'actorName' come parametro con paginazione
    let response = await axios.get(`http://localhost:8081/movies/name?actorName=${encodeURIComponent(actorName)}&page=${apiPage}&size=${pageSize}`);

    // Estrai i film dalla risposta
    let movies = response.data;
    
    // Create pagination info
    const pagination = PaginationUtils.createPaginationInfo(currentPage, pageSize, movies);
    
    // Add page range for better navigation
    const pageRange = PaginationUtils.calculatePageRange(currentPage, null, pageSize);
    pagination.pageRange = pageRange;

    // Renderizza la pagina con i film trovati
    res.render('Pages/Actors', {
      title: 'Results',
      showSearch: false,
      movies: movies,
      actorName: actorName,
      pagination: pagination,
      searchParams: req.query // Pass original search params for pagination links
    });

  } catch (error) {
    // Gestione degli errori
    console.error("Errore nel caricamento dei film:", error);

    res.render('Pages/Actors', {
      title: 'Results',
      showSearch: false,
      movies: [],
      pagination: null,
      error: "Errore nel caricamento dei film."
    });
  }
})


router.get('/oscarsSearched', (req, res) => {
  res.render('Pages/OscarsSearched.hbs', { film: req.query.film });
});

// Work in Progress page route
router.get('/work-in-progress', (req, res) => {
  res.render('Pages/WorkInProgress.hbs', { 
    title: 'Work in Progress', 
    showSearch: false 
  });
});

// API endpoint to get actor image
router.get('/api/actor-image/:actorName', async (req, res) => {
  try {
    const actorName = req.params.actorName;
    const imageUrl = await getActorImageUrl(actorName);
    
    res.json({
      success: true,
      actorName: actorName,
      imageUrl: imageUrl
    });
  } catch (error) {
    console.error('Error in actor image endpoint:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch actor image'
    });
  }
});


module.exports = router;
