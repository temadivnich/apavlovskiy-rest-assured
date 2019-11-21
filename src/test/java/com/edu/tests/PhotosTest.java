package com.edu.tests;

import com.edu.controller.ControllerSpecification;
import com.edu.controller.PhotosController;
import io.qameta.allure.Story;
import jdk.jfr.Description;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.*;

public class PhotosTest {

    private ControllerSpecification photos = new PhotosController();

    @Test
    @Story("4")
    @Description("Get all photos and verify that content length header is absent in response.")
    public void test_4() {
        given()
                .spec(photos.getRequestSpecification())
                .when()
                .get()
                .then()
                .spec(photos.getResponseSpecification())
                .assertThat()
                .header("Content-Length", is(nullValue()));
    }

    @Test
    @Story("5")
    @Description("Verify response time for photos, endpoint is less than 10 seconds.")
    public void test_5() {
        given()
                .spec(photos.getRequestSpecification())
                .when()
                .get()
                .then()
                .spec(photos.getResponseSpecification())
                .assertThat()
                .time(lessThan(10L), SECONDS);

    }
}
