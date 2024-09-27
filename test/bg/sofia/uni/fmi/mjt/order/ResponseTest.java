package bg.sofia.uni.fmi.mjt.order;

import bg.sofia.uni.fmi.mjt.order.server.Response;
import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.order.Order;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Color;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Size;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.TShirt;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ResponseTest {

    @Test
    void testResponseCreate() {
        Response response = Response.create(4);

        assertEquals(Response.Status.CREATED, response.status(),
                "Statuses should be equal!");

        assertEquals("ORDER_ID=4", response.additionalInfo(),
                "Additional info should be equal!");

        assertNull(response.orders(),
                "Orders should be null!");
    }

    @Test
    void testResponseOk() {
        Response response = Response.ok(List.of(
                new Order(1, new TShirt(Size.M, Color.RED), Destination.NORTH_AMERICA),
                new Order(2, new TShirt(Size.L, Color.BLACK), Destination.EUROPE)
        ));

        assertEquals(Response.Status.OK, response.status(),
                "Statuses should be equal!");

        assertEquals(2, response.orders().size(),
                "Number of orders should be the same!");

        assertEquals("Successfully retrieved 2 orders.", response.additionalInfo(),
                "Additional info should be equal!");

        assertEquals("{\"status\":\"OK\", \"additionalInfo\":\"Successfully retrieved 2 orders.\", "
                + "\"orders\":["
                + "{\"id\":1, \"tShirt\":{\"size\":\"M\", \"color\":\"RED\"}, "
                + "\"destination\":\"NORTH_AMERICA\"}, "
                + "{\"id\":2, \"tShirt\":{\"size\":\"L\", \"color\":\"BLACK\"}, "
                + "\"destination\":\"EUROPE\"}]}", response.toString(),
                "Responses should be equal!");
    }

    @Test
    void testResponseDecline() {
        Response response = Response.decline("Order is declined!");

        assertEquals(Response.Status.DECLINED, response.status(),
                "Statuses should be equal!");

        assertEquals("Order is declined!", response.additionalInfo(),
                "Additional info should be equal!");

        assertNull(response.orders(),
                "Orders should be null!");
    }

    @Test
    void testResponseNotFound() {
        Response response = Response.notFound(75);

        assertEquals(Response.Status.NOT_FOUND, response.status(),
                "Statuses should be equal!");

        assertEquals("Order with id = 75 does not exist.", response.additionalInfo(),
                "Additional info should be equal!");

        assertNull(response.orders(),
                "Orders should be null!");
    }

}
