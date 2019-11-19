package com.edu.tests;

import com.edu.controller.ControllerSpecification;
import com.edu.controller.PostsController;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class PostsTest {

    private ControllerSpecification posts = new PostsController();

    @Test
    @Story("1")
    @Description("Get all posts. Verify HTTP response status code and content type.")
    public void test_1() {
        given()
            .request().spec(posts.getRequestSpecification())
        .when()
            .get()
        .then()
            .spec(posts.getResponseSpecification());
    }
}
