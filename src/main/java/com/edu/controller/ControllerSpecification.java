package com.edu.controller;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.nio.charset.Charset;

import static com.edu.ConfigLoader.getBaseUrl;
import static io.restassured.http.ContentType.JSON;

public interface ControllerSpecification {
    RequestSpecification getRequestSpecification();

    ResponseSpecification getResponseSpecification();

    default RequestSpecification getBaseRequestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(getBaseUrl())
                .setContentType(JSON)
                .build();
    }

    default ResponseSpecification getBaseResponseSpecification() {
        return new ResponseSpecBuilder()
                .expectContentType(JSON.withCharset("UTF-8"))
                .build();
    }
}
