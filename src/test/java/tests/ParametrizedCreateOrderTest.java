package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.CreateOrder;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;


@RunWith(Parameterized.class)
@DisplayName("POST /api/v1/orders | Создание заказа")
public class ParametrizedCreateOrderTest {
    public String comment;
    @Parameterized.Parameter
    public List<String> color;
    CreateOrder createOrder;

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {List.of()},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        createOrder = new CreateOrder();
        createOrder.setFirstName("Naruto");
        createOrder.setLastName("Uchiha");
        createOrder.setAddress("Konoha, 142 apt.");
        createOrder.setMetroStation("4");
        createOrder.setPhone("+7 800 355 35 35");
        createOrder.setRentTime(5);
        createOrder.setDeliveryDate("2020-06-06");
        createOrder.setComment("Saske, come back to Konoha");
        createOrder.setColor(color);
    }

    @Test
    @DisplayName("Успешное создание заказа")
    public void shouldCreateOrderTest() {
        given()
                .header("Content-type", "application/json")
                .body(createOrder)
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .and()
                .body("$", hasKey("track"));
    }


}
