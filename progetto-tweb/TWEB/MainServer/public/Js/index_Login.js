function returnHome() {
    let username = document.getElementById("user").value.trim();

    if (username) {
        localStorage.setItem("username", username); // Saves username in local storage
        window.location.href = "/"; // Redirects to home page
    } else {
        alert("Inserisci un nome utente!");
    }
}
