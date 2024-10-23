package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.CourierAuthorize;
import pojo.CourierCreate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("POST /api/v1/courier/login | Авторизация курьера")
public class CourierAuthorizeTest {
    private CourierAuthorize courier;
    private Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        String randFourDigits = String.format("%04d", System.currentTimeMillis() % 10000);
        String login = "log" + randFourDigits;
        courier = new CourierAuthorize(login, randFourDigits);
        CourierCreate courierCreate = new CourierCreate(login, randFourDigits, "Test");
        given()
                .header("Content-type", "application/json")
                .body(courierCreate)
                .when()
                .post("/api/v1/courier");
    }

    @Test
    @Description("Успешная авторизация курьера")
    public void shouldAuthorizeCourierTest() {
        response = authorizeCourier(courier)
                .then()
                .statusCode(200)
                .body("$", hasKey("id"))
                .extract()
                .response();
        // Не знаю как проверить что ключ содержит номер. Пробовал .body("body", matchesRegex("[0-9]+")) - не работает,
        // поэтому сделал проверку только на наличие ключа
    }

    @Test
    @Description("Неудачная авторизация курьера при неверном логине")
    public void shouldNotAuthorizeWithWrongCourierLoginTest() {
        String revLogin = new StringBuilder(courier.getLogin()).reverse().toString();
        courier.setLogin(revLogin);
        response = authorizeCourier(courier)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"))
                .extract()
                .response();
    }

    @Test
    @Description("Неудачная авторизация курьера при неверном пароле")
    public void shouldNotAuthorizeCourierTest() {
        String revPassword = new StringBuilder(courier.getPassword()).reverse().toString();
        courier.setPassword(revPassword);
        response = authorizeCourier(courier)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"))
                .extract()
                .response();
    }

    @Test
    @Description("Неудачная авторизация курьера при пустом логине")
    public void shouldNotAuthorizeWithEmptyCourierLoginTest() {
        courier.setLogin("");
        response = authorizeCourier(courier)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"))
                .extract()
                .response();
    }

    @Test
    @Description("Неудачная авторизация курьера при пустом пароле")
    public void shouldNotAuthorizeWithEmptyCourierPasswordTest() {
        courier.setPassword("");
        response = authorizeCourier(courier)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"))
                .extract()
                .response();
    }

    @Test
    @Description("Неудачная авторизация курьера при отсутствии логина")
    public void shouldNotAuthorizeWithoutCourierLoginTest() throws NullPointerException {
        courier.setLogin(null);
        response = authorizeCourier(courier)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"))
                .extract()
                .response();
    }

    @Test
    @Description("Неудачная авторизация курьера при отсутствии пароля")
    public void shouldNotAuthorizeWithoutCourierPasswordTest() throws NullPointerException {
        courier.setPassword(null);
        response = authorizeCourier(courier)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"))
                .extract()
                .response();
    }

    @Step
    public Response authorizeCourier(CourierAuthorize courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
    }

    @After
    public void tearDown() {
        String id = response.jsonPath().getString("id");
        String deleteBody = String.format("{\"id\":\"%s\"}", id);
        given()
                .header("Content-type", "application/json")
                .body(deleteBody)
                .when()
                .delete(String.format("/api/v1/courier/%s", id));
    }
}
