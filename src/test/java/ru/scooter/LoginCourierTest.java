package ru.scooter;

import org.apache.commons.lang3.RandomStringUtils;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.scooter.pojo.NewCourier;
import ru.scooter.pojo.ScooterApiClient;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {

    private ScooterApiClient client;
    static HashMap<String, String> randomCourierData = new HashMap<>();

    @BeforeClass
    public static void generateRandomData() {
        randomCourierData.put("login", RandomStringUtils.randomAlphabetic(8));
        randomCourierData.put("password", RandomStringUtils.randomNumeric(8));
        randomCourierData.put("firstName", RandomStringUtils.randomAlphabetic(8));
        randomCourierData.put("wrongPassword", RandomStringUtils.randomNumeric(8));
    }

    @Before
    public void setUp() {
        client = new ScooterApiClient();
    }

    @AfterClass
    public static void deleteCourier() {
        ScooterApiClient.deleteCourier(randomCourierData.get("login"),
                randomCourierData.get("password"));
    }

    // логин курьера
    @Test
    public void loginNewCourier() {

        final NewCourier newCourier = new NewCourier(randomCourierData.get("login"),
                randomCourierData.get("password"),
                randomCourierData.get("firstName"));
        client.createCourier(newCourier)
                .then()
                .statusCode(201)
                .and()
                .assertThat()
                .body("ok", equalTo(true));

        client.loginCourier(newCourier)
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("id", notNullValue());

    }

    // если авторизоваться под несуществующим пользователем, запрос возвращает ошибку
    @Test
    public void loginNewCourierThatNotExist() {
        final NewCourier newCourier = new NewCourier(randomCourierData.get("login"),
                randomCourierData.get("password"),
                randomCourierData.get("firstName"));

        client.loginCourier(newCourier)
                .then()
                .statusCode(404)
                .and()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    // система вернёт ошибку, если неправильно указать логин или пароль
    @Test
    public void incorrectCredentialsTest() {
        final NewCourier newCourier = new NewCourier(randomCourierData.get("login"),
                randomCourierData.get("password"),
                randomCourierData.get("firstName"));
        System.out.println(newCourier);
        client.createCourier(newCourier)
                .then()
                .statusCode(201)
                .and()
                .assertThat()
                .body("ok", equalTo(true));

        final NewCourier newCourier1 =
                new NewCourier(randomCourierData.get("login"), randomCourierData.get("wrongPass"),
                        randomCourierData.get("firstName"));
        System.out.println(newCourier1);

        client.loginCourier(newCourier1)
                .then()
                .statusCode(404)
                .and()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    // если какого-то поля нет, запрос возвращает ошибку
    @Test
    public void loginWithoutOneOfCred() {
        final NewCourier newCourier = new NewCourier(randomCourierData.get("login"),
                randomCourierData.get("password"),
                randomCourierData.get("firstName"));
        System.out.println(newCourier);
        client.createCourier(newCourier)
                .then()
                .statusCode(201)
                .and()
                .assertThat()
                .body("ok", equalTo(true));

        final NewCourier newCourier1 =
                new NewCourier(null, randomCourierData.get("password"),
                        randomCourierData.get("firstName"));
        System.out.println(newCourier1);

        client.loginCourier(newCourier1)
                .then()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
        }

}
