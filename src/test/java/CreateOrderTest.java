import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.Order;
import order.OrderOperations;
import order.OrderScooterColors;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.BaseURI;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    String track;
    private final List<String> color;

    public CreateOrderTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Scooter color - {0}")
    public static Object[][] chooseColor() {
        return new Object[][]{
                {List.of(OrderScooterColors.BLACK_COLOR)},
                {List.of(OrderScooterColors.GREY_COLOR)},
                {List.of(OrderScooterColors.BLACK_COLOR, OrderScooterColors.GREY_COLOR)},
                {List.of()},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseURI.BASE_URI;
    }

    @After
    public void tearDown() {
        OrderOperations.cancelOrder(track);
    }

    @Test
    @DisplayName("Create an order using different scooter colors")
    public void createOrderWithDifferentColorsGetSuccess() {
        Order order = new Order("Ирина", "Бобарыкина", "Торжковская 15", "Чёрная речка", "89123456789", 5, "2023-04-23", "Позвонить за час до доставки", color);
        Response response = OrderOperations.createOrder(order);
        //track для последующего удаления заказа
        track = response.then().extract().path("track").toString();
        response.then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .and()
                .body("track", notNullValue());

    }
}