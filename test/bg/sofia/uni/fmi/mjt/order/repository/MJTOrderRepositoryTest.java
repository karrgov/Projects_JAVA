package bg.sofia.uni.fmi.mjt.order.repository;

import bg.sofia.uni.fmi.mjt.order.server.Response;
//import bg.sofia.uni.fmi.mjt.order.server.order.Order;
import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.repository.MJTOrderRepository;
import bg.sofia.uni.fmi.mjt.order.server.repository.OrderRepository;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Color;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Size;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MJTOrderRepositoryTest {

    private static OrderRepository orderRepository;

    @BeforeAll
    static void setUp() {
        orderRepository = new MJTOrderRepository();
        orderRepository.request("M", "WHITE", "NORTH_AMERICA");
        orderRepository.request("M", "BLACK", "EUROPE");
        orderRepository.request("L", "RED", "EUROPE");
        orderRepository.request("ahhha", "auuaja", "ajajaj");
    }

    @Test
    @Order(1)
    void testGetAllOrders() {
//        System.out.println("First");
        Response response = orderRepository.getAllOrders();

        assertEquals(4, response.orders().size(),
                "Size of orders should match!");

        assertEquals(Response.Status.OK, response.status(),
                "Statuses should be equal!");
    }

    @Test
    @Order(2)
    void testGetAllSuccessfulOrders() {
//        System.out.println("Second");
        Response response = orderRepository.getAllSuccessfulOrders();

        assertEquals(3, response.orders().size(),
                "Size of orders should match!");

        assertEquals(Response.Status.OK, response.status(),
                "Statuses should be equal!");
    }

    @Test
    @Order(3)
    void testRequestWithInvalidParameters() {
//        System.out.println("Third");
        Response response = orderRepository.request("hahah", "ajhjaha", "auaau");

        assertEquals(Response.Status.DECLINED, response.status(),
                "Statuses should match!");

        assertEquals(5, orderRepository.getAllOrders().orders().size(),
                "Size of orders should match!");

        assertEquals(3, orderRepository.getAllSuccessfulOrders().orders().size(),
                "Statuses should be equal!");
    }

    @Test
    @Order(4)
    void testRequestWithValidParameters() {
//        System.out.println("Fourth");
        Response response = orderRepository.request("S", "BLACK", "EUROPE");

        assertEquals(Response.Status.CREATED, response.status(),
                "Statuses should match!");

        assertEquals(6, orderRepository.getAllOrders().orders().size(),
                "Size of orders should match!");

        assertEquals(4, orderRepository.getAllSuccessfulOrders().orders().size(),
                "Statuses should be equal!");

        Optional<bg.sofia.uni.fmi.mjt.order.server.order.Order> correctOrder = orderRepository.getOrderById(4).orders().stream().findFirst();

        assertTrue(correctOrder.isPresent(),
                "The order should be present!");

        assertEquals(Size.S, correctOrder.get().tShirt().size(),
                "Sizes should match!");

        assertEquals(Color.BLACK, correctOrder.get().tShirt().color(),
                "Colors should match!");

        assertEquals(Destination.EUROPE, correctOrder.get().destination(),
                "Destinations should match!");
    }

    @Test
    void testRequestWithNullParametersThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> orderRepository.request(null, "BLACK", "EUROPE"),
                "Should throw an exception, because null parameters can not be accepted!");

        assertThrows(IllegalArgumentException.class, () -> orderRepository.request("S", null, "EUROPE"),
                "Should throw an exception, because null parameters can not be accepted!");

        assertThrows(IllegalArgumentException.class, () -> orderRepository.request("S", "BLACK", null),
                "Should throw an exception, because null parameters can not be accepted!");
    }

    @Test
    void testGetOrderByIdNotFoundStatusIfIdDoesNotExist() {
        assertEquals(Response.Status.NOT_FOUND, orderRepository.getOrderById(78).status(),
                "Status should be NOT_FOUND!");
    }

    @Test
    void testGetOrderByIdReturnsCorrectOrder() {
//        Optional<Order> correctOrder = orderRepository.getOrderById(1).orders().stream().findFirst();

        assertTrue(orderRepository.getOrderById(1).orders().stream().findFirst()
                        .isPresent(),
                "The order should be present!");

        assertEquals(Size.M, orderRepository.getOrderById(1).orders().stream().findFirst()
                        .get().tShirt().size(),
                "Sizes should match!");

        assertEquals(Color.WHITE, orderRepository.getOrderById(1).orders().stream().findFirst()
                        .get().tShirt().color(),
                "Colors should match!");

        assertEquals(Destination.NORTH_AMERICA, orderRepository.getOrderById(1).orders().stream().findFirst()
                        .get().destination(),
                "Destinations should match!");
    }

    @Test
    void testGetOrderByIdThrowsExceptionForNegativeOrZeroId() {
        assertThrows(IllegalArgumentException.class, () -> orderRepository.getOrderById(-1),
                "Order ID can not be a negative number!");

        assertThrows(IllegalArgumentException.class, () -> orderRepository.getOrderById(0),
                "Order ID can not be zero!");
    }

}
