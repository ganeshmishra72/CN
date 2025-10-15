// TCP Say Hello
package CNL.TCP;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Connected to Server.");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Thread for reading messages from server
            Thread readThread = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        if (msg.equalsIgnoreCase("exit")) {
                            System.out.println("Server requested to exit.");
                            break;
                        }
                        System.out.println("Server: " + msg);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                } finally {
                    try {
                        socket.close();
                        System.exit(0);
                    } catch (IOException ignored) {
                    }
                }
            });

            // Thread for sending messages to server
            Thread writeThread = new Thread(() -> {
                try {
                    BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
                    String msg;
                    while ((msg = console.readLine()) != null) {
                        out.println(msg);
                        if (msg.equalsIgnoreCase("exit")) {
                            System.out.println("Client requested to exit.");
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                } finally {
                    try {
                        socket.close();
                        System.exit(0);
                    } catch (IOException ignored) {
                    }
                }
            });

            readThread.start();
            writeThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
