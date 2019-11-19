package com.edu.tests;

import com.edu.controller.AlbumsController;
import com.edu.controller.ControllerSpecification;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

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

    response.assertThat()
            .header("Content-Length", String.valueOf(bodyLength));
  }
}
