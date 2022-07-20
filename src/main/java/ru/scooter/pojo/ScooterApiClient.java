package ru.scooter.pojo;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ScooterApiClient {
    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/api/v1/";

    private static final Filter requestFilter = new RequestLoggingFilter();
    private static final Filter responseFilter = new ResponseLoggingFilter();

    public Response createCourier(NewCourier newCourier) {
        return RestAssured.with()
                .filters(List.of(requestFilter, responseFilter))
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(newCourier)
                .when()
                .post("/courier");
    }

    public Response loginCourier(ExistingCourier existingCourier) {
        return RestAssured.with()
                .filters(List.of(requestFilter, responseFilter))
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(existingCourier)
                .when()
                .post("/courier/login");
    }

    public void deleteCourier(int newCourier) {
        given()
                .when()
                .delete(BASE_URL + "courier/" + newCourier)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("ok");
    }

}
