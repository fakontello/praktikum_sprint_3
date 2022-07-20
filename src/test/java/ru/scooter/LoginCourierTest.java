package ru.scooter;;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.scooter.pojo.ExistingCourier;
import ru.scooter.pojo.NewCourier;
import ru.scooter.pojo.ScooterApiClient;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ru.scooter.pojo.NewCourier.getRandomCourier;

public class LoginCourierTest {

    ScooterApiClient client;
    NewCourier newCourier;
    int courierId;
    String password = RandomStringUtils.randomAlphabetic(10);

    @Before
    public void setUp() {
        client = new ScooterApiClient();
        newCourier = getRandomCourier();
    }

    @After
    public void deleteCourier() {
        ExistingCourier existingCourierLogin = new ExistingCourier(newCourier.getLogin(), newCourier.getPassword());
        Response secondResponseLogin = client.loginCourier(existingCourierLogin);
        assertEquals(SC_OK, secondResponseLogin.statusCode());
        courierId = secondResponseLogin.body().jsonPath().getInt("id");
        client.deleteCourier(courierId);
    }

    // логин курьера
    @Test
    public void loginNewCourier() {
        Response responseCreate = client.createCourier(newCourier);
        assertEquals(SC_CREATED, responseCreate.statusCode());
        ExistingCourier existingCourier = new ExistingCourier(newCourier.getLogin(), newCourier.getPassword());
        Response responseLogin = client.loginCourier(existingCourier);
        assertEquals(SC_OK, responseLogin.statusCode());
        Integer responseMessage = responseLogin.body().jsonPath().getInt("id");
        assertNotNull(responseMessage);
    }

    // система вернёт ошибку, если неправильно указать логин или пароль
    @Test
    public void incorrectCredentialsTest() {
        Response responseCreate = client.createCourier(newCourier);
        assertEquals(SC_CREATED, responseCreate.statusCode());

        ExistingCourier existingCourier = new ExistingCourier(newCourier.getLogin(), password);
        Response responseLogin = client.loginCourier(existingCourier);
        assertEquals(SC_NOT_FOUND, responseLogin.statusCode());
        String responseMessage = responseLogin.body().jsonPath().getString("message");
        assertEquals(responseMessage, "Учетная запись не найдена");
    }

    // если какого-то поля нет, запрос возвращает ошибку
    @Test
    public void loginWithoutOneOfCredentials() {
        Response responseCreate = client.createCourier(newCourier);
        assertEquals(SC_CREATED, responseCreate.statusCode());

        ExistingCourier existingCourier = new ExistingCourier(null, newCourier.getPassword());
        Response responseLogin = client.loginCourier(existingCourier);
        assertEquals(SC_BAD_REQUEST, responseLogin.statusCode());
        String responseMessage = responseLogin.body().jsonPath().getString("message");
        assertEquals(responseMessage, "Недостаточно данных для входа");
    }

}
