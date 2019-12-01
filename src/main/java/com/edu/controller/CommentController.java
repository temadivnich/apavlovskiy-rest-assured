package com.edu.controller;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CommentController implements ControllerSpecification {
  private static final String PATH = "/comments/{id}";

    public RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseRequestSpecification())
                .setBasePath(PATH)
                .log(LogDetail.URI)
                .build();
    }

    public ResponseSpecification getResponseSpecification() {
        return new ResponseSpecBuilder()
                .addResponseSpecification(getBaseResponseSpecification())
                .log(LogDetail.STATUS)
                .build();
    }

}
