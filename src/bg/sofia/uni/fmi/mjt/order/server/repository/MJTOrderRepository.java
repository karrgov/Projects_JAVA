package bg.sofia.uni.fmi.mjt.order.server.repository;

import bg.sofia.uni.fmi.mjt.order.server.Response;
import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.order.Order;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Color;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Size;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.TShirt;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MJTOrderRepository implements OrderRepository {

    private static final String INITIAL_ERROR_MESSAGE = "invalid: ";
    private static final int INVALID_ORDER_ID = -1;

    private int orderId;
    private List<Order> orderList;

    public MJTOrderRepository() {
        this.orderList = new LinkedList<>();
        this.orderId = 1;
    }

    @Override
    public Response request(String size, String color, String destination) {
        if(size == null || color == null || destination == null) {
            throw new IllegalArgumentException("Order should not contain any null elements!");
        }

        StringBuilder errorMessage = new StringBuilder(INITIAL_ERROR_MESSAGE);
        TShirt tShirt = new TShirt(Size.sizeFromString(size), Color.colorFromString(color));
        Destination destinationEnum = Destination.destinationFromString(destination);

        if(!isDataValid(tShirt.size(), tShirt.color(), destinationEnum, errorMessage).get()) {
            this.orderList.add(new Order(INVALID_ORDER_ID, tShirt, destinationEnum));
            return Response.decline(errorMessage.toString());
        }

        this.orderList.add(new Order(this.orderId, tShirt, destinationEnum));
        this.orderId++;
        return Response.create(this.orderId);
    }

    @Override
    public Response getOrderById(int id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id can not be a negative number!");
        }

        return this.orderList.stream()
                .filter(o -> o.id() == id)
                .findFirst()
                .map(val -> Response.ok(List.of(val)))
                .orElseGet(() -> Response.notFound(id));
    }

    @Override
    public Response getAllOrders() {
        return Response.ok(this.orderList);
    }

    @Override
    public Response getAllSuccessfulOrders() {
        return Response.ok(this.orderList.stream().filter(o -> o.id() != INVALID_ORDER_ID).toList());
    }

    private AtomicBoolean isDataValid(Size size, Color color, Destination destination, StringBuilder errorMessage) {
        AtomicBoolean result = new AtomicBoolean(true);

        if(size == Size.UNKNOWN) {
            result.set(false);
            errorMessage.append("size");
        }

        if(color == Color.UNKNOWN) {
            if(!result.get()) {
                errorMessage.append(",");
            }
            errorMessage.append("color");
            result.set(false);
        }

        if(destination == Destination.UNKNOWN) {
            if(!result.get()) {
                errorMessage.append(",");
            }
            errorMessage.append("destination");
            result.set(false);
        }

        return result;
    }

}
