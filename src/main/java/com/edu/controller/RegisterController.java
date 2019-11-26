package com.edu.controller;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.slf4j.Slf4j;

import javax.json.Json;

import static com.edu.ConfigLoader.getUserEmail;
import static com.edu.ConfigLoader.getUserPassword;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;

@Slf4j
public class RegisterController implements ControllerSpecification {

    private static final String PATH = "/register";
    private static final String PATH_LOGIN = "/login";

    public RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseRequestSpecification())
                .setBasePath(PATH)
                .log(LogDetail.ALL)
                .build();
    }

    public ResponseSpecification getResponseSpecification() {
        return new ResponseSpecBuilder()
                .addResponseSpecification(getBaseResponseSpecification())
                .log(LogDetail.ALL)
                .build();
    }

    public String getAccessToken() {
        log.info("Trying to login with email: " + getUserEmail());
        String jsonBody = getBody(getUserEmail(), getUserPassword());
        ValidatableResponse response = tryLogin(jsonBody);
        if (noSuchUser(response)) {
            log.info("No such user found. Going to register as new ");
            return registerNew(jsonBody);
        } else
            return response.extract().jsonPath().get("accessToken");
    }

    private ValidatableResponse tryLogin(String bodyJson) {
        return given().spec(getBaseRequestSpecification())
                .body(bodyJson)
                .post(RegisterController.PATH_LOGIN)
                .then().spec(getResponseSpecification());
    }

    private boolean noSuchUser(ValidatableResponse response) {
        return response.extract().body().asString().equals("\"Cannot find user\"");
    }

    private String registerNew(String bodyJson) {
        log.info("Registering new user with email: " + getUserEmail());
        return given().spec(getBaseRequestSpecification())
                .body(bodyJson)
                .post(PATH)
                .then().spec(getResponseSpecification())
                .statusCode(SC_CREATED)
                .extract().jsonPath().getString("accessToken");
    }

    public String getBody(String email, String password) {
        return Json.createObjectBuilder()
                .add("email", email)
                .add("password", password)
                .build()
                .toString();
    }

}
