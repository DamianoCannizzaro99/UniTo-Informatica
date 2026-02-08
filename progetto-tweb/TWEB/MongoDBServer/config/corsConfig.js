const corsOptions = {
    origin: ['http://localhost:3000', 'http://localhost:3001'],  // Permetti richieste da frontend (3000) e stesso server (3001)
    methods: 'GET,POST',              // Metodi HTTP consentiti
    allowedHeaders: 'Content-Type',   // Headers consentiti
    credentials: true                 // Allow credentials
};

module.exports = corsOptions;