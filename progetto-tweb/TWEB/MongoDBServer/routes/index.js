var express = require('express');
var router = express.Router();
var controllerData = require('../Controllers/controllersData');
var chatController = require('../Controllers/chatController');
var userController = require('../Controllers/userController');

/* Post home page. */
router.get('/api/dataRotten',controllerData.getAllDataRottenTomatoes);
router.get('/api/dataOscar',controllerData.getAllDataTheOscarAwards);
router.get('/api/20dataOscar',controllerData.get100OscarAwards);
router.get('/api/findOscar',controllerData.getOscars);
router.get('/api/oscarYears',controllerData.getOscarYears);
router.get('/api/movieReviews',controllerData.getReviewsByMovieTitle);

// User routes
router.post('/api/user/getOrCreate', userController.getOrCreateUser);

// Chat routes
router.get('/api/chat/messages',chatController.getMessagesByRoom);
router.get('/api/chat/movie/:movieId',chatController.getMessagesByMovie);
router.post('/api/chat/message', async (req, res) => {
    try {
        const savedMessage = await chatController.saveMessage(req.body);
        res.json({ success: true, message: savedMessage });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
});

module.exports = router;
