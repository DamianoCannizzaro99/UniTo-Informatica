const User = require('../Model/user');

// Get or create user with avatarSeed
exports.getOrCreateUser = async (req, res) => {
    try {
        const { username } = req.body;
        
        if (!username || username === 'Anonimo') {
            return res.status(400).json({
                success: false,
                message: 'Valid username required'
            });
        }
        
        // Try to find existing user
        let user = await User.findOne({ username });
        
        if (!user) {
            // Create new user with auto-generated avatarSeed
            user = new User({ username });
            await user.save();
            console.log(`Created new user: ${username} with avatarSeed: ${user.avatarSeed}`);
        }
        
        res.json({
            success: true,
            user: {
                username: user.username,
                avatarSeed: user.avatarSeed
            }
        });
    } catch (error) {
        console.error('Error in getOrCreateUser:', error);
        res.status(500).json({
            success: false,
            message: 'Error processing user',
            error: error.message
        });
    }
};
