package ru.scooter;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.scooter.pojo.ExistingCourier;
import ru.scooter.pojo.ScooterApiClient;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.junit.Assert.assertEquals;

public class LoginCourierWithNonExistentCredentialsTest {

    ScooterApiClient client;
    String password = RandomStringUtils.randomAlphabetic(10);
    String login = RandomStringUtils.randomAlphabetic(10);

    @Before
    public void setUp() {
        client = new ScooterApiClient();
    }

    // если авторизоваться под несуществующим пользователем, запрос возвращает ошибку
    @Test
    public void loginNewCourierThatNotExist() {

        ExistingCourier existingCourier = new ExistingCourier(login, password);
        Response responseLogin = client.loginCourier(existingCourier);
        assertEquals(SC_NOT_FOUND, responseLogin.statusCode());
        String responseMessage = responseLogin.body().jsonPath().getString("message");
        assertEquals(responseMessage, "Учетная запись не найдена");
    }
}
