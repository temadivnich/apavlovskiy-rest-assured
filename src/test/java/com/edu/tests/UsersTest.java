package com.edu.tests;

import com.edu.controller.ControllerSpecification;
import com.edu.controller.UsersController;
import com.edu.entity.User;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.edu.api.QueryParams.NOT_EQUALS;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Collections.emptyMap;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

public class UsersTest {

    private ControllerSpecification users = new UsersController();

    @DataProvider(name = "verifyGeoLocationProvider")
    public static Object[][] verifyGeoLocationProvider() {
        String expectLat = "-31.8129";
        String expectLng = "62.5342";
        long userId = 5L;
        return new Object[][]{{expectLat, expectLng, userId}};
    }

    @Test(dataProvider = "verifyGeoLocationProvider")
    @Story("6")
    @Description("Get all users. Verify HTTP response status code. Verify the 5th user geo coordinates.")
    public void test_6(String expectLat, String expectLng, long userId) {
        User[] allUsers = getUser(emptyMap(), "")
                .statusCode(SC_OK)
                .extract().as(User[].class);
        List<User> fifthUser = List.of(allUsers).stream()
                .filter(user -> user.getId() == userId)
                .collect(Collectors.toList());
        assertEquals(expectLat, fifthUser.get(0).getAddress().getGeo().getLatitude());
        assertEquals(expectLng, fifthUser.get(0).getAddress().getGeo().getLongitude());
    }

    @Test
    @Story("9")
    @Description("Get user by street name. Verify HTTP status code. " +
            "Verify street field of returned user record. /users")
    public void test_9() {
        String streetName = "Kulas Light";
        User[] userResponse = getUser(Map.of("address.street", streetName), "")
                .statusCode(SC_OK)
                .extract().as(User[].class);
        Stream.of(userResponse).forEach(entity -> assertEquals(streetName, entity.getAddress().getStreet()));
    }

    @Test
    @Story("10")
    @Description("Get all users without the third one excluded by name. " +
            "Verify HTTP response status code. Verify that the third user in not present in response.")
    public void test_10() {
        String aloneLady = "Clementine Bauch";
        User[] userResponse = getUser(Map.of("name" + NOT_EQUALS.value(), aloneLady), "")
                .statusCode(SC_OK)
                .extract().as(User[].class);
        Stream.of(userResponse).filter(entity -> entity.getName() != null)
                .forEach(user -> assertThat(aloneLady, is(not(equalTo(user.getName())))));
    }

    @Test
    @Story("13")
    @Description("Get user by city name. Verify HTTP response status code. " +
            "Verify user with proper city is returned.")
    public void test_13() {
        String city = "Gwenborough";
        User[] userResponse = getUser(Map.of("address.city", city), "")
                .statusCode(SC_OK)
                .extract().as(User[].class);
        Stream.of(userResponse).forEach(entity -> assertEquals(city, entity.getAddress().getCity()));
    }

    @Test
    @Story("17")
    @Description("Get tenth user. Verify HTTP response status code. " +
            "Verify response against JSON schema. /users")
    public void test_17() {
        getUser(emptyMap(), "10")
                .assertThat().statusCode(SC_OK)
                .body(matchesJsonSchemaInClasspath("userSchema.json"));
    }

    private ValidatableResponse getUser(Map<String, ?> queryParams, String id) {
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
