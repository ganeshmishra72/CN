package CNL.UDP;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class ServerUDpFile {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress clientAddress = InetAddress.getByName("localhost"); // change to client IP if on different
                                                                            // machine
            int port = 6000;

            // File chooser to select file
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String fileName = file.getName();
                System.out.println("Sending file: " + fileName);

                // Send file name first
                byte[] nameBytes = fileName.getBytes();
                DatagramPacket namePacket = new DatagramPacket(nameBytes, nameBytes.length, clientAddress, port);
                socket.send(namePacket);

                // Send file data
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = fis.read(buffer)) != -1) {
                    DatagramPacket packet = new DatagramPacket(buffer, bytesRead, clientAddress, port);
                    socket.send(packet);
                }

                // Send END marker
                String endMsg = "EOF";
                byte[] endBytes = endMsg.getBytes();
                DatagramPacket endPacket = new DatagramPacket(endBytes, endBytes.length, clientAddress, port);
                socket.send(endPacket);

                fis.close();
                socket.close();
                System.out.println("File sent successfully.");
            } else {
                System.out.println("No file selected. Exiting.");
                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
