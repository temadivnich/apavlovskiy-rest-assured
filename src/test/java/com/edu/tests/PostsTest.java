package com.edu.tests;

import com.edu.controller.PostsController;
import com.edu.controller.RegisterController;
import com.edu.entity.Post;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
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
        getPost("").assertThat().statusCode(SC_OK);
    }

    @Test(dataProvider = "postEntityDataProvider")
    @Story("19")
    @Description("Create a post. Verify HTTP response status code. /664/posts")
    public void test_19(Post postEntity) {
        given()
                .spec(posts.getRequestSpecification())
                .basePath("/664/posts")
                .body(posts.getJsonBody(postEntity))
                .when()
                .post()
                .then()
                .spec(posts.getResponseSpecification())
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test(dataProvider = "postEntityDataProvider")
    @Story("21")
    @Description("Create post with adding access token in header. " +
            "Verify HTTP response status code. Verify post is created. /664/posts")
    public void test_21(Post postEntity) {
        String authToken = registerController.getAccessToken();
        Post postResponse = given().spec(posts.getRequestSpecification())
                .basePath("/664/posts")
                .auth().preemptive().oauth2(authToken)
                .body(posts.getJsonBody(postEntity))
                .when().post()
                .then().spec(posts.getResponseSpecification())
                .assertThat()
                .statusCode(SC_CREATED)
                .extract().as(Post.class);
        Post actuallyCreated = given()
                .spec(posts.getRequestSpecification())
                .pathParam("postId", postResponse.getId())
                .when().get().then().extract().body().as(Post.class);
        assertEquals(postEntity.getTitle(), actuallyCreated.getTitle());
        assertEquals(postEntity.getBody(), actuallyCreated.getBody());
        assertEquals(postEntity.getUserId(), actuallyCreated.getUserId());
    }

    @Test(dataProvider = "postEntityDataProvider") //same as test_21 ?
    @Story("22")
    @Description("Create post entity and verify that the entity is created. " +
            "Verify HTTP response status code. Use JSON in body. /posts")
    public void test_22(Post postEntity) {
        Post postResponse = createPost(postEntity)
                .assertThat().statusCode(SC_CREATED)
                .extract().as(Post.class);
        Post actuallyCreated = getPost(postResponse.getId().toString())
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
        updatePost(postEntity, "-1")
                .assertThat().statusCode(SC_NOT_FOUND);

    }

    @Test(dataProvider = "postEntityDataProvider")
    @Story("24")
    @Description("Create post entity and update the created entity. " +
            "Verify HTTP response status code and verify that the entity is updated.")
    public void test_24(Post expectedPost) {
        Post createdPost = createPost(expectedPost)
                .assertThat().statusCode(SC_CREATED)
                .extract().as(Post.class);

        expectedPost.setTitle("Title updated");
        updatePost(expectedPost, createdPost.getId().toString())
                .assertThat().statusCode(SC_OK);

        Post actualPost = getPost(createdPost.getId().toString())
                .assertThat().statusCode(SC_OK)
                .extract().as(Post.class);
        assertEquals(expectedPost.getTitle(), actualPost.getTitle());
    }

    @Test
    @Story("25")
    @Description("Delete non-existing post entity. Verify HTTP response status code.")
    public void test_25() {
        deletePost("-1").assertThat().statusCode(SC_NOT_FOUND);
    }

    @Test(dataProvider = "postEntityDataProvider")
    @Story("26")
    @Description("Create post entity, update the created entity, and delete the entity. " +
            "Verify HTTP response status code and verify that the entity is deleted.")
    public void test_26(Post expectedPost) {
        String createdPostId = createPost(expectedPost)
                .assertThat().statusCode(SC_CREATED)
                .extract().as(Post.class).getId().toString();

        expectedPost.setTitle("Title updated");
        updatePost(expectedPost, createdPostId)
                .assertThat().statusCode(SC_OK);

        deletePost(createdPostId)
                .assertThat().statusCode(SC_OK);

        getPost(createdPostId)
                .assertThat().statusCode(SC_NOT_FOUND);
    }

    private ValidatableResponse getPost(String id) {
        return given().spec(posts.getRequestSpecification())
                .pathParam("postId", id)
                .when().get()
                .then().spec(posts.getResponseSpecification());
    }

    private ValidatableResponse updatePost(Post anotherPost, String id) {
        return given()
                .spec(posts.getRequestSpecification())
                .pathParam("postId", id)
                .body(posts.getJsonBody(anotherPost))
                .when()
                .patch()
                .then()
                .spec(posts.getResponseSpecification());
    }

    private ValidatableResponse createPost(Post postEntity) {
        return given()
                .spec(posts.getRequestSpecification())
                .pathParam("postId", "")
                .body(posts.getJsonBody(postEntity))
                .when()
                .post()
                .then()
                .spec(posts.getResponseSpecification());
    }

    private ValidatableResponse deletePost(String id) {
        return given().spec(posts.getRequestSpecification())
                .pathParam("postId", id)
                .when().delete()
                .then().spec(posts.getResponseSpecification());
    }
}
