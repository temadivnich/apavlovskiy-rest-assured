package com.edu.controller;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static com.edu.ConfigLoader.getBaseUrl;
import static io.restassured.http.ContentType.JSON;

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
}
