package com.edu.tests;

import com.edu.controller.CommentController;
import com.edu.entity.Comment;
import com.google.common.collect.Ordering;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static com.edu.api.QueryParams.ORDER;
import static com.edu.api.QueryParams.SORT;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.util.Collections.emptyMap;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.testng.Assert.assertTrue;

public class CommentsTest {

    private CommentController commentController = new CommentController();

    @DataProvider(name = "commentDataProvider")
    public static Object[][] commentDataProvider() {
        Comment comment = new Comment();
        comment.setPostId(1L);
        comment.setName("id labore ex et quam laborum");
        comment.setEmail("Eliseo@gardner.biz");
        comment.setBody("laudantium enim quasi est quidem magnam voluptate ipsam eos" +
                "\ntempora quo necessitatibus" +
                "\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium");
        return new Object[][]{{comment}};
    }

    @Test
    @Story("2")
    @Description("Get all comments and verify response charset.")
    public void test_2() {
        getComment(emptyMap()).assertThat().contentType(JSON.withCharset("UTF-8"));
    }

    @Test
    @Story("11")
    @Description("Get comments with postId sorted in descending order. " +
            "Verify HTTP response status code. Verify that records are sorted in response.")
    public void test_11() {
        ValidatableResponse response = getComment(Map.of(SORT.value(), "postId", ORDER.value(), "desc"));
        List<Long> listPostId = response.extract()
                .jsonPath().getList("postId", Long.class);
        assertTrue(Ordering.natural().reverse().isOrdered(listPostId));
    }

    @Test(dataProvider = "commentDataProvider")
    @Story("18")
    @Description("Create already existing comment entity. Verify HTTP response status code. /comments")
    public void test_18(Comment commentEntity) {
        createComment(commentEntity);
        createComment(commentEntity)
                .assertThat().statusCode(SC_CREATED);
    }

    private ValidatableResponse getComment(Map<String, ?> params) {
        return given()
                .spec(commentController.getRequestSpecification())
                .queryParams(params)
                .when()
                .get()
                .then()
                .spec(commentController.getResponseSpecification());
    }

    private ValidatableResponse createComment(Comment bodyJson) {
        return given().spec(commentController.getRequestSpecification())
                .body(commentController.getJsonBody(bodyJson))
                .when().post()
                .then().spec(commentController.getResponseSpecification());
    }
}
