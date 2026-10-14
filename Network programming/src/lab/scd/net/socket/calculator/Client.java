package lab.scd.net.socket.calculator;

import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;

public class Client extends JFrame {

    private JTextField nr1Field;
    private JTextField nr2Field;
    private JTextField resultField;

    private JButton plusButton;
    private JButton minusButton;
    private JButton multiplyButton;
    private JButton divideButton;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;


    public Client() {
        // GUI

        setTitle("Network Calculator");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        nr1Field = new JTextField();
        nr2Field = new JTextField();
        resultField = new JTextField();

        resultField.setEditable(false);

        plusButton = new JButton("+");
        minusButton = new JButton("-");
        multiplyButton = new JButton("*");
        divideButton = new JButton("/");

        add(new JLabel("Numarul 1:"));
        add(nr1Field);

        add(new JLabel("Numarul 2:"));
        add(nr2Field);

        add(plusButton);
        add(minusButton);

        add(multiplyButton);
        add(divideButton);

        add(new JLabel("Rezultat:"));
        add(resultField);

        // Conectare la server

        try {
            InetAddress address = InetAddress.getByName("10.132.65.15");
            socket = new Socket(address, 1900);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            System.out.println("Conectat la server!");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Nu ma pot conecta la server!");

            e.printStackTrace();
        }

        // Evenimente butoane

        plusButton.addActionListener(e -> sendOperation("+"));
        minusButton.addActionListener(e -> sendOperation("-"));
        multiplyButton.addActionListener(e -> sendOperation("*"));
        divideButton.addActionListener(e -> sendOperation("/"));
    }


    private void sendOperation(String operatie) {

        try {
            String nr1 = nr1Field.getText();
            String nr2 = nr2Field.getText();

            Double.parseDouble(nr1);
            Double.parseDouble(nr2);

            String mesaj = nr1 + ";" + nr2 + ";" + operatie;
            System.out.println("Trimit la server: " + mesaj);

            out.println(mesaj);

            String rezultat = in.readLine();
            System.out.println("Primit de la server: " + rezultat);

            resultField.setText(rezultat);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Introdu doua numere valide!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Eroare de comunicare cu serverul!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.setVisible(true);
    }
}
