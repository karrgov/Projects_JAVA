package bg.sofia.uni.fmi.mjt.order.server;

import bg.sofia.uni.fmi.mjt.order.server.repository.MJTOrderRepository;
import bg.sofia.uni.fmi.mjt.order.server.repository.OrderRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TShirtShopServer {

    private static final int SERVER_PORT = 4444;
    private static final int MAX_EXECUTOR_THREADS = 20;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);

        Thread.currentThread().setName("Server thread");
        OrderRepository orderRepository = new MJTOrderRepository();

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started and listening for a connection!");

            Socket clientSocket;

            //noinspection InfiniteLoopStatement
            while (true) {
                clientSocket = serverSocket.accept();

                System.out.println("Accepted connection from client: " + clientSocket.getInetAddress());

                ClientRequestHandler clientHandler = new ClientRequestHandler(clientSocket, orderRepository);

                executor.execute(clientHandler);

            }
        } catch (IOException e) {
            throw new RuntimeException("There was a problem with the server socket!", e);
        } finally {
            executor.shutdown();
        }
    }

}
