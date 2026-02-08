const http = require('http');
var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
const axios = require('axios');
var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
const cors = require('cors');
const swaggerUi = require('swagger-ui-express');
const fs = require('fs');
const setupSwagger = require('./routes/swaggerConfig');
var app = express();

app.use(cors({ origin: 'http://localhost:3000' }));
// view engine setup
app.set('views', path.join(__dirname, 'views'));

setupSwagger(app);

// Register Handlebars as view engine
const {engine} = require('express-handlebars');
app.engine('hbs', engine({
  extname: '.hbs',
  defaultLayout: 'layout',
  layoutsDir: path.join(__dirname, 'views/Layouts'),
  partialsDir: path.join(__dirname, 'views/Partials'),
  helpers: {
    // Helper to build query string for pagination
    buildQueryString: function(searchParams, pageNumber) {
      const params = new URLSearchParams();
      
      // Add all search parameters except page
      for (const [key, value] of Object.entries(searchParams)) {
        if (key !== 'page') {
          params.append(key, value);
        }
      }
      
      // Add the new page number
      params.append('page', pageNumber);
      
      return params.toString();
    },
    // Helper for string comparison
    gt: function(a, b) {
      return a > b;
    },
    // Helper for equality comparison
    eq: function(a, b) {
      return a === b;
    },
    // Helper for substring
    substring: function(str, start, end) {
      return str ? str.substring(start, end) : '';
    },
    // Helper for date formatting
    formatDate: function(dateString) {
      if (!dateString) return 'Unknown date';
      try {
        return new Date(dateString).toLocaleDateString();
      } catch (error) {
        return 'Invalid date';
      }
    },
    // Helper for generating star ratings (same logic as index_HomePage.js)
    generateStars: function(rating) {
      if (!rating || rating === 0) return '';
      
      let stars = '';
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
  }
}));
app.set('view engine', 'hbs');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;


