package com.edu.tests;

import com.edu.controller.CommentController;
import com.edu.controller.ControllerSpecification;
import com.google.common.collect.Ordering;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;

public class CommentsTest {

    private ControllerSpecification commentController = new CommentController();

    @Test
    @Story("2")
    @Description("Get all comments and verify response charset.")
    public void test_2() {
        given()
            .spec(commentController.getRequestSpecification())
        .when()
            .get()
        .then()
            .spec(commentController.getResponseSpecification());
    }

    @Test
    @Story("11")
    @Description("Get comments with postId sorted in descending order. " +
            "Verify HTTP response status code. Verify that records are sorted in response.")
    public void test_11() {
        ValidatableResponse response = given()
                .request().spec(commentController.getRequestSpecification())
                .queryParams(Map.of("_sort", "postId", "_order", "desc"))
                .when()
                .get()
                .then()
                .spec(commentController.getResponseSpecification());
        List<Long> listPostId = response.extract()
                .jsonPath().getList("postId", Long.class);
        assertTrue(Ordering.natural().reverse().isOrdered(listPostId));
    }
}
