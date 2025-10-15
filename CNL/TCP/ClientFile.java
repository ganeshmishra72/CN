package CNL.TCP;

import java.io.*;
import java.net.*;

public class ClientFile {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 6000);
            System.out.println("Connected to server. Receiving file...");

            DataInputStream dis = new DataInputStream(socket.getInputStream());

            // First get the file name
            String fileName = dis.readUTF();
            System.out.println("Receiving file: " + fileName);

            // Save file locally
            FileOutputStream fos = new FileOutputStream("received_" + fileName);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = dis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            fos.close();
            dis.close();
            socket.close();

            System.out.println("File received successfully and saved as received_" + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
