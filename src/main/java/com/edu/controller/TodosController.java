package com.edu.controller;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class TodosController implements ControllerSpecification {

    private static final String PATH = "/todos/{taskId}";

    @Override
    public RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseRequestSpecification())
                .setBasePath(PATH)
                .build();
    }

    @Override
    public ResponseSpecification getResponseSpecification() {
        return new ResponseSpecBuilder()
                .addResponseSpecification(getBaseResponseSpecification())
                .log(LogDetail.URI)
                .build();
    }
}
