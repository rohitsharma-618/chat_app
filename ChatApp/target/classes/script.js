let socket;
const chatWindow = document.getElementById("chatWindow");
const loginMessage = document.getElementById("loginMessage");
const loginBtn = document.getElementById("loginBtn");
const sendBtn = document.getElementById("sendBtn");
const messageInput = document.getElementById("messageInput");

loginBtn.addEventListener("click", () => {
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();

    // Check if username and password are not empty
    if (!username || !password) {
        loginMessage.textContent = "Please enter both username and password!";
        return;
    }

    // Create a WebSocket connection
    socket = new WebSocket("ws://localhost:5000");

    socket.onopen = () => {
        console.log("Connected to WebSocket server");
        socket.send(`${username}\n${password}`);
    };

    socket.onmessage = (event) => {
        const message = event.data;
        console.log("Received message: ", message); // Debugging line

        if (message === "Authentication successful!") {
            document.querySelector(".login-form").style.display = "none";
            document.querySelector(".chat-container").style.display = "block";
            loginMessage.textContent = ''; // Clear login message
        } else if (message === "Authentication failed!") {
            loginMessage.textContent = "Invalid username or password!";
        } else {
            displayMessage(message, "server");
        }
    };

    socket.onerror = (error) => {
        console.error("WebSocket Error:", error);
        loginMessage.textContent = "WebSocket connection error. Please try again.";
    };

    socket.onclose = () => {
        console.log("Disconnected from WebSocket server");
        loginMessage.textContent = "Disconnected from server.";
    };
});

sendBtn.addEventListener("click", () => {
    const message = messageInput.value.trim();
    if (message) {
        socket.send(message);
        displayMessage(message, "user");
        messageInput.value = ""; // Clear input field
    }
});

function displayMessage(message, type) {
    const messageDiv = document.createElement("div");
    messageDiv.className = `message ${type === "user" ? "user-message" : "server-message"}`;
    messageDiv.textContent = message;
    chatWindow.appendChild(messageDiv);
    chatWindow.scrollTop = chatWindow.scrollHeight; // Scroll to the bottom
}
