// TCP Calculator Server
package CNL.TCP;

import java.io.*;
import java.net.*;

public class Servercalcu {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Calculator Server started. Waiting for client...");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String input;
            while ((input = in.readLine()) != null) {
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Client requested to exit.");
                    out.println("exit");
                    break;
                }

                System.out.println("Received from client: " + input);
                String result = calculate(input);
                out.println(result);
            }

            socket.close();
            serverSocket.close();
            System.out.println("Server closed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to perform calculation
    private static String calculate(String expression) {
        try {
            String[] parts = expression.split(" ");
            if (parts.length != 3) {
                return "Invalid format! Use: <num1> <operator> <num2>";
            }

            double num1 = Double.parseDouble(parts[0]);
            String op = parts[1];
            double num2 = Double.parseDouble(parts[2]);
            double result;

            switch (op) {
                case "+":
                    result = num1 + num2;
                    break;
                case "-":
                    result = num1 - num2;
                    break;
                case "*":
                    result = num1 * num2;
                    break;
                case "/":
                    if (num2 == 0)
                        return "Error: Division by zero!";
                    result = num1 / num2;
                    break;
                default:
                    return "Invalid operator! Use +, -, *, /";
            }

            return "Result: " + result;
        } catch (Exception e) {
            return "Error: Invalid input!";
        }
    }
}
