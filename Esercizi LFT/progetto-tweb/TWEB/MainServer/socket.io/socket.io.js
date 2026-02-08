const axios = require('axios');

module.exports = (io) => {
    io.on("connection", (socket) => {
        console.log("Un utente si è connesso");
        
        // Join a specific room
        socket.on("join room", (roomData) => {
            const { room, movieId } = roomData;
            socket.join(room);
            console.log(`User joined room: ${room}`);
            
            // Send existing messages when user joins
            if (movieId) {
                // Fetch movie-specific messages
                axios.get(`http://localhost:3001/api/chat/movie/${movieId}`)
                    .then(response => {
                        if (response.data.success && response.data.messages) {
                            socket.emit("load messages", response.data.messages);
                        }
                    })
                    .catch(err => console.error('Error loading messages:', err));
            }
        });

        socket.on("chat message", async (data) => {
            console.log(`Messaggio ricevuto: ${data.message} da ${data.username}`);
            
            // Validate message
            if (!data.message || !data.username || data.message.length > 500) {
                return;
            }
            
            // Generate avatar seed if not provided
            if (!data.avatarSeed) {
                data.avatarSeed = data.username + Date.now();
            }
            
            // Add timestamp
            data.timestamp = new Date();
            
            // Save to MongoDB
            try {
                const messageToSave = {
                    username: data.username,
                    message: data.message,
                    room: data.room || 'general',
                    movieId: data.movieId || null,
                    avatarSeed: data.avatarSeed,
                    timestamp: data.timestamp
                };
                
                // Save to database via API
                const response = await axios.post('http://localhost:3001/api/chat/message', messageToSave);
                console.log('Message saved to database:', response.data);
                
                // Emit to room or all
                if (data.room) {
                    io.to(data.room).emit("chat message", data);
                } else {
                    io.emit("chat message", data);
                }
            } catch (error) {
                console.error('Error saving message:', error.message);
                // Still emit the message even if save fails
                if (data.room) {
                    io.to(data.room).emit("chat message", data);
                } else {
                    io.emit("chat message", data);
                }
            }
        });

        socket.on("disconnect", () => {
            console.log("Un utente si è disconnesso");
        });
    });
};
