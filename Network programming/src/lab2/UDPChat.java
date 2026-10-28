package lab2;


import java.awt.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.*;

public class UDPChat extends JFrame {

    private static final int MY_PORT = 5000;
    private static final int FRIEND_PORT = 5001;
    private static final String FRIEND_IP = "10.132.76.247";

    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;

    private DatagramSocket socket;
    private InetAddress friendAddress;

    private final BlockingQueue<String> messages =
            new LinkedBlockingQueue<>();

    public UDPChat() throws Exception {

        socket = new DatagramSocket(MY_PORT);
        friendAddress = InetAddress.getByName(FRIEND_IP);

        setTitle("UDP Chat - Port " + MY_PORT);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Chat display
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(chatArea);

        // Message input
        messageField = new JTextField();
        sendButton = new JButton("Send");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Send button
        sendButton.addActionListener(e -> sendMessage());

        // Send with Enter key
        messageField.addActionListener(e -> sendMessage());

        // THREAD 1: SENDING MESSAGES
        Thread senderThread = new Thread(() -> {

            while (!socket.isClosed()) {
                try {
                    String message = messages.take();
                    byte[] data = message.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket packet = new DatagramPacket(data, data.length, friendAddress, FRIEND_PORT);
                    socket.send(packet);
                    displayMessage("Me: " + message);

                } catch (Exception e) {
                    if (!socket.isClosed()) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // THREAD 2: RECEIVING MESSAGES
        Thread receiverThread = new Thread(() -> {

            while (!socket.isClosed()) {
                try {
                    byte[] buffer = new byte[4096];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String message = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
                    displayMessage("Friend: " + message);

                } catch (Exception e) {
                    if (!socket.isClosed()) {
                        e.printStackTrace();
                    }
                }
            }
        });

        senderThread.setDaemon(true);
        receiverThread.setDaemon(true);

        senderThread.start();
        receiverThread.start();

        setVisible(true);
    }

    // Add message to sending queue
    private void sendMessage() {

        String message = messageField.getText().trim();

        if (!message.isEmpty()) {
            messages.offer(message);
            messageField.setText("");
        }
    }

    // Display messages in chat window
    private void displayMessage(String message) {

        SwingUtilities.invokeLater(() -> {
            chatArea.append(message + "\n");
            chatArea.setCaretPosition(
                    chatArea.getDocument().getLength()
            );
        });
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                new UDPChat();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        });
    }
}