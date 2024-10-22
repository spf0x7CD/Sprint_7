package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
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
    @Parameterized.Parameter(0)
    public String firstName;
    @Parameterized.Parameter(1)
    public String lastName;
    @Parameterized.Parameter(2)
    public String address;
    @Parameterized.Parameter(3)
    public String metroStation;
    @Parameterized.Parameter(4)
    public String phone;
    @Parameterized.Parameter(5)
    public int rentTime;
    @Parameterized.Parameter(6)
    public String deliveryDate;
    @Parameterized.Parameter(7)
    public String comment;
    @Parameterized.Parameter(8)
    public List<String> color;
    CreateOrder createOrder;
    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][] {
                {"Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", List.of("BLACK")},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", List.of("GREY")},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", List.of("BLACK", "GREY")},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", List.of()},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        createOrder = new CreateOrder();
        createOrder.setFirstName(firstName);
        createOrder.setLastName(lastName);
        createOrder.setAddress(address);
        createOrder.setMetroStation(metroStation);
        createOrder.setPhone(phone);
        createOrder.setRentTime(rentTime);
        createOrder.setDeliveryDate(String.valueOf(deliveryDate));
        createOrder.setComment(comment);
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
