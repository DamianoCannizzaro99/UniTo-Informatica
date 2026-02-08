const mongoose = require('mongoose');

/*Stringa di connessione per il database*/
const connectionString = process.env.MONGODB_URI || 'mongodb://localhost:27017/CineMagazine';

// Mongoose global promise is deprecated, using native Promise
mongoose.Promise = global.Promise;

// Connection configuration
const connectDB = async () => {
    try {
        const connection = await mongoose.connect(connectionString, {
           
            serverSelectionTimeoutMS: 5000, // Keep trying to send operations for 5 seconds
            socketTimeoutMS: 45000, // Close sockets after 45 seconds of inactivity
            family: 4 // Use IPv4, skip trying IPv6
        });
        console.log(`Connected to Database: ${connection.connection.host}`);
        return connection;
    } catch (error) {
        console.error('Database connection failed:', error.message);
        console.error('Full error details:', error);
        process.exit(1);
    }
};

// Initialize connection
connectDB();

// Export the connection function and mongoose instance
module.exports = { connectDB, mongoose };

