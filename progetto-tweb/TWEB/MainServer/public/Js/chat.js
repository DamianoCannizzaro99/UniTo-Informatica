console.log('[CHAT] Initializing chat system...');
const socket = io();

socket.on('connect', () => {
    console.log('[CHAT] Connected to socket.io server');
});

socket.on('connect_error', (error) => {
    console.error('[CHAT] Socket connection error:', error);
});

// Function to initialize or reinitialize chat
function initializeChat() {
    console.log('[CHAT] DOM Content Loaded');

    // Get HTML elements
    const chatMessages = document.getElementById("chatMessages");
    const messageInput = document.getElementById("messageInput");
    const sendMessageBtn = document.getElementById("sendMessageBtn");
    const chatUsername = document.getElementById("chatUsername");

    console.log('[CHAT] Elements found:', {
        chatMessages: !!chatMessages,
        messageInput: !!messageInput,
        sendMessageBtn: !!sendMessageBtn,
        chatUsername: !!chatUsername
    });

    if (!chatMessages || !messageInput || !sendMessageBtn) {
        console.error('Chat elements not found in DOM');
        return;
    }

    // Retrieve username from localStorage if exists
    let username = localStorage.getItem("username") || null;
    const isLoggedIn = username && username !== "Anonimo";
    let avatarSeed = null;

    // Update UI based on login status
    if (isLoggedIn) {
        chatUsername.textContent = username;
        // Enable input for logged in users
        messageInput.disabled = false;
        sendMessageBtn.disabled = false;
        messageInput.placeholder = "Scrivi un messaggio...";

        // Fetch avatarSeed from MongoDB
        fetch('http://localhost:3001/api/user/getOrCreate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username: username })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success && data.user) {
                avatarSeed = data.user.avatarSeed;
                console.log('[CHAT] Got avatarSeed from MongoDB:', avatarSeed);

                // Update user's avatar with the one from database
                const userAvatarImg = document.getElementById('userAvatar');
                if (userAvatarImg) {
                    userAvatarImg.src = `https://api.dicebear.com/7.x/avataaars/svg?seed=${avatarSeed}`;
                }
            }
        })
        .catch(error => {
            console.error('[CHAT] Error fetching user data:', error);
        });
    } else {
        chatUsername.textContent = "Accedi per commentare";
        // Disable input for anonymous users
        messageInput.disabled = true;
        sendMessageBtn.disabled = true;
        messageInput.placeholder = "Devi effettuare l'accesso per inviare messaggi";

        // Use default avatar for anonymous users
        const userAvatarImg = document.getElementById('userAvatar');
        if (userAvatarImg) {
            userAvatarImg.src = "/img/account.png";
        }
    }

    // Get movie ID from URL if on movie page
    function getMovieIdFromUrl() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get('id');
    }

    // Determine current room
    const movieId = getMovieIdFromUrl();
    const currentRoom = movieId ? `movie_${movieId}` : 'general';

    // Join the appropriate room
    socket.emit('join room', { room: currentRoom, movieId: movieId });

    // Button click and Enter key to send message
    sendMessageBtn.addEventListener("click", sendMessage);
    messageInput.addEventListener("keypress", (event) => {
        if (event.key === "Enter" && !event.shiftKey) {
            event.preventDefault();
            sendMessage();
        }
    });

    // Function to send a message
    function sendMessage() {
        const message = messageInput.value.trim();
        if (message) {
            socket.emit("chat message", {
                username,
                message,
                room: currentRoom,
                movieId: movieId,
                avatarSeed: avatarSeed
            });
            messageInput.value = ""; // Clear input field
        }
    }

    // Function to escape HTML
    function escapeHtml(text) {
        const map = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#039;'
        };
        return text.toString().replace(/[&<>"']/g, (m) => map[m]);
    }

    // Function to render a message
    function renderMessage(data) {
        // Generate avatar URL
        const avatarUrl = `https://api.dicebear.com/7.x/avataaars/svg?seed=${data.avatarSeed || data.username}`;

        // Format timestamp
        const timestamp = data.timestamp ? new Date(data.timestamp).toLocaleTimeString('it-IT', {
            hour: '2-digit',
            minute: '2-digit'
        }) : '';

        // Create message element
        const messageDiv = document.createElement('div');
        messageDiv.className = 'd-flex flex-row mb-2';

        messageDiv.innerHTML = `
            <div class="col-1">
                <img src="${avatarUrl}" alt="Avatar di ${escapeHtml(data.username)}" class="chat-message-avatar rounded-circle">
            </div>
            <div class="col-11 d-flex flex-column">
                <div class="d-flex align-items-baseline">
                    <h5 class="mb-0">${escapeHtml(data.username)}</h5>
                    ${timestamp ? `<small class="text-muted ms-2">${timestamp}</small>` : ''}
                </div>
                <p class="mb-0">${escapeHtml(data.message)}</p>
            </div>
        `;

        chatMessages.appendChild(messageDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    // Listen for new messages
    socket.on("chat message", (data) => {
        console.log("Messaggio ricevuto dal server:", data);
        if (!data || !data.username || !data.message) {
            console.error("Errore: Messaggio ricevuto non valido", data);
            return;
        }
        renderMessage(data);
    });

    // Listen for message history when joining
    socket.on("load messages", (messages) => {
        console.log(`Caricamento di ${messages.length} messaggi precedenti`);
        messages.forEach(message => renderMessage(message));
    });
}

// Wait for DOM to be ready
document.addEventListener('DOMContentLoaded', function() {
    initializeChat();
    
    // Check for username changes when window gets focus (user might have logged in in another tab)
    window.addEventListener('focus', function() {
        const currentUsername = localStorage.getItem("username");
        const chatUsername = document.getElementById("chatUsername");
        
        if (chatUsername && chatUsername.textContent !== currentUsername) {
            console.log('[CHAT] Username changed, reinitializing chat...');
            location.reload(); // Reload to reinitialize chat with new user
        }
    });
});
