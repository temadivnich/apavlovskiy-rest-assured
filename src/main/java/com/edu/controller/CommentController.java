package com.edu.controller;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class CommentController implements ControllerSpecification {
    private static final String PATH = "/comments";

    public RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseRequestSpecification())
                .setBasePath(PATH)
                .build();
    }

    public ResponseSpecification getResponseSpecification() {
        return new ResponseSpecBuilder()
                .addResponseSpecification(getBaseResponseSpecification())
                .log(LogDetail.ALL)
                .build();
    }

}
