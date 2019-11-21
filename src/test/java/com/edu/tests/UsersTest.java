package com.edu.tests;

import com.edu.controller.ControllerSpecification;
import com.edu.controller.UsersController;
import com.edu.entity.GeoLocation;
import com.edu.entity.User;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
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

    @Test(dataProvider ="verifyGeoLocationProvider")
    @Story("6")
    @Description("Get all users. Verify HTTP response status code. Verify the 5th user geo coordinates.")
    public void test_6(String expectLat, String expectLng, long userId) {
        User[] allUsers = given()
                .spec(users.getRequestSpecification())
                .when()
                .get()
                .then()
                .spec(users.getResponseSpecification())
                .statusCode(HttpStatus.SC_OK)
                .extract().as(User[].class);
        List<User> fifthUser = List.of(allUsers).stream()
                .filter(user -> user.getId() == userId)
                .collect(Collectors.toList());
        assertEquals(expectLat, fifthUser.get(0).getAddress().getGeo().getLatitude());
        assertEquals(expectLng, fifthUser.get(0).getAddress().getGeo().getLongitude());
    }
}
