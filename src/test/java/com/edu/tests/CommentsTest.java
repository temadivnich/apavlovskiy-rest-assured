package com.edu.tests;

import com.edu.controller.CommentController;
import com.edu.entity.Comment;
import com.google.common.collect.Ordering;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static com.edu.api.QueryParams.ORDER;
import static com.edu.api.QueryParams.SORT;
import static io.restassured.http.ContentType.JSON;
import static java.util.Collections.emptyMap;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.testng.Assert.assertTrue;

public class CommentsTest {

    private final CommentController commentController = new CommentController();

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
        commentController.get(emptyMap(), "").assertThat().contentType(JSON.withCharset("UTF-8"));
    }

    @Test
    @Story("11")
    @Description("Get comments with postId sorted in descending order. " +
            "Verify HTTP response status code. Verify that records are sorted in response.")
    public void test_11() {
        Map<String, String> queryParams = Map.of(SORT.value(), "postId", ORDER.value(), "desc");
        List<Long> listPostId = commentController.get(queryParams, "").extract()
                .jsonPath().getList("postId", Long.class);
        assertTrue(Ordering.natural().reverse().isOrdered(listPostId), "Response body is not sorted by postID");
    }

    @Test(dataProvider = "commentDataProvider")
    @Story("18")
    @Description("Create already existing comment entity. Verify HTTP response status code. /comments")
    public void test_18(Comment commentEntity) {
        commentController.create(commentEntity);
        commentController.create(commentEntity)
                .assertThat().statusCode(SC_CREATED);
    }

}
