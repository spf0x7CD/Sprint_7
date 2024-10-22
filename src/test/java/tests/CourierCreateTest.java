package tests;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import pojo.CourierCreate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("POST /api/v1/courier | Создание нового курьера")
public class CourierCreateTest {
    private CourierCreate courier;
    private String login;
    private String password;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        String randFourDigits = String.format("%04d", System.currentTimeMillis() % 10000);
        login = "log" + randFourDigits;
        password = randFourDigits;
        courier = new CourierCreate(login, password, "Test");
    }

    @Test
    @DisplayName("Успешное создание нового курьера возвращает статус 201 и параметр \"ok\": true в теле ответа")
    public void shouldCreateNewCourierTest() {
        createCourier(courier)
                .then()
                .statusCode(201)
                .and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Курьер не создается при пустом логине")
    public void shouldNotCreateNewCourierWithEmptyLoginTest() {
        courier.setLogin("");
        createCourier(courier)
                .then()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
        ;
    }

    @Test
    @DisplayName("Курьер не создается при пустом пароле")
    public void shouldNotCreateNewCourierWithEmptyPasswordTest() {
        courier.setPassword("");
        createCourier(courier)
                .then()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
        ;
    }

    @Test
    @DisplayName("Курьер не создается при пустом имени")
    public void shouldNotCreateNewCourierWithEmptyFirstNameTest() {
        courier.setFirstName("");
        createCourier(courier)
                .then()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
        ;
    }

    @Test
    @DisplayName("Курьер не создается при отсутвии значения логина")
    public void shouldNotCreateNewCourierWithoutLoginValueTest() throws NullPointerException {
        courier.setLogin(null);
        createCourier(courier)
                .then()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Курьер не создается при отсутвии значения пароля")
    public void shouldNotCreateNewCourierWithoutPasswordValueTest() throws NullPointerException {
        courier.setPassword(null);
        createCourier(courier)
                .then()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
        ;
    }

    @Test
    @DisplayName("Курьер не создается при отсутвии значения имени")
    public void shouldNotCreateNewCourierWithoutFirstNameValueTest() throws NullPointerException {
        courier.setFirstName(null);
        createCourier(courier)
                .then()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Нельзя создать курьера с одинаковым логином")
    public void shouldNotCreateAnotherCourierWithTheSameLoginTest() {
        createCourier(courier)
                .then()
                .statusCode(201);
        createCourier(courier)
                .then()
                .statusCode(409)
                .and()
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Step
    @DisplayName("POST /api/v1/courier")
    public Response createCourier(CourierCreate courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @After
    public void tearDown() {
        String loginBody = String.format("{\"login\":\"%s\",\"password\":\"%s\"}", login, password);
        String id = given()
                .header("Content-type", "application/json")
                .body(loginBody)
                .when()
                .post("/api/v1/courier/login")
                .jsonPath()
                .getString("id");
        String deleteBody = String.format("{\"id\":\"%s\"}", id);
        given()
                .header("Content-type", "application/json")
                .body(deleteBody)
                .when()
                .delete(String.format("/api/v1/courier/%s", id));
    }

}
