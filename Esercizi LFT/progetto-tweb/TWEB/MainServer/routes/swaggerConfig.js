const swaggerUi = require('swagger-ui-express');
const swaggerJPA = require('../Swagger Documentation/swaggerServer.json');

module.exports = (app) => {
    app.use('/api-docs/jpa', swaggerUi.serve, swaggerUi.setup(swaggerJPA));
};
