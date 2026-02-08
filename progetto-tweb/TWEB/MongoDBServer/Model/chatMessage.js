const mongoose = require('mongoose');

const chatMessageSchema = new mongoose.Schema({
    username: {
        type: String,
        required: true,
        trim: true
    },
    message: {
        type: String,
        required: true,
        trim: true,
        maxLength: 500
    },
    room: {
        type: String,
        required: true,
        default: 'general'
    },
    movieId: {
        type: String,
        default: null
    },
    timestamp: {
        type: Date,
        default: Date.now
    },
    avatarSeed: {
        type: String,
        required: true
    }
}, {
    collection: 'chatMessages',
    timestamps: true  // This will automatically add createdAt and updatedAt
});

// Index for efficient queries
chatMessageSchema.index({ room: 1, timestamp: -1 });
chatMessageSchema.index({ movieId: 1, timestamp: -1 });

module.exports = mongoose.model('ChatMessage', chatMessageSchema);
