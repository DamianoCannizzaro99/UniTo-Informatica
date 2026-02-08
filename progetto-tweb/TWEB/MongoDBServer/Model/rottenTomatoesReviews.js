const mongoose = require('mongoose');

const model = new mongoose.Schema({
    _id: {
        type: mongoose.Schema.Types.ObjectId,
        required: true
    },
    rotten_tomatoes_link: {
        type: String
    },
    movie_title: {
        type: String
    },
    critic_name: {
        type: String
    },
    top_critic: {
        type: Boolean
    },
    publisher_name: {
        type: String
    },
    review_type: {
        type: String,
        enum: ['Fresh', 'Rotten']
    },
    review_date: {
        type: Date
    },
    review_content: {
        type: String

    }
},
    {
        collection:'rottenTomatoesReviews'
    });

module.exports = mongoose.model('rottenTomatoesReviews',model);
