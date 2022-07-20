package ru.scooter;

import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.scooter.pojo.Order;
import ru.scooter.pojo.ScooterApiClient;
import java.util.Arrays;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ru.scooter.pojo.Order.getNewOrder;

@RunWith(Parameterized.class)
public class NewOrderTest {

    ScooterApiClient client;
    Order newOrder;
    public final String[] color;

    public NewOrderTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object setOrderColor() {
        return Arrays.asList(new String[][] { {"BLACK", "GREY"} }, new String[][] { {"BLACK"} },
                new String[][] { {"GREY"} }, new String[][] { {} });
    }

    @Before
    public void setUp() {
        client = new ScooterApiClient();
        newOrder = getNewOrder();
    }

    // создание заказа с обоими цветами
    @Test
    public void createNewOrder() {
        newOrder.setColor(color);
        Response responseCreate = client.createOrder(newOrder);
        assertEquals(SC_CREATED, responseCreate.statusCode());
        String responseMessage = responseCreate.body().toString();
        assertNotNull(responseMessage);

    }

}
