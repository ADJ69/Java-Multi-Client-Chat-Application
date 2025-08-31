import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER = "127.0.0.1";
    private static final int PORT = 5050;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER, PORT)) {
            System.out.println("Connected to chat server");

            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Thread for reading messages from server
            new Thread(() -> {
                try {
                    String response;
                    while ((response = serverIn.readLine()) != null) {
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            }).start();

            // Main thread for sending messages
            String msg;
            while ((msg = input.readLine()) != null) {
                out.println(msg);
                if (msg.equalsIgnoreCase("exit")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
