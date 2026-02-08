const mongoose = require('mongoose');

const model = new mongoose.Schema({
    _id: {
        type: mongoose.Schema.Types.ObjectId,
        required: true
    },
    year_film: {
        type: Number
    },
    year_ceremony: {
        type: Number
    },
    ceremony: {
        type: Number
    },
    category: {
        type: String
    },
    name: {
        type: String
    },
    film: {
        type: String
    },
    winner: {
        type: Boolean
    }
},{
    collection:'theOscarAwards'
});

module.exports = mongoose.model('theOscarAwards', model);