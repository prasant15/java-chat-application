import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ClientGUI {
    private JTextArea chatArea;
    private JTextField messageField;
    private PrintWriter out;

    public ClientGUI() throws IOException {
        Socket socket = new Socket("localhost", 8180);

        BufferedReader serverInput = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // UI Setup
        JFrame frame = new JFrame("Chat Application");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        messageField = new JTextField();
        JButton sendButton = new JButton("Send");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(messageField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        frame.add(panel, BorderLayout.SOUTH);

        frame.setVisible(true);

        // Send button action
        sendButton.addActionListener(e -> sendMessage());

        // Enter key send
        messageField.addActionListener(e -> sendMessage());

        // Receive messages thread
        new Thread(() -> {
            String msg;
            try {
                while ((msg = serverInput.readLine()) != null) {
                    chatArea.append(msg + "\n");
                }
            } catch (IOException e) {
                chatArea.append("Disconnected\n");
            }
        }).start();
    }

    private void sendMessage() {
        String msg = messageField.getText();
        out.println(msg);
        messageField.setText("");
    }

    public static void main(String[] args) throws IOException {
        new ClientGUI();
    }
}
