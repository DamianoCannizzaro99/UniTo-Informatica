const ChatMessage = require('../Model/chatMessage');

// Get chat messages for a specific room
exports.getMessagesByRoom = async (req, res) => {
    try {
        const { room = 'general', limit = 50 } = req.query;
        
        const messages = await ChatMessage
            .find({ room })
            .sort({ timestamp: -1 })
            .limit(parseInt(limit));
        
        // Reverse to get chronological order
        messages.reverse();
        
        res.json({
            success: true,
            room: room,
            count: messages.length,
            messages: messages
        });
    } catch (error) {
        console.error('Error fetching chat messages:', error);
        res.status(500).json({
            success: false,
            message: 'Error fetching chat messages',
            error: error.message
        });
    }
};

// Get chat messages for a specific movie
exports.getMessagesByMovie = async (req, res) => {
    try {
        const { movieId } = req.params;
        const { limit = 50 } = req.query;
        
        if (!movieId) {
            return res.status(400).json({
                success: false,
                message: 'Movie ID is required'
            });
        }
        
        const messages = await ChatMessage
            .find({ movieId })
            .sort({ timestamp: -1 })
            .limit(parseInt(limit));
        
        // Reverse to get chronological order
        messages.reverse();
        
        res.json({
            success: true,
            movieId: movieId,
            count: messages.length,
            messages: messages
        });
    } catch (error) {
        console.error('Error fetching movie chat messages:', error);
        res.status(500).json({
            success: false,
            message: 'Error fetching movie chat messages',
            error: error.message
        });
    }
};

// Save a new chat message (this will be called from socket.io)
exports.saveMessage = async (messageData) => {
    try {
        const message = new ChatMessage(messageData);
        await message.save();
        return message;
    } catch (error) {
        console.error('Error saving chat message:', error);
        throw error;
    }
};
