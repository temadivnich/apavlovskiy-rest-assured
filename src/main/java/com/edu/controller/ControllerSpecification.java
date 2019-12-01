package com.edu.controller;

import com.edu.entity.Entity;
import com.edu.entity.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.Map;

import static com.edu.ConfigLoader.getBaseUrl;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.util.Collections.emptyMap;

public interface ControllerSpecification {
    RequestSpecification getRequestSpecification();

    ResponseSpecification getResponseSpecification();

    default RequestSpecification getBaseRequestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(getBaseUrl())
                .setContentType(JSON)
                .log(LogDetail.URI)
                .build();
    }

    default ResponseSpecification getBaseResponseSpecification() {
        return new ResponseSpecBuilder()
                .expectContentType(JSON.withCharset("UTF-8"))
                .build();
    }

    default ValidatableResponse get(Map<String, ?> params) {
        return given()
                .spec(getRequestSpecification())
                .queryParams(params)
                .when()
                .get()
                .then()
                .spec(getResponseSpecification());
    }


    default ValidatableResponse get(String id) {
        return get(emptyMap(), id);
    }

    default ValidatableResponse get(Map<String, ?> queryParams, String id) {
        return given()
                .spec(getRequestSpecification())
                .pathParam("id", id)
                .queryParams(queryParams)
                .when().get()
                .then().spec(getResponseSpecification());
    }

    default ValidatableResponse create(Entity postEntity, RequestSpecification requestSpec) {
        String jsonBody = getJsonBody(postEntity);
        return create(jsonBody, requestSpec);
    }

    default ValidatableResponse create(Entity postEntity) {
        return create(postEntity, getRequestSpecification()
                .pathParam("id", ""));
    }

    default ValidatableResponse create(String jsonBody, RequestSpecification requestSpec) {
        return given()
                .spec(requestSpec)
                .body(jsonBody)
                .when()
                .post()
                .then()
                .spec(getResponseSpecification());
    }


    default ValidatableResponse update(Post anotherPost, String id) {
        return given()
                .spec(getRequestSpecification())
                .pathParam("id", id)
                .body(getJsonBody(anotherPost))
                .when()
                .patch()
                .then()
                .spec(getResponseSpecification());
    }

    default ValidatableResponse delete(String id) {
        return given().spec(getRequestSpecification())
                .pathParam("id", id)
                .when().delete()
                .then().spec(getResponseSpecification());
    }

    private String getJsonBody(Entity postEntity) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(postEntity);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }


}
