function initNavBar() {
    if (!sessionStorage.getItem("sessionActive")) {
        localStorage.removeItem("username"); // Delete username if session is not active
        sessionStorage.setItem("sessionActive", "true"); // Set session as active
    }

    let username = localStorage.getItem("username");
    let userSection = document.getElementById("userSection");
    if (username) {
        // If the user is logged in, show the username and logout button
        userSection.innerHTML = `
            <div class="dropdown">
                <button class="btn btn-light dropdown-toggle me-5" type="button" id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                    ${username}
                </button>
                <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                    <li><a class="dropdown-item" href="#" onclick="logout()">Logout</a></li>
                </ul>
            </div>
        `;
    }
}

function logout() {
    localStorage.removeItem("username");
    window.location.href = "/"; // Redirect to homepage after logout
}
