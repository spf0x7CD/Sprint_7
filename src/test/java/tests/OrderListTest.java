package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("GET /api/v1/orders | Получить список заказов")
public class OrderListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Успешное получение списка заказов")
    public void shouldGetOrderListTest() {
        given()
                .when()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .and()
                .body("$", hasKey("orders"));
        // Не знаю как проверить что ключ содержит именно список со значениями. Пробовал .body("orders", array(matchesRegex(".+"))) - не работает
        // По аналогии с тестом авторизации сделал только проверку наличия ключа
    }
}
