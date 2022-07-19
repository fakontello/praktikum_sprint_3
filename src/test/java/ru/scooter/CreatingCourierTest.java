package ru.scooter;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.scooter.pojo.NewCourier;
import ru.scooter.pojo.ScooterApiClient;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreatingCourierTest {

    private ScooterApiClient client;
    static HashMap<String, String> randomCourierData = new HashMap<>();

    @BeforeClass
    public static void generateRandomData() {
        randomCourierData.put("login", RandomStringUtils.randomAlphabetic(8));
        randomCourierData.put("password", RandomStringUtils.randomNumeric(8));
        randomCourierData.put("firstName", RandomStringUtils.randomAlphabetic(8));
    }

    @Before
    public void setUp() {
        client = new ScooterApiClient();
    }

    // Создание нового курьера
    @Test
    public void creatingNewCourier() {

        final NewCourier newCourier = new NewCourier(randomCourierData.get("login"),
                randomCourierData.get("password"),
                randomCourierData.get("firstName"));
        client.createCourier(newCourier)
                .then()
                .statusCode(201)
                .and()
                .assertThat()
                .body("ok", equalTo(true));
    }

    // Создание курьера с сществующим логином
    @Test
    public void attemptToCreateExistingCourier() {

        final NewCourier newCourier = new NewCourier(randomCourierData.get("login"),
                randomCourierData.get("password"),
                randomCourierData.get("firstName"));

        client.createCourier(newCourier)
                .then()
                .statusCode(201)
                .and()
                .assertThat()
                .body("ok", equalTo(true));

        client.createCourier(newCourier)
                .then()
                .statusCode(409)
                .and()
                .assertThat()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    // если одного из полей нет, запрос возвращает ошибку
    @Test
    public void attemptToCreateCourierWithoutImportantFields() {

        final NewCourier newCourier = new NewCourier(null,
                randomCourierData.get("password"),
                randomCourierData.get("firstName"));
        client.createCourier(newCourier)
                .then()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

}
