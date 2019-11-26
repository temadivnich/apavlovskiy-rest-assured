package com.edu.tests;

import com.edu.controller.RegisterController;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.replaceFiltersWith;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class RegisterTest {
    private RegisterController register = new RegisterController();

    @Test
    @Story("20")
    @Description("Register new user. Verify HTTP response status code. " +
            "Verify that access token is present is response body.")
    public void test_20() {
        String randomEmail = RandomStringUtils.randomAlphabetic(5).concat("@gmail.com");
        String randPassword = RandomStringUtils.randomAlphanumeric(5);
        given().spec(register.getRequestSpecification())
                .body(register.getBody(randomEmail, randPassword))
                .post()
                .then().spec(register.getResponseSpecification())
                .assertThat()
                .statusCode(SC_CREATED)
                .body("accessToken", is(notNullValue()));
    }


}
