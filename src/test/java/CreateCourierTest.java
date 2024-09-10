import courier.Courier;
import courier.CourierOperations;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.BaseURI;

import static org.hamcrest.CoreMatchers.equalTo;


public class CreateCourierTest {
    private static String login;
    private static String password;
    private static String firstName;


    Courier courier;
    String id;

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseURI.BASE_URI;
        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(8);
        firstName = RandomStringUtils.randomAlphabetic(8);
    }

    @After
    public void tearDown() {
        CourierOperations.deleteCourier(id);
    }

    @Test
    @DisplayName("Create a new courier using correct data")
    public void createNewCourierGetSuccessResponse() {
        courier = new Courier(login, password, firstName);
        Response response = CourierOperations.createCourier(courier);
        //id нужен для последующего удаления курьера
        id = CourierOperations.signInCourier(courier).then().extract().path("id").toString();
        response.then().assertThat().statusCode(HttpStatus.SC_CREATED)
                .and()
                .body("ok", equalTo(true));

    }

    @Test
    @DisplayName("Create a courier without a login")
    public void createCourierWithoutLoginGetError() {
        courier = new Courier(password, firstName);
        CourierOperations.createCourier(courier)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    @DisplayName("Create a courier without a password")
    public void createCourierWithoutPasswordGetError() {
        courier = new Courier(login, firstName);
        CourierOperations.createCourier(courier)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    @DisplayName("Create a courier without a first name")
    public void createCourierWithoutFirstNameGetError() {
        courier = new Courier(login, password);
        CourierOperations.createCourier(courier)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    //Тут баг - комментарий не соответствует документации
    @Test
    @DisplayName("Create 2 similar couriers")
    public void createTwoSimilarCourierGetError() {
        courier = new Courier(login, password, firstName);
        CourierOperations.createCourier(courier);
        CourierOperations.createCourier(courier)
                .then().assertThat().statusCode(HttpStatus.SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется"));
    }
}