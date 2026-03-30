import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8180);

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

// 👇 ADD HERE
        System.out.print("Enter your name: ");
        String name = input.readLine();

// Thread for receiving messages
        new Thread(() -> {
            String msg;
            try {
                while ((msg = serverInput.readLine()) != null) {
                    System.out.println(msg);
                }
            } catch (IOException e) {
                System.out.println("Disconnected");
            }
        }).start();

// Sending messages
        String userMsg;
        while ((userMsg = input.readLine()) != null) {
            out.println("[" + name + "]: " + userMsg); // 👈 UPDATED
        }
    }
}