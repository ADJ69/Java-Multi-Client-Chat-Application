import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 5050;
    private static Set<ClientHandler> clients = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Chat Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);

                // Create a new thread for each client
                ClientHandler client = new ClientHandler(socket);
                clients.add(client);
                client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Broadcast a message to all clients
    public static void broadcast(String message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }

    // Inner class for handling each client
    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String name;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // First message from client = name
                out.println("Enter your name: ");
                name = in.readLine();
                System.out.println(name + " has joined the chat.");
                broadcast("📢 " + name + " has joined the chat!", this);

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) {
                        break;
                    }
                    System.out.println(name + ": " + message);
                    broadcast(name + ": " + message, this);
                }
            } catch (IOException e) {
                System.out.println("Connection lost with " + name);
            } finally {
                try { socket.close(); } catch (IOException e) {}
                synchronized (clients) { clients.remove(this); }
                broadcast("❌ " + name + " left the chat.", this);
            }
        }

        // Send message to this client
        private void sendMessage(String message) {
            out.println(message);
        }
    }
}
