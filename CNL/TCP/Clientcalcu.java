// TCP Calculator Client
package CNL.TCP;

import java.io.*;
import java.net.*;

public class Clientcalcu {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Connected to Calculator Server.");
            System.out.println("Enter expressions like: 10 + 20");
            System.out.println("Type 'exit' to close connection.");

            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String msg;
            while (true) {
                System.out.print("Enter operation: ");
                msg = console.readLine();

                out.println(msg);
                if (msg.equalsIgnoreCase("exit")) {
                    System.out.println("Client requested to exit.");
                    break;
                }

                String response = in.readLine();
                if (response == null || response.equalsIgnoreCase("exit")) {
                    System.out.println("Server closed the connection.");
                    break;
                }

                System.out.println(response);
            }

            socket.close();
            System.out.println("Client closed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
