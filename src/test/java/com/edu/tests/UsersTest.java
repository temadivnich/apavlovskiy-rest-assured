package com.edu.tests;

import com.edu.controller.ControllerSpecification;
import com.edu.controller.UsersController;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static com.edu.api.QueryParams.NOT_EQUALS;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Collections.emptyMap;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.*;

public class UsersTest {

    private ControllerSpecification users = new UsersController();

    @DataProvider(name = "verifyGeoLocationProvider")
    public static Object[][] verifyGeoLocationProvider() {
        String expectLat = "-71.4197";
        String expectLng = "71.7478";
        String userId = "5";
        return new Object[][]{{expectLat, expectLng, userId}};
    }

    @Test(dataProvider = "verifyGeoLocationProvider")
    @Story("6")
    @Description("Get all users. Verify HTTP response status code. Verify the 5th user geo coordinates.")
    public void test_6(String expectLat, String expectLng, String userId) {
        get(emptyMap(), "")
                .assertThat().statusCode(SC_OK)
                .body("address.geo.lat["+userId+"]", is(expectLat))
                .body("address.geo.lng["+userId+"]", is(expectLng));
    }

    @Test
    @Story("9")
    @Description("Get user by street name. Verify HTTP status code. " +
            "Verify street field of returned user record. /users")
    public void test_9() {
        String streetName = "Kulas Light";
        get(Map.of("address.street", streetName), "")
                .assertThat().statusCode(SC_OK)
                .body("address.street", everyItem(is(streetName)));
    }

    @Test
    @Story("10")
    @Description("Get all users without the third one excluded by name. " +
            "Verify HTTP response status code. Verify that the third user in not present in response.")
    public void test_10() {
        String aloneLady = "Clementine Bauch";
        get(Map.of("name" + NOT_EQUALS.value(), aloneLady), "")
                .statusCode(SC_OK)
                .body("name", everyItem(is(not(aloneLady))));
    }

    @Test
    @Story("13")
    @Description("Get user by city name. Verify HTTP response status code. " +
            "Verify user with proper city is returned.")
    public void test_13() {
        String city = "Gwenborough";
        get(Map.of("address.city", city), "")
                .assertThat().statusCode(SC_OK)
                .body("address.city", everyItem(is(city)));
    }

    @Test
    @Story("17")
    @Description("Get tenth user. Verify HTTP response status code. " +
            "Verify response against JSON schema. /users")
    public void test_17() {
        get(emptyMap(), "10")
                .assertThat().statusCode(SC_OK)
                .body(matchesJsonSchemaInClasspath("userSchema.json"));
    }

  private ValidatableResponse get(Map<String, ?> queryParams, String id) {
        return given()
                .spec(users.getRequestSpecification())
                .pathParam("userId", id)
                .queryParams(queryParams)
                .when()
                .get()
                .then()
                .spec(users.getResponseSpecification());
    }
}
