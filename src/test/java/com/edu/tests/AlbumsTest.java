package com.edu.tests;

import com.edu.controller.AlbumsController;
import com.edu.controller.ControllerSpecification;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.hamcrest.Matchers.equalTo;

public class AlbumsTest {
    private ControllerSpecification albums = new AlbumsController();

    @Test
    @Story("3")
    @Description("Get third album (path parameter) and verify content length.")
    public void test_3() {
        get("3").assertThat()
                .header("Content-Length", resp -> equalTo(String.valueOf(resp.asString().length())));
    }

    @Test
    @Story("7")
    @Description("Get non-existing album. Verify HTTP response status code.")
    public void test_7() {
        get("-1").assertThat().statusCode(SC_NOT_FOUND);
    }

    private ValidatableResponse get(String id) {
        return given()
                .spec(albums.getRequestSpecification())
                .pathParam("albumId", id)
                .when()
                .get()
                .then()
                .spec(albums.getResponseSpecification());
    }
}
