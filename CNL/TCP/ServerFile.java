package CNL.TCP;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class ServerFile {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6000);
            System.out.println("Server started. Waiting for client...");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");

            // Open file chooser
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                System.out.println("Sending file: " + file.getName());

                // Send file name first
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(file.getName());

                // Send file data
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }

                fis.close();
                dos.close();
                socket.close();
                serverSocket.close();

                System.out.println("File sent successfully.");
            } else {
                System.out.println("No file selected. Closing server.");
                socket.close();
                serverSocket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
