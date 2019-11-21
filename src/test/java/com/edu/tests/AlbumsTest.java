package com.edu.tests;

import com.edu.controller.AlbumsController;
import com.edu.controller.ControllerSpecification;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class AlbumsTest {
    private ControllerSpecification albums = new AlbumsController();

    @Test
    @Story("3")
    @Description("Get third album (path parameter) and verify content length.")
    public void test_3() {
        ValidatableResponse response = given()
                .spec(albums.getRequestSpecification())
                .pathParam("albumId", "3")
                .when()
                .get()
                .then()
                .spec(albums.getResponseSpecification());
        int bodyLength = response.extract().body().asString().length();
        String contentLength = response.extract().header("Content-Length");
        assertThat(contentLength, is(equalTo(String.valueOf(bodyLength))));
    }

    @Test
    @Story("7")
    @Description("Get non-existing album. Verify HTTP response status code.")
    public void test_7() {
        given()
                .spec(albums.getRequestSpecification())
                .pathParam("albumId", "-1")
                .when()
                .get()
                .then()
                .spec(albums.getResponseSpecification())
                .assertThat()
                .statusCode(SC_NOT_FOUND);
    }
}
