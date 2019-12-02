package com.edu.tests;

import com.edu.controller.AlbumsController;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.hamcrest.Matchers.equalTo;

public class AlbumsTest {
    private final AlbumsController albums = new AlbumsController();

    @Test
    @Story("3")
    @Description("Get third album (path parameter) and verify content length.")
    public void test_3() {
        albums.get("3").assertThat()
                .header("Content-Length", resp -> equalTo(String.valueOf(resp.asString().length())));
    }

    @Test
    @Story("7")
    @Description("Get non-existing album. Verify HTTP response status code.")
    public void test_7() {
        albums.get("-1").assertThat().statusCode(SC_NOT_FOUND);
    }

}
