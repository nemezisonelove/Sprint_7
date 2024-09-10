package courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import utils.APIs;

import static io.restassured.RestAssured.given;

public class CourierOperations {

    @Step("Create a courier")
    public static Response createCourier(Courier courier) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(APIs.COURIER_PATH);
        return response;
    }

    @Step("Sign in a courier")
    public static Response signInCourier(Courier courier) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(APIs.LOGIN_PATH);
        return response;
    }

    @Step("Delete a courier")
    public static void deleteCourier(String id) {
        if (id != null)
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(id)
                    .when()
                    .delete(APIs.COURIER_PATH + id);
    }
}