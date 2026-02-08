const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
    username: {
        type: String,
        required: true,
        unique: true,
        trim: true
    },
    avatarSeed: {
        type: String,
        unique: true
    }
}, {
    collection: 'users'
});

// Auto-generate avatar seed before saving
userSchema.pre('save', function(next) {
    if (!this.avatarSeed) {
        const randomPart = Math.random().toString(36).substring(2, 10);
        const timestamp = Date.now();
        this.avatarSeed = `${this.username}_${timestamp}_${randomPart}`;
    }
    next();
});

module.exports = mongoose.model('User', userSchema);
