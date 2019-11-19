package com.edu.controller;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

public class PostsController implements ControllerSpecification {
    private static final String PATH = "/posts";

    public RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseRequestSpecification())
                .setBasePath(PATH)
//                .addHeaders(getBaseHeader())
//                .log(LogDetail.ALL)
                .build();
    }

    public ResponseSpecification getResponseSpecification() {
        return new ResponseSpecBuilder()
                .addResponseSpecification(getBaseResponseSpecification())
                .expectStatusCode(HttpStatus.SC_OK)
                .log(LogDetail.ALL)
                .build();
    }
}
