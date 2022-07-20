package ru.scooter;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.scooter.pojo.NewCourier;
import ru.scooter.pojo.ScooterApiClient;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.junit.Assert.assertEquals;

public class CreatingCourierWithoutPasswordTest {

    String login = RandomStringUtils.randomAlphabetic(10);
    ScooterApiClient client;

    @Before
    public void setUp() {
        client = new ScooterApiClient();
    }

    // если одного из полей нет, запрос возвращает ошибку
    @Test
    public void attemptToCreateCourierWithoutImportantFields() {

        final NewCourier newCourier = new NewCourier(login,
                null,
                null);

        Response responseCreate = client.createCourier(newCourier);
        assertEquals(SC_BAD_REQUEST, responseCreate.statusCode());

        String responseMessage = responseCreate.body().jsonPath().getString("message");
        assertEquals(responseMessage, "Недостаточно данных для создания учетной записи");
    }
}
