// UDP Two-Way Chat - Server
package CNL.UDP;

import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(5000);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("UDP Server started. Waiting for client...");

            Thread receiveThread = new Thread(() -> {
                try {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        String msg = new String(packet.getData(), 0, packet.getLength());
                        System.out.println("\nClient: " + msg);

                        if (msg.equalsIgnoreCase("exit")) {
                            System.out.println("Client exited. Closing server...");
                            socket.close();
                            System.exit(0);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Connection closed.");
                }
            });

            Thread sendThread = new Thread(() -> {
                try {
                    while (true) {
                        String msg = console.readLine();
                        if (msg == null)
                            continue;

                        InetAddress address = InetAddress.getByName("localhost");
                        byte[] data = msg.getBytes();
                        DatagramPacket packet = new DatagramPacket(data, data.length, address, 6000);
                        socket.send(packet);

                        if (msg.equalsIgnoreCase("exit")) {
                            System.out.println("Server exiting...");
                            socket.close();
                            System.exit(0);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Connection closed.");
                }
            });

            receiveThread.start();
            sendThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
