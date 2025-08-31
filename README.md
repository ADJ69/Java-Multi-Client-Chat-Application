# Java-Multi-Client-Chat-Application
This project demonstrates a multi-client chat system using Java Sockets and multithreading.

📂 Project Structure
├── ChatServer.java   # Server code
├── ChatClient.java   # Client code
└── README.md         # Documentation

🚀 How It Works
Server
Listens on a fixed port (5050).
Accepts new clients and spawns a thread for each client.
Stores all client output streams in a shared set.
Broadcasts messages from one client to all others.

Client
Connects to the server using IP & port.
Sends the username at connection time.
Runs two threads: one for sending and one for receiving.
Displays all incoming messages from the server.

🔎 How It Works Internally
ChatServer uses ServerSocket to accept connections.
Each client runs in a ClientHandler thread.
Messages are broadcast using a shared set of writers.
ChatClient runs two threads:
One reads input from the user and sends it.

One listens for messages from the server.
