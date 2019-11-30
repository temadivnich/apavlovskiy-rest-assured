package com.edu.tests;

import com.edu.controller.PostsController;
import com.edu.controller.RegisterController;
import com.edu.entity.Post;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Set;

import static com.edu.api.QueryParams.LIMIT;
import static com.edu.api.QueryParams.PAGE;
import static io.restassured.RestAssured.given;
import static java.util.Collections.emptyMap;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.testng.Assert.assertEquals;

public class PostsTest {

    private PostsController posts = new PostsController();
    private RegisterController registerController = new RegisterController();

    @DataProvider(name = "postEntityDataProvider")
    public static Object[][] postEntityDataProvider() {
        Post post = new Post();
        post.setBody("My test post");
        post.setTitle("hello");
        post.setUserId(1L);
        return new Object[][]{{post}};
    }

    @Test
    @Story("1")
    @Description("Get all posts. Verify HTTP response status code and content type.")
    public void test_1() {
        get(emptyMap(), "").assertThat().statusCode(SC_OK);
    }

    @Test
    @Story("14")
    @Description("Get only first 10 posts. Verify HTTP response status code. " +
            "Verify that only first posts are returned. /posts")
    public void test_14() {
        var limit = 10;
        var queryParams = Map.of(PAGE.value(), "1", LIMIT.value(), limit);
        get(queryParams, "")
                .assertThat().statusCode(SC_OK)
                .and().header("Link", is(notNullValue()))
                .and().body("", hasSize(limit));
    }

    @Test()
    @Story("15")
    @Description("Get posts with id = 55 and id = 60. " +
            "Verify HTTP response status code. Verify id values of returned records. /posts")
    public void test_15() {
        get(Map.of("id", Set.of("55", "60")), "")
                .assertThat().statusCode(SC_OK)
                .body("id", hasItems(55, 60))
                .body("id", hasSize(2));
    }

    @Test(dataProvider = "postEntityDataProvider")
    @Story("19")
    @Description("Create a post. Verify HTTP response status code. /664/posts")
    public void test_19(Post postEntity) {
        create(postEntity, posts.getRequestSpecification().basePath("/664/posts"))
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test(dataProvider = "postEntityDataProvider")
    @Story("21")
    @Description("Create post with adding access token in header. " +
            "Verify HTTP response status code. Verify post is created. /664/posts")
    public void test_21(Post postEntity) {
        String authToken = registerController.getAccessToken();
        Post postResponse = create(postEntity, posts.getRequestSpecification()
                .basePath("/664/posts")
                .auth().preemptive().oauth2(authToken))
                .assertThat().statusCode(SC_CREATED)
                .extract().as(Post.class);
        Post actuallyCreated = get(emptyMap(), postResponse.getId().toString())
                .extract().body().as(Post.class);
        assertEquals(postEntity.getTitle(), actuallyCreated.getTitle());
        assertEquals(postEntity.getBody(), actuallyCreated.getBody());
        assertEquals(postEntity.getUserId(), actuallyCreated.getUserId());
    }

    @Test(dataProvider = "postEntityDataProvider") //same as test_21 ?
    @Story("22")
    @Description("Create post entity and verify that the entity is created. " +
            "Verify HTTP response status code. Use JSON in body. /posts")
    public void test_22(Post postEntity) {
        Post postResponse = create(postEntity)
                .assertThat().statusCode(SC_CREATED)
                .extract().as(Post.class);
        Post actuallyCreated = get(emptyMap(), postResponse.getId().toString())
                .assertThat().statusCode(SC_OK)
                .extract().body().as(Post.class);
        assertEquals(postEntity.getTitle(), actuallyCreated.getTitle());
        assertEquals(postEntity.getBody(), actuallyCreated.getBody());
        assertEquals(postEntity.getUserId(), actuallyCreated.getUserId());
    }

    @Test(dataProvider = "postEntityDataProvider")
    @Story("23")
    @Description("Update non-existing entity. Verify HTTP response status code.  /posts")
    public void test_23(Post postEntity) {
        update(postEntity, "-1")
                .assertThat().statusCode(SC_NOT_FOUND);
    }

    @Test(dataProvider = "postEntityDataProvider")
    @Story("24")
    @Description("Create post entity and update the created entity. " +
            "Verify HTTP response status code and verify that the entity is updated.")
    public void test_24(Post expectedPost) {
        Post createdPost = create(expectedPost)
                .assertThat().statusCode(SC_CREATED)
                .extract().as(Post.class);

        expectedPost.setTitle("Title updated");
        update(expectedPost, createdPost.getId().toString())
                .assertThat().statusCode(SC_OK);

        Post actualPost = get(emptyMap(), createdPost.getId().toString())
                .assertThat().statusCode(SC_OK)
                .extract().as(Post.class);
        assertEquals(expectedPost.getTitle(), actualPost.getTitle());
    }

    @Test
    @Story("25")
    @Description("Delete non-existing post entity. Verify HTTP response status code.")
    public void test_25() {
        delete("-1").assertThat().statusCode(SC_NOT_FOUND);
    }

    @Test(dataProvider = "postEntityDataProvider")
    @Story("26")
    @Description("Create post entity, update the created entity, and delete the entity. " +
            "Verify HTTP response status code and verify that the entity is deleted.")
    public void test_26(Post expectedPost) {
        String createdPostId = create(expectedPost)
                .assertThat().statusCode(SC_CREATED)
                .extract().as(Post.class).getId().toString();

        expectedPost.setTitle("Title updated");
        update(expectedPost, createdPostId)
                .assertThat().statusCode(SC_OK);

        delete(createdPostId)
                .assertThat().statusCode(SC_OK);

        get(emptyMap(), createdPostId)
                .assertThat().statusCode(SC_NOT_FOUND);
    }

    private ValidatableResponse get(Map<String, ?> queryParams, String id) {
        return given().spec(posts.getRequestSpecification())
                .pathParam("postId", id)
                .queryParams(queryParams)
                .when().get()
                .then().spec(posts.getResponseSpecification());
    }

    private ValidatableResponse update(Post anotherPost, String id) {
        return given()
                .spec(posts.getRequestSpecification())
                .pathParam("postId", id)
                .body(posts.getJsonBody(anotherPost))
                .when()
                .patch()
                .then()
                .spec(posts.getResponseSpecification());
    }

    private ValidatableResponse create(Post postEntity, RequestSpecification requestSpec) {
        return given()
                .spec(requestSpec)
                .body(posts.getJsonBody(postEntity))
                .when()
                .post()
                .then()
                .spec(posts.getResponseSpecification());
    }

    private ValidatableResponse create(Post postEntity) {
        return create(postEntity, posts.getRequestSpecification()
                .pathParam("postId", ""));
    }

    private ValidatableResponse delete(String id) {
        return given().spec(posts.getRequestSpecification())
                .pathParam("postId", id)
                .when().delete()
                .then().spec(posts.getResponseSpecification());
    }
}
