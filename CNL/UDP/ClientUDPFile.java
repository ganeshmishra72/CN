package CNL.UDP;

import java.io.*;
import java.net.*;

public class ClientUDPFile {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(6000);
            System.out.println("Waiting for file...");

            byte[] buffer = new byte[4096];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // First receive file name
            socket.receive(packet);
            String fileName = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Receiving file: " + fileName);

            FileOutputStream fos = new FileOutputStream("received_" + fileName);

            while (true) {
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());

                if (msg.equals("EOF")) {
                    System.out.println("File received successfully.");
                    break;
                }

                fos.write(packet.getData(), 0, packet.getLength());
            }

            fos.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
