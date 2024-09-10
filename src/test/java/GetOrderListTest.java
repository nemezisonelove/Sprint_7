import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import utils.APIs;
import utils.BaseURI;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsNull.notNullValue;

public class GetOrderListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseURI.BASE_URI;
    }

    @Test
    @DisplayName("Get order list")
    public void getOrderListTest() {
        given()
                .header("Content-type", "application/json")
                .when()
                .get(APIs.ORDER_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("orders", notNullValue());
    }
}