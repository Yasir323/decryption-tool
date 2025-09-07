package org.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Properties;
import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Properties props = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Config file not found");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String url = props.getProperty("api.url");
        String secret = props.getProperty("api.secret");

        JFrame frame = new JFrame();
        frame.setTitle("Decryption Tool");
        frame.setSize(640, 480);
        frame.setResizable(true);

        // Label
        JLabel label = new JLabel("Encrypted text");

        // Input box
        JTextField tf = new JTextField();
        tf.setToolTipText("Encrypted text");
        tf.setMaximumSize(new java.awt.Dimension(600, 30));

        // Button
        JButton button = new JButton("Decrypt");
        JLabel decryptedText = new JLabel();

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 String encryptedText = tf.getText();
                 DecryptionService ds = new DecryptionService(url, secret);
                 decryptedText.setText(ds.decrypt(encryptedText));
            }
        });

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(label);
        frame.add(tf);
        frame.add(button);
        frame.add(decryptedText);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you want to exit?",
                        "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION
                );
                if (result == JOptionPane.YES_OPTION) {
                    frame.dispose();
                    System.exit(0);
                }
            }
        });
        frame.setVisible(true);
    }
}