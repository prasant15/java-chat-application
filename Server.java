import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8180);
        System.out.println("Server started...");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("New client connected");

            ClientHandler client = new ClientHandler(socket);
            clients.add(client);

            new Thread(client).start();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        }

        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    System.out.println("Message: " + message);
                    broadcast(message);
                }
            } catch (IOException e) {
                System.out.println("Client disconnected");
            }
        }

        private void broadcast(String message) {
            for (ClientHandler client : clients) {
                client.out.println(message);
            }
        }
    }
}
