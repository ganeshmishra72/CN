
package CNL.UDP;

import java.net.*;
import java.io.*;

public class Servercalcu {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(5001);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("UDP Calculator Server started on port 5001.");

            Thread receiveThread = new Thread(() -> {
                try {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        String expr = new String(packet.getData(), 0, packet.getLength());
                        System.out.println("\nClient: " + expr);

                        InetAddress clientAddress = packet.getAddress();
                        int clientPort = packet.getPort();

                        if (expr.equalsIgnoreCase("exit")) {
                            System.out.println("Client exited. Closing server...");
                            socket.close();
                            System.exit(0);
                        }

                        // Perform calculation and send result
                        String result = calculate(expr);
                        byte[] sendData = result.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress,
                                clientPort);
                        socket.send(sendPacket);
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
                        DatagramPacket packet = new DatagramPacket(data, data.length, address, 6001);
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

    private static String calculate(String expr) {
        try {
            String[] parts = expr.split(" ");
            if (parts.length != 3)
                return "Invalid format! Use: <num1> <operator> <num2>";

            double n1 = Double.parseDouble(parts[0]);
            double n2 = Double.parseDouble(parts[2]);
            String op = parts[1];
            double res;

            switch (op) {
                case "+":
                    res = n1 + n2;
                    break;
                case "-":
                    res = n1 - n2;
                    break;
                case "*":
                    res = n1 * n2;
                    break;
                case "/":
                    if (n2 == 0)
                        return "Error: Division by zero!";
                    res = n1 / n2;
                    break;
                default:
                    return "Invalid operator! Use +, -, *, /";
            }
            return "Result: " + res;
        } catch (Exception e) {
            return "Error: Invalid input!";
        }
    }
}
