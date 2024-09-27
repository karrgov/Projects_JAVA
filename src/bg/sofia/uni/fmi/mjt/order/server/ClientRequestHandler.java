package bg.sofia.uni.fmi.mjt.order.server;

import bg.sofia.uni.fmi.mjt.order.server.repository.OrderRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRequestHandler implements Runnable {

    private static final String UNKNOWN_COMMAND_MESSAGE = "";
    private static final int INDEX_OF_COMMAND = 0;
    private static final int INDEX_OF_SECOND_WORD_GET_COMMAND = 1;
    private static final int INDEX_OF_ID_GET_COMMAND = 2;
    private static final int INDEX_OF_SIZE_REQUEST_COMMAND = 1;
    private static final int INDEX_OF_COLOR_REQUEST_COMMAND = 1;
    private static final int INDEX_OF_DESTINATION_REQUEST_COMMAND = 1;
    private static final int INDEX_OF_VALUE_IN_PAIR = 1;

    private final Socket socket;
    private final OrderRepository orderRepository;

    public ClientRequestHandler(Socket socket, OrderRepository orderRepository) {
        this.socket = socket;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Client Request handler for: " + this.socket.getRemoteSocketAddress());

        try(PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                handleInput(inputLine, out);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                this.socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handleInput(String inputLine, PrintWriter out) {
        String[] inputParams = inputLine.split("\\s+");

        switch (inputParams[INDEX_OF_COMMAND].toLowerCase()) {
            case "get" -> handleGetCommand(inputParams, out);
            case "request" -> handleRequestCommand(inputParams, out);
            default -> out.println(UNKNOWN_COMMAND_MESSAGE);
        }
    }

    private void handleGetCommand(String[] inputParams, PrintWriter out) {
        switch (inputParams[INDEX_OF_SECOND_WORD_GET_COMMAND].toLowerCase()) {
            case "all" -> out.println(this.orderRepository.getAllOrders());
            case "all-successful" -> out.println(this.orderRepository.getAllSuccessfulOrders());
            case "my-order" -> out.println(this.orderRepository.getOrderById(
                    Integer.parseInt(getValueFromEqualsSignPair(inputParams[INDEX_OF_ID_GET_COMMAND]))));
        }
    }

    private void handleRequestCommand(String[] inputParams, PrintWriter out) {
        String size = getValueFromEqualsSignPair(inputParams[INDEX_OF_SIZE_REQUEST_COMMAND]);
        String color = getValueFromEqualsSignPair(inputParams[INDEX_OF_COLOR_REQUEST_COMMAND]);
        String destination = getValueFromEqualsSignPair(inputParams[INDEX_OF_DESTINATION_REQUEST_COMMAND]);

        out.println(this.orderRepository.request(size, color, destination));
    }

    private String getValueFromEqualsSignPair(String pair) {
        return pair.split("=")[INDEX_OF_VALUE_IN_PAIR];
    }

}
