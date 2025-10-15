
package CNL.UDP;

import java.net.*;
import java.io.*;

public class Clientcalcu {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(6001);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Connected to UDP Calculator Server (port 5001).");
            System.out.println("Type expressions like: 10 + 5");
            System.out.println("Type 'exit' to quit.");

            Thread receiveThread = new Thread(() -> {
                try {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        String msg = new String(packet.getData(), 0, packet.getLength());
                        System.out.println("\nServer: " + msg);

                        if (msg.equalsIgnoreCase("exit")) {
                            System.out.println("Server exited. Closing client...");
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
                        DatagramPacket packet = new DatagramPacket(data, data.length, address, 5001);
                        socket.send(packet);

                        if (msg.equalsIgnoreCase("exit")) {
                            System.out.println("Client exiting...");
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
