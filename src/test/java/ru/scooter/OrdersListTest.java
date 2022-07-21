package ru.scooter;

import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.scooter.pojo.ScooterApiClient;

import static org.hamcrest.CoreMatchers.notNullValue;

public class OrdersListTest {

    ScooterApiClient client;

    @Before
    public void setUp() {
        client = new ScooterApiClient();
    }

    // проверить список всех заказов
    @Test
    public void getOrdersList() {
        Response response = client.getOrders();
        response
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("orders", notNullValue());
    }
}
