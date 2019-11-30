package com.edu.tests;

import com.edu.controller.ControllerSpecification;
import com.edu.controller.PhotosController;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

import java.util.Map;

import static com.edu.api.QueryParams.END;
import static com.edu.api.QueryParams.START;
import static io.restassured.RestAssured.given;
import static java.util.Collections.emptyMap;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.*;

public class PhotosTest {

    private ControllerSpecification photos = new PhotosController();

    @Test
    @Story("4")
    @Description("Get all photos and verify that content length header is absent in response.")
    public void test_4() {
        get(emptyMap())
                .assertThat().header("Content-Length", is(nullValue()));
    }

    @Test
    @Story("5")
    @Description("Verify response time for photos, endpoint is less than 10 seconds.")
    public void test_5() {
        get(emptyMap())
                .assertThat().time(lessThan(10L), SECONDS);
    }

    @Test
    @Story("12")
    @Description("Get photos from the third album. Verify HTTP response status code. " +
            "Verify that only photos from third album are returned. /photos")
    public void test_12() {
        int albumId = 3;
        get(Map.of("albumId", albumId))
                .assertThat().statusCode(SC_OK)
                .body("albumId", everyItem(is(albumId)));
    }

    @Test
    @Story("16")
    @Description("Get photos from first album in range from 20th to 25th. " +
            "Verify HTTP response status code. Verify returned album and photo ids. /photos")
    public void test_16() {
        Map<String, String> queryParams = Map.of("albumId", "1",
                START.value(), "20", END.value(), "25");
        get(queryParams)
                .assertThat().statusCode(SC_OK)
                .body("albumId", everyItem(is(1)))
                .body("id", hasItems(21, 22, 23, 24, 25))
                .body("id", hasSize(5));
    }

    private ValidatableResponse get(Map<String, ?> params) {
        return given()
                .spec(photos.getRequestSpecification())
                .queryParams(params)
                .when()
                .get()
                .then()
                .spec(photos.getResponseSpecification());
    }
}
