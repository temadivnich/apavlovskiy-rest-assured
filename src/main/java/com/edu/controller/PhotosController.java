package com.edu.controller;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class PhotosController implements ControllerSpecification {

  private static final String PATH = "/photos";
  @Override
  public RequestSpecification getRequestSpecification() {
    return new RequestSpecBuilder()
            .addRequestSpecification(getBaseRequestSpecification())
            .setBasePath(PATH)
            .log(LogDetail.ALL)
            .build();
  }

  @Override
  public ResponseSpecification getResponseSpecification() {
    return new ResponseSpecBuilder()
            .addResponseSpecification(getBaseResponseSpecification())
            .log(LogDetail.ALL)
            .build();
  }
}
