package ru.scooter;

import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.scooter.pojo.ExistingCourier;
import ru.scooter.pojo.NewCourier;
import ru.scooter.pojo.ScooterApiClient;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static ru.scooter.pojo.NewCourier.getRandomCourier;

public class CreatingCourierTest {

    ScooterApiClient client;
    NewCourier newCourier;
    int courierId;

    @Before
    public void setUp() {
        client = new ScooterApiClient();
    }

    @After
    public void deleteCourier() {
        ExistingCourier existingCourierLogin = new ExistingCourier(newCourier.getLogin(), newCourier.getPassword());
        Response secondResponseLogin = client.loginCourier(existingCourierLogin);
        assertEquals(SC_OK, secondResponseLogin.statusCode());
        courierId = secondResponseLogin.body().jsonPath().getInt("id");
        client.deleteCourier(courierId);
    }

    // Создание нового курьера
    @Test
    public void creatingNewCourier() {
        newCourier = getRandomCourier();
        Response responseCreate = client.createCourier(newCourier);
        assertEquals(SC_CREATED, responseCreate.statusCode());
        String responseMessage = responseCreate.body().jsonPath().getString("ok");
        MatcherAssert.assertThat(responseMessage, true);
    }

    // Создание курьера с сществующим логином и паролем
    @Test
    public void attemptToCreateExistingCourier() {
        newCourier = getRandomCourier();
        Response responseCreate = client.createCourier(newCourier);
        assertEquals(SC_CREATED, responseCreate.statusCode());

        Response anotherResponseCreate = client.createCourier(newCourier);
        assertEquals(SC_CONFLICT, anotherResponseCreate.statusCode());

        String responseMessage = anotherResponseCreate.body().jsonPath().getString("message");
        assertEquals(responseMessage, "Этот логин уже используется. Попробуйте другой.");
    }

}
