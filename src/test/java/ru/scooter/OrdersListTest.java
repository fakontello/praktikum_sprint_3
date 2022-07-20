package ru.scooter;

import org.junit.Before;
import org.junit.Test;
import ru.scooter.pojo.ScooterApiClient;

public class OrdersListTest {

    ScooterApiClient client;

    @Before
    public void setUp() {
        client = new ScooterApiClient();
    }

    // проверить список всех заказов
    @Test
    public void getOrdersList() {
        client.getOrders();

    }
}
