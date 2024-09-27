package bg.sofia.uni.fmi.mjt.order.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TShirtShopClient {

    private static final int SERVER_PORT = 4444;

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            Thread.currentThread().setName("TShirt client thread: " + socket.getLocalPort());

            System.out.println("Connected to the server!");

            while (true) {
            System.out.print("Enter message:");
            String message = scanner.nextLine();

            if(message.equals("disconnect")) {
                break;
            }

            out.println(message);

            String reply = in.readLine();
            System.out.print("The server replied <" + reply + ">");
            }
        } catch (IOException e) {
            throw new RuntimeException("There was a problem with the communication!", e);
        }
    }

}
