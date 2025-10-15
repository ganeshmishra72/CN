// TCP Say Hello

package CNL.TCP;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server started. Waiting for client...");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Thread for reading messages from client
            Thread readThread = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        if (msg.equalsIgnoreCase("exit")) {
                            System.out.println("Client requested to exit.");
                            out.println("exit"); // notify client
                            break;
                        }
                        System.out.println("Client: " + msg);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                } finally {
                    try {
                        socket.close();
                        serverSocket.close();
                        System.exit(0);
                    } catch (IOException ignored) {
                    }
                }
            });

            // Thread for sending messages to client
            Thread writeThread = new Thread(() -> {
                try {
                    BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
                    String msg;
                    while ((msg = console.readLine()) != null) {
                        out.println(msg);
                        if (msg.equalsIgnoreCase("exit")) {
                            System.out.println("Server requested to exit.");
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                } finally {
                    try {
                        socket.close();
                        serverSocket.close();
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
